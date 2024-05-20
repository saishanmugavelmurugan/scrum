package com.sis.scrum.util;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class PagenationUtil {
    /**
     * Filter the given list with pagenumber and page size.
     *
     * @param sourceList - Any list
     * @param pageNumber - page number
     * @param pageSize   - page size
     * @param <T>
     * @return List<T>
     */
    public <T> List<T> getPage(List<T> sourceList, int pageNumber, int pageSize) {
        int fromIndex = (pageNumber - 1) * pageSize;
        if (sourceList == null || sourceList.size() <= fromIndex) {
            return Collections.emptyList();
        }
        return sourceList.subList(fromIndex, Math.min(fromIndex + pageSize, sourceList.size()));
    }
}
