package ru.nsu.xsld.parsing;

import ru.nsu.xsld.utils.ImmutableLinkedList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Илья on 06.06.2016.
 */
public class Path extends ImmutableLinkedList<Path.Part, Path> {


    public Path(Path parent, Part last) {
        super(parent, last);
    }

    public Path(Part last) {
        super(last);
    }

    public static Path of(Part... parts) {
        return of(Arrays.asList(parts));
    }

    public static Path of(List<Part> parts) {
        Path result = new Path(parts.get(0));
        for (int i = 1; i < parts.size(); i++) {
            result = result.append(parts.get(i));
        }
        return result;
    }

    @Override
    protected Path produce(Part last) {
        return new Path(last);
    }

    @Override
    public Path append(Part last) {
        return new Path(this, last);
    }

    public UnresolvedPath unresolve() {
        List<String> result = toList().stream().map(it -> it.name).collect(Collectors.toList());
        return UnresolvedPath.of(result);
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

}