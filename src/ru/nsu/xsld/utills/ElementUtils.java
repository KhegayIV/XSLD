package ru.nsu.xsld.utills;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by Илья on 08.06.2016.
 */
public abstract class ElementUtils {
    public static final String XSD_NAMESPACE = "http://www.w3.org/2001/XMLSchema";
    public static final String XSLD_NAMESPACE = "xsld.nsu.ru";

    private ElementUtils(){}

    public static NodeListIterator<Element> descendantsByName(Element element, String namespaceURI, String localName) {
        return new NodeListIterator<>(element.getElementsByTagNameNS(namespaceURI, localName));
    }

    public static Stream<Element> childrenStreamByName(Element element, String namespaceURI, String localName) {
        return descendantsByName(element, namespaceURI, localName).stream().filter(it -> it.getParentNode() == element);
    }

    public static Stream<Element> childrenStreamByPath(Element parent, String namespaceURI, String... elementNames){
        return StreamUtils.foldLeft(Stream.of(elementNames), Stream.of(parent),
                (stream, name) ->
                        stream.flatMap(it -> childrenStreamByName(it, namespaceURI, name)));
    }

    public static Stream<Element> childrenOneOf(Element parent, String namespaceURI, String... alternatives){
        return StreamUtils.foldLeft(
                Stream.of(alternatives).map(name -> childrenStreamByPath(parent, namespaceURI, name)),
                Stream.<Element>of(),
                Stream::concat);
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
            return StreamSupport.stream(this.spliterator(), false);
        }
    }
}
