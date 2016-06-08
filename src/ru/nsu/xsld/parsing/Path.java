package ru.nsu.xsld.parsing;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Илья on 06.06.2016.
 */
public class Path implements Iterable<Path.Part>{
    protected List<Part> parts;

    public Path(List<Part> parts) {
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

    public UnresolvedPath unresolve(){
        List<String> stringParts = parts.stream().map(it -> it.name).collect(Collectors.toList()); // Because of IDEA bug
        return new UnresolvedPath(stringParts);
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

    @Override
    public Iterator<Part> iterator() {
       return parts.iterator();
    }

    public static class Part {
        public final String name;
        public final int order;

        public Part(String name, int order) {
            this.name = name;
            this.order = order;
        }

        public Part(String name) {
            this(name, 0);
        }

        void addToBuilder(StringBuilder builder) {
            builder.append(name).append('[').append(order).append(']');
        }
    }

    public Part get(int index){
        return parts.get(index);
    }

    public int length(){
        return parts.size();
    }
}
