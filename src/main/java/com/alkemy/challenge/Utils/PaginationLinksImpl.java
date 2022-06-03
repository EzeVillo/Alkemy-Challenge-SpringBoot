package com.alkemy.challenge.Utils;

import com.alkemy.challenge.DTOs.Pages.PageDTO;
import com.alkemy.challenge.Utils.Interfaces.PaginationLinks;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class PaginationLinksImpl implements PaginationLinks {

	public String createHeaderLink(PageDTO<?> page, UriComponentsBuilder uriBuilder) {
		final StringBuilder headerLink = new StringBuilder();
		headerLink.append("");

		if (page.getHasNext()) {
			String uri = constructUri(page.getPageNumber() + 1, page.getPageSize(), uriBuilder);
			headerLink.append(buildHeaderLink(uri, "next"));
		}

		if (page.getHasPrevious()) {
			String uri = constructUri(page.getPageNumber() - 1, page.getPageSize(), uriBuilder);
			appendCommaIfNecessary(headerLink);
			headerLink.append(buildHeaderLink(uri, "prev"));
		}

		if (!page.getIsFirst()) {
			String uri = constructUri(0, page.getPageSize(), uriBuilder);
			appendCommaIfNecessary(headerLink);
			headerLink.append(buildHeaderLink(uri, "first"));
		}

		if (!page.getIsLast()) {
			String uri = constructUri(page.getTotalPages() - 1, page.getPageSize(), uriBuilder);
			appendCommaIfNecessary(headerLink);
			headerLink.append(buildHeaderLink(uri, "last"));
		}

		return headerLink.toString();
	}

	private String constructUri(int pageNumber, int size, UriComponentsBuilder uriBuilder) {
		return uriBuilder.replaceQueryParam("page", pageNumber).replaceQueryParam("size", size).build().encode()
				.toUriString();
	}

	private String buildHeaderLink(final String uri, final String rel) {
		return "<" + uri + ">; rel=\"" + rel + "\"";
	}

	private void appendCommaIfNecessary(final StringBuilder headerLink) {
		if (headerLink.length() > 0) {
			headerLink.append(", ");
		}
	}

}
