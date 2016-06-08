package ru.nsu.xsld.utils;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.nsu.xsld.parsing.Path;
import ru.nsu.xsld.parsing.UnresolvedPath;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by Илья on 08.06.2016.
 */
public abstract class ElementUtils {
    public static final String XSD_NAMESPACE = "http://www.w3.org/2001/XMLSchema";
    public static final String XSLD_NAMESPACE = "xsld.nsu.ru";
    public static final String ATTR_PREFIX = "@";

    private ElementUtils() {
    }

    public static NodeListIterator<Element> descendantsByName(Element element, String namespaceURI, String localName) {
        return new NodeListIterator<>(element.getElementsByTagNameNS(namespaceURI, localName));
    }

    public static Stream<Element> childrenStreamByName(Element element, String namespaceURI, String localName) {
        return descendantsByName(element, namespaceURI, localName).stream().filter(it -> it.getParentNode() == element);
    }

    public static Stream<Element> childrenStreamByPath(Element parent, String namespaceURI, String... elementNames) {
        return StreamUtils.foldLeft(Stream.of(elementNames), Stream.of(parent),
                (stream, name) ->
                        stream.flatMap(it -> childrenStreamByName(it, namespaceURI, name)));
    }

    public static Stream<Element> childrenOneOf(Element parent, String namespaceURI, String... alternatives) {
        return StreamUtils.foldLeft(
                Stream.of(alternatives).map(name -> childrenStreamByPath(parent, namespaceURI, name)),
                Stream.<Element>of(),
                Stream::concat);
    }

    public static Optional<Element> orderedChild(Element root, String namespaceURI, String name, int order) {
        return childrenStreamByName(root, namespaceURI, name).skip(order).findFirst();
    }

    public static Optional<Element> getElementByPath(Element root, String namespaceURI, Path path) {
        return StreamUtils.foldLeft(StreamUtils.fromIterable(path).skip(1), Optional.of(root),
                (parent, part) -> parent.flatMap(it -> orderedChild(it, namespaceURI, part.name, part.order)));
    }

    public static Optional<String> getValueByPath(Element root, String namespaceURI, Path path) {
        Path.Part last = path.last();
        if (!last.name.startsWith(ATTR_PREFIX)) {
            return getElementByPath(root, namespaceURI, path).map(Node::getTextContent);
        } else {
            return Optional.ofNullable(getElementByPath(root, namespaceURI, path.parent())
                    .orElse(root).getAttribute(last.name.substring(1)));
        }
    }

    public static boolean isAttribute(UnresolvedPath path){
        return path.last().startsWith(ATTR_PREFIX);
    }

    public static boolean isAttribute(Path path){
        return path.last().name.startsWith(ATTR_PREFIX);
    }

    public static class NodeListIterator<T extends Node> implements Iterator<T>, Iterable<T> {
        private NodeList list;
        private int position = 0;
        private int max;

        public NodeListIterator(NodeList list) {
            this.list = list;
            max = list.getLength();
        }

        @Override
        public boolean hasNext() {
            return position < max;
        }

        @Override
        public T next() {
            return (T) list.item(position++);
        }

        @Override
        public Iterator<T> iterator() {
            return this;
        }

        public Stream<T> stream() {
            return StreamUtils.fromIterable(this);
        }
    }
}
