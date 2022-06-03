package com.alkemy.challenge.Utils.Interfaces;

import com.alkemy.challenge.DTOs.Pages.PageDTO;

import org.springframework.web.util.UriComponentsBuilder;

public interface PaginationLinks {
    public String createHeaderLink(PageDTO<?> page, UriComponentsBuilder uriBuilder);
}
