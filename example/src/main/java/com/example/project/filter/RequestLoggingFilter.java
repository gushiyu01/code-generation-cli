package com.example.project.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

/**
 * 请求日志过滤器 - 记录所有API调用
 *
 * @author CodeGenerator
 */
@Component
@Order(1)
public class RequestLoggingFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);
    private static final String REQUEST_ID_HEADER = "X-Request-Id";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 生成或获取请求ID
        String requestId = httpRequest.getHeader(REQUEST_ID_HEADER);
        if (requestId == null || requestId.isEmpty()) {
            requestId = UUID.randomUUID().toString().replace("-", "");
        }

        // 记录请求开始时间
        long startTime = System.currentTimeMillis();

        // 记录请求信息
        logRequest(httpRequest, requestId);

        // 设置响应请求ID
        httpResponse.setHeader(REQUEST_ID_HEADER, requestId);

        // 添加请求ID到请求属性
        httpRequest.setAttribute("requestId", requestId);

        try {
            // 继续执行后续过滤器
            chain.doFilter(request, response);
        } finally {
            // 记录响应信息
            long duration = System.currentTimeMillis() - startTime;
            logResponse(httpRequest, httpResponse, requestId, duration);
        }
    }

    /**
     * 记录请求信息
     */
    private void logRequest(HttpServletRequest request, String requestId) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("╔══════════════════════════════════════════════════════════════════\n");
        sb.append("║ REQUEST                                                          ║\n");
        sb.append("╠══════════════════════════════════════════════════════════════════\n");
        sb.append(String.format("║ Request ID: %s\n", requestId));
        sb.append(String.format("║ Method:      %s %s\n", request.getMethod(), request.getRequestURI()));
        sb.append(String.format("║ Remote IP:   %s\n", getClientIp(request)));
        sb.append(String.format("║ Content-Type:%s\n", request.getContentType()));
        sb.append(String.format("║ User-Agent: %s\n", request.getHeader("User-Agent")));

        // 记录请求参数
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            sb.append(String.format("║ Query:       %s\n", request.getQueryString()));
        }

        // 记录Headers
        log.debug("Request Headers: {}", getHeadersInfo(request));

        sb.append("╚══════════════════════════════════════════════════════════════════");

        log.info(sb.toString());
    }

    /**
     * 记录响应信息
     */
    private void logResponse(HttpServletRequest request, HttpServletResponse response,
                             String requestId, long duration) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("╔══════════════════════════════════════════════════════════════════\n");
        sb.append("║ RESPONSE                                                         ║\n");
        sb.append("╠══════════════════════════════════════════════════════════════════\n");
        sb.append(String.format("║ Request ID: %s\n", requestId));
        sb.append(String.format("║ Method:     %s %s\n", request.getMethod(), request.getRequestURI()));
        sb.append(String.format("║ Status:     %d %s\n", response.getStatus(),
                getStatusText(response.getStatus())));
        sb.append(String.format("║ Duration:   %d ms\n", duration));
        sb.append("╚══════════════════════════════════════════════════════════════════");

        // 根据响应状态码选择日志级别
        if (response.getStatus() >= 500) {
            log.error(sb.toString());
        } else if (response.getStatus() >= 400) {
            log.warn(sb.toString());
        } else {
            log.info(sb.toString());
        }
    }

    /**
     * 获取客户端真实IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多个代理的情况，取第一个IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * 获取请求头信息
     */
    private String getHeadersInfo(HttpServletRequest request) {
        StringBuilder headers = new StringBuilder();
        java.util.Collections.list(request.getHeaderNames()).forEach(name -> {
            // 敏感头信息不记录
            if (!"Authorization".equalsIgnoreCase(name) &&
                    !"Cookie".equalsIgnoreCase(name) &&
                    !"Set-Cookie".equalsIgnoreCase(name)) {
                headers.append(name).append("=").append(request.getHeader(name)).append(", ");
            }
        });
        return headers.length() > 0 ? headers.toString() : "N/A";
    }

    /**
     * 获取状态文本
     */
    private String getStatusText(int status) {
        return switch (status) {
            case 200 -> "OK";
            case 201 -> "Created";
            case 204 -> "No Content";
            case 301 -> "Moved Permanently";
            case 302 -> "Found";
            case 304 -> "Not Modified";
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 405 -> "Method Not Allowed";
            case 409 -> "Conflict";
            case 415 -> "Unsupported Media Type";
            case 422 -> "Unprocessable Entity";
            case 429 -> "Too Many Requests";
            case 500 -> "Internal Server Error";
            case 501 -> "Not Implemented";
            case 502 -> "Bad Gateway";
            case 503 -> "Service Unavailable";
            case 504 -> "Gateway Timeout";
            default -> "Unknown";
        };
    }
}
