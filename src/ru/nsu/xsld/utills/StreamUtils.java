package ru.nsu.xsld.utills;

import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * Created by Илья on 08.06.2016.
 */
public abstract class StreamUtils {
    private StreamUtils(){}


    @SafeVarargs
    public static <T> Stream<T> concat(Stream<T>... streams){
        return Stream.of(streams).reduce(Stream::concat).orElse(null);
    }

    public static <Q, T> Q foldLeft(Stream<T> stream, Q initial, BiFunction<Q,T,Q> accumulator){
        Q result = initial;
        for (T object : (Iterable<T>)stream::iterator){
            result = accumulator.apply(result, object);
        }
        return result;
    }
}
