package ru.nsu.xsld.paths;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by Илья on 07.06.2016.
 */
public class UnresolvedPath {
    private List<String> parts;

    public UnresolvedPath(List<String> parts) {
        this.parts = parts;
    }

    public UnresolvedPath(String... parts){
        this(Arrays.asList(parts));
    }

    public UnresolvedPath append(String part){
        List<String> list = new ArrayList<>();
        list.addAll(parts);
        list.add(part);
        return new UnresolvedPath(list);
    }

    @Override
    public String toString() {
        return parts.stream().collect(Collectors.joining("/"));
    }
}
