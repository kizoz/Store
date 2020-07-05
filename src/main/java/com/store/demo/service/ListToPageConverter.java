package com.store.demo.service;

import org.springframework.beans.support.PagedListHolder;

import java.util.List;

public class ListToPageConverter {

    protected static <T> List<T> getListOfPage(List<T> list, int p, int size){        //  Gets List, returns page by number
        PagedListHolder<T> page= new PagedListHolder<>(list);
        page.setPageSize(size);
        if(p>page.getPageCount()) {
            throw new IllegalArgumentException(String.format("Page number %s out of bounds", p));
        }
        page.setPage(p);
        return page.getPageList();
    }
}
