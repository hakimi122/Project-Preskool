
package controllers.headmaster;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.User;


@WebFilter(filterName = "HeadMasterFilter", urlPatterns = {"/schedule-manage", "/exam", "/teacher",
    "/student", "/class", "/assessment?action=close","/subject"})
public class HeadMasterFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if ("headmaster".equals(user.getRole())) {
            chain.doFilter(request, response);
            return;
        }
        session.setAttribute("notification", "Rất tiếc bạn không có quyền truy cập đường dẫn này!");
        session.setAttribute("typeNoti", "alert-danger");
        String role = user.getRole();
        if ("teacher".equals(role)) {
            res.sendRedirect(req.getContextPath() + "/teacher/dashboard");
        } else if ("student".equals(role)) {
            res.sendRedirect(req.getContextPath() + "/student/dashboard");
        }
    }

    public void destroy() {
    }

    public void init(FilterConfig filterConfig) {
    }

}
