package controllers.common;

import dal.implement.UserDAOImpl;
import dal.interfaces.IUserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.InputStream;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.User;
import org.apache.tomcat.jakartaee.commons.compress.utils.IOUtils;
import org.json.JSONArray;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2 MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 50 // 50 MB
)
public class ProfileController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ProfileController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProfileController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        //processRequest(request, response);
        User user = (User) session.getAttribute("user");
        if (user != null) {
            request.getRequestDispatcher("./views/profile.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        IUserDAO ud = new UserDAOImpl();

        String updatePart = request.getParameter("updatePart");
        if (updatePart.equals("about")) {
            String fullName = request.getParameter("fullName");
            String dob = request.getParameter("dob");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String gender = request.getParameter("gender");
            String tmp = "";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            User existingUser = ud.checkExist("email", email, user.getUserId());
            if (existingUser != null) {
                // Email đã tồn tại
                session.setAttribute("notification", "Email already in use by another user.");
                response.sendRedirect(request.getContextPath() + "/profile");
                return;
            }
            user.setFullName(fullName);
            user.setAddress(address);
            user.setEmail(email);
            user.setGender(gender);

            user.setPhone(phone);
            try {
                // Chuyển đổi chuỗi ngày tháng từ request thành đối tượng java.util.Date
                java.util.Date utilDate = sdf.parse(dob);
                // Chuyển đổi từ java.util.Date sang java.sql.Date
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                // Cập nhật thông tin ngày tháng trong đối tượng User
                user.setDob(sqlDate);
            } catch (ParseException ex) {
                Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, ex);
            }
            String base64Image = "";
            Part filePart = request.getPart("avatar");
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // Lấy tên tệp
            if (fileName != null && !fileName.isEmpty()) {
                InputStream imageStream = filePart.getInputStream();
                byte[] imageBytes = IOUtils.toByteArray(imageStream);
                base64Image = "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
            } else {
                base64Image = user.getAvatar();
            }
            user.setAvatar(base64Image);
            ud.updateProfile(user);
            session.setAttribute("user", user);
            response.sendRedirect(request.getContextPath() + "/profile");
        } else {
            String oldPassword = request.getParameter("oldPassword");
            String newPassword = request.getParameter("newPassword");
            User loggedInUser = ud.login(user.getUsername(), oldPassword);
            if (loggedInUser != null) {
                String historyPassword = user.getHistoryPassword();
                JSONArray passwordHistory = new JSONArray(historyPassword != null ? historyPassword : "[]");

                // Cập nhật mật khẩu mới và lịch sử mật khẩu
                passwordHistory.put(newPassword);
                if (passwordHistory.length() > 5) {
                    // Giữ lại 5 mật khẩu gần nhất
                    JSONArray updatedHistory = new JSONArray();
                    for (int i = passwordHistory.length() - 5; i < passwordHistory.length(); i++) {
                        updatedHistory.put(passwordHistory.getString(i));
                    }
                    passwordHistory = updatedHistory;
                }

                user.setHistoryPassword(passwordHistory.toString());
                int numberOfChange = ud.changePassword(user.getUserId(), newPassword, user.getHistoryPassword());
                if (numberOfChange > 0) {
                    // Thay đổi mật khẩu thành công
                    session.setAttribute("notification", "Change password successfully.");
                    session.setAttribute("typeNoti", "alert-success");
                    session.removeAttribute("user");
                    response.sendRedirect(request.getContextPath() + "/login");
                } else {
                    // Có lỗi xảy ra trong quá trình thay đổi mật khẩu
                    session.setAttribute("notification", "Failed to change password. Please try again later.");
                    session.setAttribute("typeNoti", "alert-danger");
                    response.sendRedirect(request.getContextPath() + "/profile"); // Redirect back to the profile page
                }
            } else {
                // Mật khẩu cũ không đúng
                session.setAttribute("notification", "Old password is incorrect.");
                session.setAttribute("typeNoti", "alert-danger");
                response.sendRedirect(request.getContextPath() + "/profile"); // Redirect back to the profile page
            }
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
