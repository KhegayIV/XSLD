package ru.nsu.xsld.parsing;

import org.w3c.dom.Element;
import ru.nsu.xsld.utils.ElementUtils;
import ru.nsu.xsld.utils.OtherUtils;
import ru.nsu.xsld.utils.StreamUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static ru.nsu.xsld.utils.ElementUtils.ATTR_PREFIX;

/**
 * Created by Илья on 06.06.2016.
 */
public class ElementResolver {
    private final Element root;
    private final LabelMap labelMap;

    public ElementResolver(Element root, LabelMap labelMap) {
        this.root = root;
        this.labelMap = labelMap;
    }

    public Optional<String> getValue(String label, Path context) {
        return labelMap.getPathByLabel(label).stream().sorted(new PathComparator(context))
                .map(it -> OtherUtils.resolvePath(it, context))
                .map(it -> ElementUtils.getValueByPath(root, labelMap.getTargetNamespace(), it))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    public Stream<Path> resolvePath(UnresolvedPath path){
        Stream<OtherUtils.Pair<Element, Path>> initial =
                Stream.of(new OtherUtils.Pair<>(root, new Path().append(path.get(0), 0)));
            return StreamUtils.foldLeft(StreamUtils.fromIterable(path).skip(1), initial,
                    ((stream, last) -> {
                        if (last.startsWith(ATTR_PREFIX)) {
                            return stream.map(it -> new OtherUtils.Pair<>(it.first, it.second.append(last, 0)));
                        } else {
                            return stream.flatMap(pair -> {
                                        List<Element> children = ElementUtils
                                                .childrenStreamByName(pair.first, labelMap.getTargetNamespace(), last)
                                                .collect(Collectors.toList());
                                        return IntStream.range(0, children.size()).mapToObj(index ->
                                                new OtherUtils.Pair<>(children.get(index), pair.second.append(last, index)));
                                    }
                            );
                        }
                    })

                    )
                    .map( pair -> pair.second);
    }

    private static class PathComparator implements Comparator<UnresolvedPath> {
        private UnresolvedPath context;

        PathComparator(Path context) {
            this.context = context.unresolve();
        }

        @Override
        public int compare(UnresolvedPath first, UnresolvedPath second) {
            int dist = context.distance(first) - context.distance(second);
            if (dist != 0) return dist;
            int commonFirst = context.common(first).size();
            int commonSecond = context.common(second).size();
            //if common size equals context size -
            if (commonFirst != context.length() && commonSecond != context.length()) {
                return commonSecond - commonFirst; //Path with bigger common part is closer
            }
            if (commonFirst != context.length()) {
                return -1; //Parent is closer than child
            }
            if (commonSecond != context.length()) {
                return 1; //Parent is closer than child
            }
            return 0; // Children on same depth are equal
        }
    }
}
