
package controllers.headmaster;

import dal.implement.SubjectDAO;
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
import models.Subject;
import models.User;
import org.json.JSONArray;

public class ManageTeacherController extends HttpServlet {

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
            out.println("<title>Servlet ManageTeacher</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ManageTeacher at " + request.getContextPath() + "</h1>");
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
            List<User> teachers = ud.getTeacherList();
            request.setAttribute("teachers", teachers);
            request.getRequestDispatcher("./views/teacher-list.jsp").forward(request, response);
        } else if (action.equals("add")) {
            request.getRequestDispatcher("./views/teacher-add.jsp").forward(request, response);
        } else if (action.equals("delete")) {
            String userId_raw = request.getParameter("userId");
            int userId = Integer.parseInt(userId_raw);
            int result = ud.DeleteUser(userId);
            if (result <= 0) {
                session.setAttribute("notification", "delete failed. internal error");
                session.setAttribute("typeNoti", "alert-danger");
                response.sendRedirect(request.getContextPath() + "/teacher?action=list");
                return;
            }
            session.setAttribute("notification", "delete success.");
            session.setAttribute("typeNoti", "alert-success");
            response.sendRedirect(request.getContextPath() + "/teacher?action=list");
        } else if (action.equals("details")) {
            String userId1_raw = request.getParameter("userId");
            int userId1 = Integer.parseInt(userId1_raw);
            User u = ud.getUserById(userId1);
            request.setAttribute("u", u);
            request.getRequestDispatcher("./views/user-details.jsp").forward(request, response);
        } else if (action.equals("assignsubject")) {
            String userId1_raw = request.getParameter("teacherId");
            User u = ud.getUserById(Integer.parseInt(userId1_raw));
            List<Subject> subjects = new SubjectDAO().getSubjectList();
            request.setAttribute("subjects", subjects);
            request.setAttribute("t", u);
            request.getRequestDispatcher("./views/assign-subject.jsp").forward(request, response);
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
        String type = request.getParameter("type");
        if (type.equals("assignsubject")) {
            String teacherId_raw = request.getParameter("teacherId");
            int teacherId = Integer.parseInt(teacherId_raw);
            String[] selectedSubjects = request.getParameterValues("selectedSubjects");
            if (selectedSubjects != null) {
                int[] subjectIds = new int[selectedSubjects.length];
                for (int i = 0; i < selectedSubjects.length; i++) {
                    subjectIds[i] = Integer.parseInt(selectedSubjects[i]);
                }
                int result = new SubjectDAO().AssignSubjectsToTeacher(teacherId, subjectIds);
                if (result <= 0) {
                    session.setAttribute("notification", "Assign subjects failed. Internal error.");
                    session.setAttribute("typeNoti", "alert-danger");
                } else {
                    session.setAttribute("notification", "Assign subjects success.");
                    session.setAttribute("typeNoti", "alert-success");
                }
            } else {
                session.setAttribute("notification", "No subjects selected.");
                session.setAttribute("typeNoti", "alert-warning");
            }
            response.sendRedirect(request.getContextPath() + "/teacher?action=assignsubject&teacherId=" + teacherId);
        } else {
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
            userSave.setRole("teacher");
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
                Logger.getLogger(ManageTeacherController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

            if (type.equals("add")) {
                User existingUser = ud.checkExist("email", email, -1);
                if (existingUser != null) {
                    // Email đã tồn tại
                    String username = request.getParameter("username");
                    userSave.setUsername(username);
                    userSave.setEmail(email);
                    session.setAttribute("userSavet", userSave);
                    session.setAttribute("notification", "Email already in use by another user.");
                    session.setAttribute("typeNoti", "alert-danger");
                    response.sendRedirect(request.getContextPath() + "/teacher?action=add");
                    return;
                }
                userSave.setEmail(email);
                String username = request.getParameter("username");
                existingUser = ud.checkExist("username", username, -1);
                if (existingUser != null) {
                    // Email đã tồn tại
                    session.setAttribute("notification", "Username entered already in use by another user.");
                    session.setAttribute("typeNoti", "alert-danger");
                    response.sendRedirect(request.getContextPath() + "/teacher?action=add");
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
                    response.sendRedirect(request.getContextPath() + "/teacher?action=add");
                    return;
                }
            } else {
                String userId_raw = request.getParameter("userId");
                int userId = Integer.parseInt(userId_raw);
                User existingUser = ud.checkExist("email", email, userId);
                if (existingUser != null) {
                    // Email đã tồn tại
                    userSave.setEmail(email);
                    userSave.setUserId(userId);
                    session.setAttribute("userSavet", userSave);
                    session.setAttribute("notification", "Email already in use by another user.");
                    session.setAttribute("typeNoti", "alert-danger");
                    response.sendRedirect(request.getContextPath() + "/teacher?action=list");
                    return;
                }
                userSave.setUserId(userId);
                userSave.setEmail(email);
                int result = ud.SaveUser("edit", userSave);
                if (result <= 0) {
                    session.setAttribute("notification", "Edit falied. internal error");
                    session.setAttribute("typeNoti", "alert-danger");
                    response.sendRedirect(request.getContextPath() + "/teacher?action=list");                    return;
                }
            }
            session.setAttribute("notification", type + " success.");
            session.setAttribute("typeNoti", "alert-success");
            response.sendRedirect(request.getContextPath() + "/teacher?action=list");
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
