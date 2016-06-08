package ru.nsu.xsld.utils;

import java.util.*;

/**
 * Created by Илья on 08.06.2016.
 */
public abstract class ImmutableLinkedList<T, Q extends ImmutableLinkedList<T, Q>> implements Iterable<T> {
    private final T last;
    private final Q parent;
    private final int length;

    protected ImmutableLinkedList(Q parent, T last) {
        this.last = last;
        this.parent = parent;
        this.length = parent.length() + 1;
    }

    protected ImmutableLinkedList(T last) {
        this.last = last;
        this.parent = null;
        this.length = 1;
    }


    public T get(int index) {
        if (index >= length) throw new IndexOutOfBoundsException();
        if (index == length - 1) return last;
        return parent.get(index);
    }

    public int length() {
        return length;
    }

    public T last() {
        return last;
    }

    public Optional<Q> parent() {
        return Optional.ofNullable(parent);
    }

    protected abstract Q produce(T last);

    public abstract Q append(T last);

    public Q reverse() {
        Q result = produce(last);
        Optional<Q> source = parent();
        while (source.isPresent()) {
            result = result.append(source.get().last());
            source = source.flatMap(ImmutableLinkedList::parent);
        }
        return result;
    }

    public List<T> toList() {
        ImmutableLinkedList<T, Q> path = this;
        List<T> result = new ArrayList<>();
        result.add(last);
        while (path.parent != null) {
            path = path.parent;
            result.add(path.last);
        }
        Collections.reverse(result);
        return result;
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
            return reversedList != null;
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
