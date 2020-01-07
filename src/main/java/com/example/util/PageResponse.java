package com.example.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Page;

import java.util.List;

public class PageResponse<T> {
    @JsonProperty("records")
    private List<T> result;

    private int totalPages;
    private long totalElement;
    private int page;
    private int size;

    public PageResponse() { }

    public PageResponse(Page<T> page) {
        this.result = page.getContent();
        this.totalElement = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.page = page.getNumber() + 1;
        this.size = page.getSize();
    }

    public PageResponse(List<T> result, int totalPages, long totalElement, int page, int size) {
        this.totalPages = totalPages;
        this.totalElement = totalElement;
        this.page = page;
        this.size = size;
        this.result = result;
    }

    public static <T> PageResponse<T> create(Page<T> page) {
        return new PageResponse(page);
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElement() {
        return totalElement;
    }

    public void setTotalElement(long totalElement) {
        this.totalElement = totalElement;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }
}
