package cn.edu.fudan.campuservice.filter;


import cn.edu.fudan.campuservice.entity.Response;
import cn.edu.fudan.campuservice.utils.JwtUtil;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final PathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if (isProtectedUrl(request)) {
                String token = request.getHeader("Authorization");
                JwtUtil.validateToken(token);
            }
        } catch (Exception e) {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(new Response<>("401", "unauthorized", e.getMessage()).toString());
            return;
        }
        filterChain.doFilter(request, response);
    }

    private boolean isProtectedUrl(HttpServletRequest request) {
        return !PATH_MATCHER.match("/login", request.getServletPath())
                && !PATH_MATCHER.match("/register", request.getServletPath());
    }

}