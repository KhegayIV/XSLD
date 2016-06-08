package ru.nsu.xsld.utills;

import ru.nsu.xsld.parsing.Path;
import ru.nsu.xsld.parsing.UnresolvedPath;

import java.util.*;

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

    public static Path resolvePath(UnresolvedPath source, Path context){
        List<Path.Part> result = new ArrayList<>();
        for (int i = 0; i < source.length(); i++) {
            String name = source.get(i);
            if (i < context.length() && context.get(i).name.equals(name)){
                result.add(context.get(i));
            } else {
                result.add(new Path.Part(name));
            }
        }
        return new Path(result);
    }
}
