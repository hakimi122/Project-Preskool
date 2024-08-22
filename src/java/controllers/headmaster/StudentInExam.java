
package controllers.headmaster;

import dal.implement.ClassDAOImpl;
import dal.implement.ExamDAO;
import dal.implement.StudentClassDAOImpl;
import dal.implement.UserDAOImpl;
import dal.interfaces.IClassDAO;
import dal.interfaces.IExamDAO;
import dal.interfaces.IStudentClassDAO;
import dal.interfaces.IUserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import models.Exam;
import models.User;


public class StudentInExam extends HttpServlet {

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
            out.println("<title>Servlet StudentInExam</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet StudentInExam at " + request.getContextPath() + "</h1>");
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
        IClassDAO cd = new ClassDAOImpl();
        IStudentClassDAO scd = new StudentClassDAOImpl();
        IUserDAO ud = new UserDAOImpl();
        IExamDAO ed = new ExamDAO();
        if (action.equals("save")) {
            String classId_raw = request.getParameter("examId");
            Exam classes = ed.getExamById(Integer.parseInt(classId_raw));
            request.setAttribute("c", classes);
            List<User> students = scd.getStudentsAddToExam(classId_raw);
            request.setAttribute("students", students);
            request.getRequestDispatcher("./views/examstudent-save.jsp").forward(request, response);
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

        IStudentClassDAO scd = new StudentClassDAOImpl();
        String type = request.getParameter("type");
        String classId = request.getParameter("examId");
        String slotId = request.getParameter("slotId");
        String examDate = request.getParameter("examDate");

        String[] studentIds = request.getParameterValues("selectedStudents");
        int countChange = 0;
        if (type.equals("save")) {
            for (String studentId : studentIds) {
                Exam c = scd.isStudentInExam(examDate, slotId, studentId);
                countChange += scd.saveStudentToExam(c, studentId, classId);
            }
        }

        if (countChange == studentIds.length) {
            session.setAttribute("notification", "All students added to exam successfully.");
            session.setAttribute("typeNoti", "alert-success");
        } else if (countChange > 0) {
            session.setAttribute("notification", "Some students added to exam successfully. Some failed.");
            session.setAttribute("typeNoti", "alert-warning");
        } else {
            session.setAttribute("notification", "Failed to add students to exam. Internal error.");
            session.setAttribute("typeNoti", "alert-danger");
        }
        response.sendRedirect(request.getContextPath() + "/studentinexam?action=save&examId=" + classId);
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
