package com.alkemy.challenge.DTOs.Pages;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageDTO<T> {
    private List<T> content = new ArrayList<>();
    private int pageSize;
    private int pageNumber;
    private int totalPages;
    private long totalElements;
    private Boolean isFirst;
    private Boolean isLast;
    private Boolean isEmpty;
    private Boolean hasNext;
    private Boolean hasPrevious;
}
