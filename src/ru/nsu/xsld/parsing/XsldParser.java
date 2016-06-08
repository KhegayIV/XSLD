package ru.nsu.xsld.parsing;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import ru.nsu.xsld.utills.ElementUtils;
import ru.nsu.xsld.XsldException;
import ru.nsu.xsld.rules.Rule;
import ru.nsu.xsld.rules.xsldrules.AllowRule;
import ru.nsu.xsld.rules.xsldrules.AssertRule;
import ru.nsu.xsld.rules.xsldrules.RequireRule;
import ru.nsu.xsld.rules.xsldrules.TypeAssertRule;
import ru.nsu.xsld.utills.StreamUtils;

import java.util.*;
import java.util.stream.Stream;

import static ru.nsu.xsld.utills.ElementUtils.XSD_NAMESPACE;
import static ru.nsu.xsld.utills.ElementUtils.XSLD_NAMESPACE;

/**
 * Created by Илья on 07.06.2016.
 */
public class XsldParser {
    private Map<UnresolvedPath, String> pathLabels = new HashMap<>();
    private List<Rule> rules = new ArrayList<>();

    private Map<String, Element> typeElements = new HashMap<>();

    public XsldParser(Element schema) throws XsldException {
        for (Element element : ElementUtils.descendantsByName(schema, XSD_NAMESPACE, "complexType")) {
            typeElements.put(element.getAttribute("name"), element);
        }


        Element root = getRoot(schema).orElseThrow(() -> new XsldException("No root found"));
        lookupElement(root, new UnresolvedPath());
        System.out.printf("");
    }

    public LabelMap createMap(){
        return new LabelMap(pathLabels);
    }

    private static Optional<Element> getRoot(Element schema) {
        return ElementUtils.childrenStreamByName(schema, XSD_NAMESPACE, "element")
                .findFirst();
    }

    /**
     * Recursively searches for elements and attributes inside
     *
     * @param parentPath UnresolvedPath to this node, not inclusive
     */
    private void lookupElement(Element element, UnresolvedPath parentPath) throws XsldException {
        UnresolvedPath path = parentPath.append(element.getAttribute("name"));
        inspect(element, parentPath);
        Optional<Element> type;
        if (element.hasAttribute("type")) {
            type = Optional.ofNullable(typeElements.get(element.getAttribute("type")));
        } else {
            type = ElementUtils.childrenStreamByName(element, XSD_NAMESPACE, "complexType").findAny();
        }
        while (type.isPresent()) {

            //Collect attributes from type and restriction/extension
            Stream<Element> attributes = StreamUtils.concat(
                    ElementUtils.childrenStreamByName(type.get(), XSD_NAMESPACE, "attribute"),
                    ElementUtils
                            .childrenOneOf(type.get(), XSD_NAMESPACE, "simpleContent", "complexContent")
                            .flatMap(it -> ElementUtils.childrenOneOf(it, "restriction", "extension"))
                            .flatMap(it -> ElementUtils.childrenStreamByName(it, XSD_NAMESPACE, "attribute"))
            );
            for (Element attribute : (Iterable<Element>) attributes::iterator){
                inspect(attribute, path);
            }

            //Collect elements everywhere
            Stream<Element> childElems = ElementUtils.childrenOneOf(type.get(), XSD_NAMESPACE,
                    "choice", "all", "sequence").flatMap( it ->
                    ElementUtils.childrenStreamByName(it, XSD_NAMESPACE, "element"));
            for (Element child : (Iterable<Element>) childElems::iterator) {
                lookupElement(child, path);
            }

            //Search for inheritance
            type = ElementUtils.childrenStreamByPath(type.get(), XSD_NAMESPACE, "complexContent", "extension")
                    .findAny()
                    .map( it -> it.getAttribute("base")).map(typeElements::get);
        }
    }

    /**
     * Inspects xs:element or xs:attribute for labels and rules
     *
     * @param parentPath UnresolvedPath to this node, not inclusive
     */
    private void inspect(Element element, UnresolvedPath parentPath) throws XsldException {
        UnresolvedPath path = parentPath.append(element.getAttribute("name"));
        Attr labelNode = element.getAttributeNodeNS(XSLD_NAMESPACE, "label");
        if (labelNode != null) {
            addLabel(path, labelNode.getValue());
        }

        for (Element allow : ElementUtils.descendantsByName(element, XSLD_NAMESPACE, "allow")) {
            if (!allow.hasAttribute("error"))
                throw new XsldException("Require rule in " + element.getAttribute("name") + " doesn't have error specified");
            rules.add(new AllowRule(path, allow.getAttribute("if"), allow.getAttribute("error")));
        }
        for (Element require : ElementUtils.descendantsByName(element, XSLD_NAMESPACE, "require")) {
            if (!require.hasAttribute("error"))
                throw new XsldException("Require rule in " + element.getAttribute("name") + " doesn't have error specified");
            rules.add(new RequireRule(path, require.getAttribute("if"), require.getAttribute("error")));
        }
        for (Element anAssert : ElementUtils.descendantsByName(element, XSLD_NAMESPACE, "assert")) {
            String condition = element.getAttribute("if");
            if (!anAssert.hasAttribute("error"))
                throw new XsldException("Require rule in " + element.getAttribute("name") + " doesn't have error specified");
            if (anAssert.hasAttribute("type")) {
                rules.add(new TypeAssertRule(path, condition, anAssert.getAttribute("type"), anAssert.getAttribute("error")));
            } else if (anAssert.hasAttribute("value")) {
                rules.add(new AssertRule(path, condition, "this = " + anAssert.getAttribute("value"), anAssert.getAttribute("error")));
            } else {
                if (!anAssert.hasAttribute("test"))
                    throw new XsldException("Unknown assert in " + element.getAttribute("name"));
                rules.add(new AssertRule(path, condition, anAssert.getAttribute("test"), anAssert.getAttribute("error")));
            }
        }

    }

    private void addLabel(UnresolvedPath path, String label) {
        pathLabels.put(path, label);
    }

}
