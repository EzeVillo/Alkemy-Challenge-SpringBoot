package com.alkemy.challenge.DTOs.Pages.Converters;

import com.alkemy.challenge.DTOs.Pages.PageDTO;

import org.springframework.data.domain.Page;

public interface PageConverter<T> {
    public PageDTO<T> toPageDTO(Page<T> content);
}
