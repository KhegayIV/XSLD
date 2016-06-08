package ru.nsu.xsld.parsing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Илья on 07.06.2016.
 */
public class UnresolvedPath implements Iterable<String>{
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

    @Override
    public Iterator<String> iterator() {
        return parts.iterator();
    }

    public List<String> common(UnresolvedPath other){
        List<String> result = new ArrayList<>();
        for (int i = 0; i < parts.size(); i++) {
            if (parts.get(i).equals(other.parts.get(i))){
                result.add(parts.get(i));
            } else {
                break;
            }
        }
        return result;
    }

    public int distance(UnresolvedPath other){
        List<String> common = this.common(other);
        return parts.size() + other.parts.size() - 2*common.size(); //Distance between nodes

    }

    public String get(int index){
        return parts.get(index);
    }

    public int length() {
        return parts.size();
    }
}
