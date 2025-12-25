package edu.jjxy.researchmanagementsystem.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.jjxy.researchmanagementsystem.common.Result;
import edu.jjxy.researchmanagementsystem.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final TokenStore tokenStore;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AuthInterceptor(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        if (!path.startsWith("/api/")) {
            return true;
        }
        if ("/api/auth/login".equals(path)) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token == null || token.trim().isEmpty()) {
            writeError(response, 401, "未登录或登录已过期");
            return false;
        }

        User user = tokenStore.get(token);
        if (user == null) {
            writeError(response, 401, "未登录或登录已过期");
            return false;
        }

        request.setAttribute("currentUser", user);

        String role = user.getRole();
        if (role == null) {
            writeError(response, 403, "无权限执行该操作");
            return false;
        }
        if ("ADMIN".equalsIgnoreCase(role)) {
            return true;
        }

        String method = request.getMethod();
        if ("OPTIONS".equalsIgnoreCase(method)) {
            return true;
        }

        if ("USER".equalsIgnoreCase(role)) {
            if ("GET".equalsIgnoreCase(method)) {
                if (path.startsWith("/api/user") && !"/api/user/password".equals(path)) {
                    writeError(response, 403, "无权限访问该资源");
                    return false;
                }
                return true;
            }
            if ("PUT".equalsIgnoreCase(method) && "/api/user/password".equals(path)) {
                return true;
            }
            if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method) || "DELETE".equalsIgnoreCase(method)) {
                if (path.startsWith("/api/paper")
                        || path.startsWith("/api/book")
                        || path.startsWith("/api/project")
                        || path.startsWith("/api/project-member")
                        || path.startsWith("/api/project-paper")
                        || path.startsWith("/api/project-book")) {
                    return true;
                }
            }
            writeError(response, 403, "无权限执行该操作");
            return false;
        }

        writeError(response, 403, "无权限执行该操作");
        return false;
    }

    private void writeError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        Result<Object> r = new Result<>();
        r.setCode(status);
        r.setMsg(message);
        r.setData(null);
        response.getWriter().write(objectMapper.writeValueAsString(r));
    }
}
