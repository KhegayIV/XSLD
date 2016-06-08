package ru.nsu.xsld.parsing;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import ru.nsu.xsld.utils.ElementUtils;
import ru.nsu.xsld.utils.OtherUtils;

import java.util.Comparator;
import java.util.Optional;

/**
 * Created by Илья on 06.06.2016.
 */
public class ElementResolver {
    private final Element root;
    private final String targetNamespace;
    private final LabelMap labelMap;

    ElementResolver(Element root, String targetNamespace, LabelMap labelMap) {
        this.root = root;
        this.targetNamespace = targetNamespace;
        this.labelMap = labelMap;
    }

    //TODO: Attributes
    public Optional<String> getValue(String label, Path context){
        return labelMap.getPathByLabel(label).stream().sorted(new PathComparator(context))
                .map(it -> OtherUtils.resolvePath(it, context))
                .map(it -> ElementUtils.getByPath(root, targetNamespace, it))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Node::getTextContent)
                .findFirst();
    }

    private static class PathComparator implements Comparator<UnresolvedPath>{
        private UnresolvedPath context;

        PathComparator(Path context) {
            this.context = context.unresolve();
        }

        @Override
        public int compare(UnresolvedPath first, UnresolvedPath second) {
            int dist = context.distance(first) - context.distance(second);
            if (dist != 0) return dist;
            int commonFirst = context.common(first).size();
            int commonSecond = context.common(second).size();
            //if common size equals context size -
            if (commonFirst != context.length() && commonSecond != context.length()){
                return commonSecond - commonFirst; //Path with bigger common part is closer
            }
            if (commonFirst != context.length()){
                return -1; //Parent is closer than child
            }
            if (commonSecond != context.length()){
                return 1; //Parent is closer than child
            }
            return 0; // Children on same depth are equal
        }
    }
}
