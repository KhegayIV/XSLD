package ru.nsu.xsld.parsing;

import ru.nsu.xsld.utils.ImmutableLinkedList;
import ru.nsu.xsld.utils.StreamUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Илья on 06.06.2016.
 */
public class Path extends ImmutableLinkedList<Path.Part, Path> {
    private static final Path empty = new Path();


    public Path() {
    }

    protected Path(Path parent, Part last) {
        super(parent, last);
    }

    public static Path of(Part... parts) {
        return of(Arrays.asList(parts));
    }

    public static Path of(List<Part> parts) {
        return StreamUtils.foldLeft(parts.stream(), new Path(), Path::append);
    }

    @Override
    protected Path empty() {
        return empty;
    }

    @Override
    public Path append(Part last) {
        return new Path(this, last);
    }

    public Path append(String name, int order) {
        return append(new Part(name, order));
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

        @Override
        public String toString() {
            return name+"["+order+"]";
        }
    }

}