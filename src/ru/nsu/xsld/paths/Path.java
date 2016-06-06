package ru.nsu.xsld.paths;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by Илья on 06.06.2016.
 */
public class Path{
    protected List<Part> parts;

    private Path(List<Part> parts) {
        this.parts = parts;
    }

    public Path(Part... parts) {
        this(Arrays.asList(parts));
    }

    public Path append(Part part) {
        List<Part> list = new ArrayList<>();
        list.addAll(parts);
        list.add(part);
        return new Path(list);
    }

    public Path append(String element, int order) {
        return append(new Part(element, order));
    }

    public Path append(String element) {
        return append(element, 1);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Iterator<Part> iterator = parts.iterator();
        while (iterator.hasNext()) {
            Part part = iterator.next();
            part.addToBuilder(builder);
            if (iterator.hasNext()) {
                builder.append('/');
            }
        }
        return builder.toString();
    }

    public static class Part {
        private final String element;
        private final int order;

        public Part(String element, int order) {
            this.element = element;
            this.order = order;
        }

        public Part(String element) {
            this(element, 1);
        }

        void addToBuilder(StringBuilder builder) {
            builder.append(element).append('[').append(order).append(']');
        }
    }
}
