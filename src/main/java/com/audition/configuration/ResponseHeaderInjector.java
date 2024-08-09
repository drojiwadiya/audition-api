package com.audition.configuration;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ResponseHeaderInjector implements Filter {

    @Autowired
    private Tracer tracer;

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response,
        final FilterChain chain) throws IOException, ServletException {
        final Span currentSpan = tracer.currentSpan();
        if (currentSpan != null) {
            final HttpServletResponse res = (HttpServletResponse) response;
            res.setHeader("X-B3-TraceId", currentSpan.context().traceId());
            res.setHeader("X-B3-SpanId", currentSpan.context().spanId());
        }
        chain.doFilter(request, response);
    }
}
