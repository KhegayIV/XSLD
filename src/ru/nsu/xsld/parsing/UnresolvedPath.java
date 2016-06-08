package ru.nsu.xsld.parsing;

import ru.nsu.xsld.utils.ImmutableLinkedList;
import ru.nsu.xsld.utils.StreamUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Илья on 07.06.2016.
 */
public class UnresolvedPath extends ImmutableLinkedList<String, UnresolvedPath> {
    private static final UnresolvedPath empty = new UnresolvedPath();

    public UnresolvedPath() { }

    protected UnresolvedPath(UnresolvedPath parent, String last) {
        super(parent, last);
    }


    public static UnresolvedPath of(List<String> parts) {
        return StreamUtils.foldLeft(parts.stream(), new UnresolvedPath(), UnresolvedPath::append);
    }

    public static UnresolvedPath of(String... parts) {
        return of(Arrays.asList(parts));
    }

    public List<String> common(UnresolvedPath other) {
        Iterator<String> thisIterator = iterator();
        Iterator<String> otherIterator = other.iterator();
        List<String> result = new ArrayList<>();
        while (thisIterator.hasNext() && otherIterator.hasNext()) {
            String value = thisIterator.next();
            if (value.equals(otherIterator.next())) {
                result.add(value);
            } else {
                break;
            }
        }

        return result;
    }

    public int distance(UnresolvedPath other) {
        List<String> common = this.common(other);
        return length() + other.length() - 2 * common.size(); //Distance between nodes

    }

    @Override
    protected UnresolvedPath empty() {
        return empty;
    }

    @Override
    public UnresolvedPath append(String last) {
        return new UnresolvedPath(this, last);
    }
}
