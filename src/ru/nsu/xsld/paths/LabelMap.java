package ru.nsu.xsld.paths;

import ru.nsu.xsld.utills.OtherUtils;

import java.util.*;

/**
 * Created by Илья on 06.06.2016.
 */
public class LabelMap {
    private Map<String, Set<UnresolvedPath>> labelPaths;
    private Map<UnresolvedPath, String> pathLabels;

    public LabelMap(Map<UnresolvedPath, String> pathLabels) {
        this.pathLabels = pathLabels;
        this.labelPaths = OtherUtils.reverseMap(pathLabels);
    }

    public Optional<String> getLabelByPath(UnresolvedPath path){
        return Optional.ofNullable(pathLabels.get(path));
    }
    public Collection<UnresolvedPath> getPathByLabel(String label){
        return Optional.ofNullable(labelPaths.get(label)).orElse(Collections.emptySet());
    }
}
