// 📁 src/main/java/com/parcinformatique/filter/AuthenticationFilter.java
package com.parcinformatique.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        // Pages publiques (pas besoin d'être connecté)
        if (path.startsWith("/auth") ||
                path.equals("/") ||
                path.startsWith("/resources/") ||
                path.contains(".css") ||
                path.contains(".js") ||
                path.contains(".jpg") ||
                path.contains(".png")) {

            chain.doFilter(request, response);
            return;
        }

        // Vérifier la session
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            // Non connecté → redirection vers login
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/auth?action=login");
            return;
        }

        // Utilisateur connecté → continuer
        chain.doFilter(request, response);
    }
}