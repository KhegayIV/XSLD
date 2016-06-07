package ru.nsu.xsld.paths;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.nsu.xsld.XsldException;
import ru.nsu.xsld.rules.Rule;
import ru.nsu.xsld.rules.xsldrules.AllowRule;
import ru.nsu.xsld.rules.xsldrules.AssertRule;
import ru.nsu.xsld.rules.xsldrules.RequireRule;
import ru.nsu.xsld.rules.xsldrules.TypeAssertRule;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by Илья on 07.06.2016.
 */
public class XsldParser {
    public static final String XSD_NAMESPACE = "http://www.w3.org/2001/XMLSchema";
    public static final String XSLD_NAMESPACE = "xsld.nsu.ru";
    private Map<String, List<UnresolvedPath>> labelPaths = new HashMap<>();
    private Map<UnresolvedPath, String> pathLabels = new HashMap<>();
    private List<Rule> rules = new ArrayList<>();

    private Map<String, Element> typeElements = new HashMap<>();

    public XsldParser(Element schema) throws XsldException {
        for (Element element : descendantsByName(schema,XSD_NAMESPACE, "complexType")) {
            typeElements.put(element.getAttribute("name"), element);
        }


        Element root = getRoot(schema);
        if (root == null) throw new XsldException("No root found");
        lookupElement(root, new UnresolvedPath());
    }

    /**
     * Recursively searches for elements and attributes inside
     * @param parentPath UnresolvedPath to this node, not inclusive
     */
    private void lookupElement(Element element, UnresolvedPath parentPath) throws XsldException {
        UnresolvedPath path = parentPath.append(element.getAttribute("name"));
        inspect(element, parentPath);
        Element type = typeElements.get(element.getAttribute("type"));
        if (type!=null){
            for (Element attribute : descendantsByName(type, XSD_NAMESPACE, "attribute")) {
                inspect(attribute, path);
            }
            for (Element child : descendantsByName(type, XSD_NAMESPACE, "element")) {
                lookupElement(child, path);
            }
        }
    }

    /**
     * Inspects xs:element or xs:attribute for labels and rules
     * @param parentPath UnresolvedPath to this node, not inclusive
     */
    private void inspect(Element element, UnresolvedPath parentPath) throws XsldException {
        UnresolvedPath path = parentPath.append(element.getAttribute("name"));
        Attr labelNode = element.getAttributeNodeNS(XSLD_NAMESPACE, "label");
        if (labelNode != null){
            addLabel(path, labelNode.getValue());
        }

        for (Element allow : descendantsByName(element, XSLD_NAMESPACE, "allow")){
            if (!allow.hasAttribute("error"))
                throw new XsldException("Require rule in "+element.getAttribute("name")+" doesn't have error specified");
            rules.add(new AllowRule(path, allow.getAttribute("if"), allow.getAttribute("error")));
        }
        for (Element require : descendantsByName(element, XSLD_NAMESPACE, "require")){
            if (!require.hasAttribute("error"))
                throw new XsldException("Require rule in "+element.getAttribute("name")+" doesn't have error specified");
            rules.add(new RequireRule(path, require.getAttribute("if"), require.getAttribute("error")));
        }
        for (Element anAssert : descendantsByName(element, XSLD_NAMESPACE, "assert")) {
            String condition = element.getAttribute("if");
            if (!anAssert.hasAttribute("error"))
                throw new XsldException("Require rule in "+element.getAttribute("name")+" doesn't have error specified");
            if (anAssert.hasAttribute("type")){
                rules.add(new TypeAssertRule(path, condition, anAssert.getAttribute("type"), anAssert.getAttribute("error")));
            } else if (anAssert.hasAttribute("value")){
                rules.add(new AssertRule(path, condition, "this = "+anAssert.getAttribute("value"), anAssert.getAttribute("error")));
            } else {
                if (!anAssert.hasAttribute("test"))
                    throw new XsldException("Unknown assert in "+element.getAttribute("name"));
                rules.add(new AssertRule(path, condition, anAssert.getAttribute("test"), anAssert.getAttribute("error")));
            }
        }

    }

    private void addLabel(UnresolvedPath path, String label){
        pathLabels.put(path, label);
        labelPaths.putIfAbsent(label, new ArrayList<>());
        labelPaths.get(label).add(path);
    }

    private static Element getRoot(Element schema){
        return descendantsByName(schema, XSD_NAMESPACE, "element").stream()
                .filter(it -> it.getParentNode() == schema)
                .findFirst().orElse(null);
    }

    private static NodeListIterator<Element> descendantsByName(Element element, String namespaceURI, String localName){
        return new NodeListIterator<>(element.getElementsByTagNameNS(namespaceURI, localName));
    }

    private static class NodeListIterator<T extends Node> implements Iterator<T>, Iterable<T>{
        private NodeList list;
        private int position = 0;
        private int max;

        public NodeListIterator(NodeList list) {
            this.list = list;
            max = list.getLength();
        }

        @Override
        public boolean hasNext() {
            return position<max;
        }

        @Override
        public T next() {
            return (T) list.item(position++);
        }

        @Override
        public Iterator<T> iterator() {
            return this;
        }

        public Stream<T> stream(){
            return StreamSupport.stream(this.spliterator(), false);
        }
    }
}
