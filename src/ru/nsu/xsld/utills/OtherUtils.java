package ru.nsu.xsld.utills;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Илья on 08.06.2016.
 */
public abstract class OtherUtils {
    private OtherUtils(){}

    public static <T,Q> Map<Q, Set<T>> reverseMap(Map<T, Q> source){
        Map<Q, Set<T>> result = new HashMap<>();
        for (Map.Entry<T,Q> entry : source.entrySet()) {
            T key = entry.getKey();
            Q value = entry.getValue();
            if (!result.containsKey(value)){
                result.put(value, new HashSet<T>());
            }
            result.get(value).add(key);
        }
        return result;
    }
}
