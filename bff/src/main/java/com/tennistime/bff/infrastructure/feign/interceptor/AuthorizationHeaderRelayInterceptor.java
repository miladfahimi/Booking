package com.tennistime.bff.infrastructure.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Feign interceptor that forwards the Authorization header from the incoming HTTP request.
 */
@Component
public class AuthorizationHeaderRelayInterceptor implements RequestInterceptor {

    /**
     * Propagates the Authorization header from the current servlet request to the outgoing Feign request.
     *
     * @param requestTemplate template used to construct the downstream HTTP request.
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (!(attributes instanceof ServletRequestAttributes servletAttributes)) {
            return;
        }

        HttpServletRequest request = servletAttributes.getRequest();
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(authorizationHeader)) {
            requestTemplate.header(HttpHeaders.AUTHORIZATION, authorizationHeader);
        }
    }
}

