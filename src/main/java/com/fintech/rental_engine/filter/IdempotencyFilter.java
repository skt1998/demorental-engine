package com.fintech.rental_engine.filter;

import com.fintech.rental_engine.entity.IdempotencyRecord;
import com.fintech.rental_engine.service.IdempotencyService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Optional;

@Component
@Order(1)
public class IdempotencyFilter implements Filter {

    private final IdempotencyService idempotencyService;

    public IdempotencyFilter(IdempotencyService idempotencyService) {
        this.idempotencyService = idempotencyService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if ("POST".equalsIgnoreCase(httpRequest.getMethod())) {
            String idempotencyKey = httpRequest.getHeader("Idempotency-Key");

            if (idempotencyKey != null && !idempotencyKey.isBlank()) {

                boolean isNewRequest = idempotencyService.lockKey(idempotencyKey);

                if (!isNewRequest) {
                    Optional<IdempotencyRecord> existingRecord = idempotencyService.getRecord(idempotencyKey);

                    if (existingRecord.isPresent()) {
                        IdempotencyRecord record = existingRecord.get();
                        httpResponse.setStatus(record.getResponseCode());
                        httpResponse.setContentType("application/json");
                        httpResponse.getWriter().write(record.getResponseBody());
                        return;
                    } else {
                        httpResponse.setStatus(429);
                        httpResponse.setContentType("application/json");
                        httpResponse.getWriter().write("{\"error\": \"Concurrent request processing with same Idempotency-Key.\"}");
                        return;
                    }
                }

                ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(httpResponse);

                chain.doFilter(request, responseWrapper);

                byte[] responseArray = responseWrapper.getContentAsByteArray();
                String responseBody = new String(responseArray, responseWrapper.getCharacterEncoding());

                idempotencyService.saveRecord(
                        idempotencyKey,
                        httpRequest.getRequestURI(),
                        responseWrapper.getStatus(),
                        responseBody
                );

                responseWrapper.copyBodyToResponse();
                return;
            }
        }

        chain.doFilter(request, response);
    }
}