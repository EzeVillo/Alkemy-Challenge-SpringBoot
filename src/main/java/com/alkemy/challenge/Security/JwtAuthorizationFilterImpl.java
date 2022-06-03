package com.alkemy.challenge.Security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alkemy.challenge.Entities.User;
import com.alkemy.challenge.Error.Exceptions.Auth.Users.UserNotFoundException;
import com.alkemy.challenge.Repositories.UserRepository;
import com.alkemy.challenge.Security.Interfaces.JwtAuthorizationFilter;
import com.alkemy.challenge.Security.Interfaces.JwtTokenProvider;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilterImpl extends OncePerRequestFilter implements JwtAuthorizationFilter {

	private final JwtTokenProvider tokenProvider;
	private final UserRepository userRepository;

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try {
			String token = getJwtFromRequest(request);

			if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
				Long userId = tokenProvider.getUserIdFromJWT(token);

				User user = (User) loadUserById(userId);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,
						user.getRoles(), user.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);

			}
		} catch (Exception ex) {
			throw new JwtException("Failed to establish user authentication in security context");
		}

		filterChain.doFilter(request, response);

	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader(JwtTokenProviderImpl.TOKEN_HEADER);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtTokenProviderImpl.TOKEN_PREFIX)) {
			return bearerToken.substring(JwtTokenProviderImpl.TOKEN_PREFIX.length(), bearerToken.length());
		}
		return null;
	}

	public UserDetails loadUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException());
    }

}
