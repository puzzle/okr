package ch.puzzle.okr.util;

import java.util.List;
import java.util.stream.StreamSupport;

public class CollectionUtils {

    private CollectionUtils() {
    }

    public static <T> List<T> iterableToList(Iterable<T> iterable) {
        return StreamSupport //
                .stream(iterable.spliterator(), false) //
                .toList();
    }
}
