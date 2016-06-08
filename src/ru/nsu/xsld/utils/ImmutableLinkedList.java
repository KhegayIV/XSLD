package ru.nsu.xsld.utils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Илья on 08.06.2016.
 */
public abstract class ImmutableLinkedList<T, Q extends ImmutableLinkedList<T, Q>> implements Iterable<T> {
    private final T last;
    private final Q parent;
    private final int length;

    protected ImmutableLinkedList(){
        last = null;
        parent = null;
        length = 0;
    }

    protected ImmutableLinkedList(Q parent, T last) {
        this.last = last;
        this.parent = parent;
        this.length = parent.length() + 1;
    }


    public T get(int index) {
        if (index >= length) throw new IndexOutOfBoundsException();
        if (index == length - 1) return last();
        return parent.get(index);
    }

    public int length() {
        return length;
    }

    public boolean isEmpty(){
        return length == 0;
    }

    public T last() {
        if (last == null) throw new IndexOutOfBoundsException("Get on empty list");
        return last;
    }

    public Q parent() {
        if (parent == null) return empty();
        return parent;
    }

    protected abstract Q empty();

    public abstract Q append(T last);

    public Q reverse() {
        if (this.isEmpty()) return empty();
        Q result = empty().append(last());
        Q source = parent();
        while (!source.isEmpty()){
            result = result.append(source.last());
            source = source.parent();
        }
        return result;
    }

    public List<T> toList() {
        return StreamUtils.fromIterable(this).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("/");
        for (T t : this) {
            joiner.add(t.toString());
        }
        return joiner.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new ImmListIterator<>(this);
    }

    private static class ImmListIterator<T, Q extends ImmutableLinkedList<T, Q>> implements Iterator<T> {
        private ImmutableLinkedList<T, Q> reversedList;


        ImmListIterator(ImmutableLinkedList<T, Q> source) {
            reversedList = source.reverse();
        }

        @Override
        public boolean hasNext() {
            return !reversedList.isEmpty();
        }

        @Override
        public T next() {
            try {
                return reversedList.last;
            } finally {
                reversedList = reversedList.parent;
            }
        }
    }

}
