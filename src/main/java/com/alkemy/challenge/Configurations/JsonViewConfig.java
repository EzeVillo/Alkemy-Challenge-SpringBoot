package com.alkemy.challenge.Configurations;

import java.util.Collection;

import com.alkemy.challenge.Entities.UserRole;
import com.alkemy.challenge.Views.View;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

@RestControllerAdvice
class SecurityJsonViewControllerAdvice extends AbstractMappingJacksonResponseBodyAdvice {

    @ControllerAdvice
    public class JsonViewConfiguration extends AbstractMappingJacksonResponseBodyAdvice {

        @Override
        public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
            return super.supports(returnType, converterType);
        }

        @Override
        protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType,
                MethodParameter returnType, ServerHttpRequest request, ServerHttpResponse response) {
            Class<?> viewClass = View.UserView.class;
            if (SecurityContextHolder.getContext().getAuthentication() != null
                    && SecurityContextHolder.getContext().getAuthentication().getAuthorities() != null) {
                Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext()
                        .getAuthentication().getAuthorities();
                if (authorities.stream().anyMatch(o -> o.getAuthority().equals("ROLE_" + UserRole.ADMIN.name()))) {
                    viewClass = View.AdminView.class;
                }
            }
            bodyContainer.setSerializationView(viewClass);
        }
    }

    @Override
    protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType,
            MethodParameter returnType, ServerHttpRequest request, ServerHttpResponse response) {
        // TODO Auto-generated method stub

    }

}
