package com.alkemy.challenge.DTOs.Pages.Converters;

import com.alkemy.challenge.DTOs.Pages.PageDTO;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PageConverterImpl<T> implements PageConverter<T> {

    public PageDTO<T> toPageDTO(Page<T> content) {
        PageDTO<T> page = new PageDTO<>();
        page.setContent(content.getContent());
        page.setIsEmpty(content.isEmpty());
        page.setIsFirst(content.isFirst());
        page.setPageSize(content.getSize());
        page.setIsLast(content.isLast());
        page.setPageNumber(content.getNumber());
        page.setTotalElements(content.getTotalElements());
        page.setTotalPages(content.getTotalPages());
        page.setHasNext(content.hasNext());
        page.setHasPrevious(content.hasPrevious());
        return page;
    }
}
