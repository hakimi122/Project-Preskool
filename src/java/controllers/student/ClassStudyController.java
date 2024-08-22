package controllers.student;

import dal.implement.AssessmentDAO;
import dal.implement.ClassDAOImpl;
import dal.implement.StudentClassDAOImpl;
import dal.interfaces.IAssessmentDAO;
import dal.interfaces.IClassDAO;
import dal.interfaces.IStudentClassDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import models.Assessment;
import models.ClassRoom;
import models.StudentClass;
import models.User;


/**
 * Servlet implementation class ClassStudyController
 */
public class ClassStudyController extends HttpServlet {

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
            out.println("<title>Servlet ClassStudyController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ClassStudyController at " + request.getContextPath() + "</h1>");
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
        String action = request.getParameter("action");
        IStudentClassDAO scd = new StudentClassDAOImpl();
        HttpSession session = request.getSession();
        User student = (User) session.getAttribute("user");
        if (student == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if (action.equals("list")) {
            List<ClassRoom> classes = scd.getClassesStudentStudy(student.getUserId());
            request.setAttribute("classes", classes);
            request.getRequestDispatcher("./views/classstudy-list.jsp").forward(request, response);
        } else {
            IClassDAO cd = new ClassDAOImpl();
            IAssessmentDAO as = new AssessmentDAO();
            String classId = request.getParameter("classId");
            int studentId = student.getUserId();
            ClassRoom classroom = cd.getClassById(Integer.parseInt(classId));
            List<Assessment> ass = new ArrayList<>();
            if (!classroom.getStudents().isEmpty()) {
                for (var s : classroom.getStudents()) {
                    Assessment a = as.getAssessmentsByStudentAndYear(s.getUserId(), classroom.getYear());
                    ass.add(a);
                }
            }

            request.setAttribute("ass", ass);
            request.setAttribute("classroom", classroom);
            request.getRequestDispatcher("./views/classstudy-details.jsp").forward(request, response);
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
        processRequest(request, response);
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
