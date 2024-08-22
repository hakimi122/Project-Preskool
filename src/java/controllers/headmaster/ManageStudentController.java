package controllers.headmaster;

import dal.implement.UserDAOImpl;
import dal.interfaces.IUserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.User;
import org.json.JSONArray;

public class ManageStudentController extends HttpServlet {

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
            out.println("<title>Servlet ManageStudent</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ManageStudent at " + request.getContextPath() + "</h1>");
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

        String action = request.getParameter("action");
        IUserDAO ud = new UserDAOImpl();
        if (action == null || action.equals("list")) {
            List<User> students = ud.getStudentList();
            request.setAttribute("students", students);
            request.getRequestDispatcher("./views/student-list.jsp").forward(request, response);
        }

        if (action.equals("add")) {
            request.getRequestDispatcher("./views/student-add.jsp").forward(request, response);
        } else if (action.equals("delete")) {
            String userId_raw = request.getParameter("userId");
            int userId = Integer.parseInt(userId_raw);
            int result = ud.DeleteUser(userId);
            if (result <= 0) {
                session.setAttribute("notification", "delete failed. internal error");
                session.setAttribute("typeNoti", "alert-danger");
                response.sendRedirect(request.getContextPath() + "/student?action=list");
                return;
            }
            session.setAttribute("notification", "delete success.");
            session.setAttribute("typeNoti", "alert-success");
            response.sendRedirect(request.getContextPath() + "/student?action=list");
        } else if (action.equals("details")) {
            String userId1_raw = request.getParameter("userId");
            int userId1 = Integer.parseInt(userId1_raw);
            User u = ud.getUserById(userId1);
            request.setAttribute("u", u);
            request.getRequestDispatcher("./views/user-details.jsp").forward(request, response);
        } else if (action.equals("edit")) {
            String uploadTime = (String) session.getAttribute("loadTime");
            User userEdit = new User();
            if (uploadTime != null && uploadTime.equals("2")) {
                userEdit = (User) session.getAttribute("userSave");
            } else {
                String userId_editRaw = request.getParameter("userId");
                int userId = Integer.parseInt(userId_editRaw);
                userEdit = ud.getUserById(userId);
            }
            request.setAttribute("userEdit", userEdit);
            request.getRequestDispatcher("./views/student-edit.jsp").forward(request, response);
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

        IUserDAO ud = new UserDAOImpl();
        String fullName = request.getParameter("fullName");
        String dob_raw = request.getParameter("dob");
        String gender = request.getParameter("gender");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String email = request.getParameter("email");

        User userSave = new User();
        userSave.setFullName(fullName);
        userSave.setGender(gender);
        userSave.setPhone(phone);
        userSave.setAddress(address);
        userSave.setRole("student");
        userSave.setActive(true);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Chuyển đổi chuỗi ngày tháng từ request thành đối tượng java.util.Date
            java.util.Date utilDate = sdf.parse(dob_raw);
            // Chuyển đổi từ java.util.Date sang java.sql.Date
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            // Cập nhật thông tin ngày tháng trong đối tượng User
            userSave.setDob(sqlDate);

        } catch (ParseException ex) {
            Logger.getLogger(ManageStudentController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        String userId = request.getParameter("userId");
        if (userId != null && userId.length() > 0) { // edit
            int userId_Number = Integer.parseInt(userId);
            User existingUser = ud.checkExist("email", email, userId_Number);
            if (existingUser != null) {
                // Email đã tồn tại
                userSave.setEmail(email);
                userSave.setUserId(userId_Number);
                session.setAttribute("userSave", userSave);
                session.setAttribute("loadTime", "2");
                session.setAttribute("notification", "Email already in use by another user.");
                session.setAttribute("typeNoti", "alert-danger");
                response.sendRedirect(request.getContextPath() + "/student?action=edit&userId=" + userId_Number);
                return;
            }
            userSave.setUserId(userId_Number);
            userSave.setEmail(email);
            int result = ud.SaveUser("edit", userSave);
            if (result <= 0) {
                session.setAttribute("notification", "Edit falied. internal error");
                session.setAttribute("typeNoti", "alert-danger");
                response.sendRedirect(request.getContextPath() + "/student?action=list");
            }
        } else {
            User existingUser = ud.checkExist("email", email, -1);
            if (existingUser != null) {
                // Email đã tồn tại
                userSave.setEmail(email);
                String username = request.getParameter("username");
                userSave.setUsername(username);
                session.setAttribute("userSave", userSave);
                session.setAttribute("notification", "Email already in use by another user.");
                session.setAttribute("typeNoti", "alert-danger");
                response.sendRedirect(request.getContextPath() + "/student?action=add");
                return;
            }
            userSave.setEmail(email);
            String username = request.getParameter("username");
            existingUser = ud.checkExist("username", username, -1);
            if (existingUser != null) {
                // Email đã tồn tại
                session.setAttribute("userSave", userSave);
                session.setAttribute("notification", "Username entered already in use by another user.");
                session.setAttribute("typeNoti", "alert-danger");
                response.sendRedirect(request.getContextPath() + "/student?action=add");
                return;
            }
            userSave.setUsername(username);
            String password = request.getParameter("password");
            JSONArray passwordHistory = new JSONArray("[]");
            passwordHistory.put(password);
            userSave.setHistoryPassword(passwordHistory.toString());
            userSave.setPassword(password);
            int result = ud.SaveUser("add", userSave);
            if (result <= 0) {
                session.setAttribute("notification", "Add falied. internal error");
                session.setAttribute("typeNoti", "alert-danger");
                response.sendRedirect(request.getContextPath() + "/student?action=add");
                return;
            }
        }
        session.setAttribute("notification", "Saved success.");
        session.setAttribute("typeNoti", "alert-success");
        response.sendRedirect(request.getContextPath() + "/student?action=list");
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
