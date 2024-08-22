package controllers.headmaster;

import dal.implement.AssessmentDAO;
import dal.implement.ClassDAOImpl;
import dal.implement.StudentClassDAOImpl;
import dal.implement.UserDAOImpl;
import dal.interfaces.IAssessmentDAO;
import dal.interfaces.IClassDAO;
import dal.interfaces.IStudentClassDAO;
import dal.interfaces.IUserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.List;
import models.ClassRoom;
import models.StudentClass;
import models.User;

public class ManageStudentInClassController extends HttpServlet {

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
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ManageClass</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ManageClass at " + request.getContextPath() + "</h1>");
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
        if (action.equals("add")) {
            String classId_raw = request.getParameter("classId");
            ClassRoom classes = cd.getClassById(Integer.parseInt(classId_raw));
            request.setAttribute("c", classes);
            List<User> students = scd.getStudentsToAddToClass(classId_raw);
            request.setAttribute("students", students);
            request.getRequestDispatcher("./views/classstudent-add.jsp").forward(request, response);
        } else if (action.equals("list")) {
            String userId_raw = request.getParameter("classId");
            ClassRoom classes = cd.getClassById(Integer.parseInt(userId_raw));
            request.setAttribute("c", classes);
            request.getRequestDispatcher("./views/classstudent-list.jsp").forward(request, response);
        } else if (action.equals("delete")) {
            String classId = request.getParameter("classId");
            String studentId = request.getParameter("studentId");

            int result = scd.removeStudentFromClass(studentId, classId);
            if (result > 0) {
                session.setAttribute("notification", "Deleted success.");
                session.setAttribute("typeNoti", "alert-success");
            } else {
                session.setAttribute("notification", "Deleted Failed. Internal error");
                session.setAttribute("typeNoti", "alert-danger");
            }
            response.sendRedirect(request.getContextPath() + "/studentinclass?action=list&classId=" + classId);
        } else if (action.equals("enterscore")) {
            String userId_raw = request.getParameter("classId");
            
            ClassRoom classes = cd.getClassById(Integer.parseInt(userId_raw));
            List<StudentClass> scs = scd.getStudentClassScore(Integer.parseInt(userId_raw));
            request.setAttribute("scs", scs);
            request.setAttribute("c", classes);
            request.getRequestDispatcher("./views/enterscore.jsp").forward(request, response);
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

        IClassDAO ud = new ClassDAOImpl();
        IStudentClassDAO scd = new StudentClassDAOImpl();
        String type = request.getParameter("type");
        String classId = request.getParameter("classId");
        String year = request.getParameter("year");
        String[] studentIds = request.getParameterValues("selectedStudents");
        int countChange = 0;
        if (type.equals("add")) {
            for (String studentId : studentIds) {
                ClassRoom c = scd.isStudentInClassForYear(classId, studentId, year);
                if (c != null) {
                    scd.changeClass(c.getClassId() + "", studentId);
                }
                countChange += scd.saveStudentToClass(classId, studentId);
            }
            if (countChange == studentIds.length) {
                session.setAttribute("notification", "All students added to class successfully.");
                session.setAttribute("typeNoti", "alert-success");
            } else if (countChange > 0) {
                session.setAttribute("notification", "Some students added to class successfully. Some failed.");
                session.setAttribute("typeNoti", "alert-warning");
            } else {
                session.setAttribute("notification", "Failed to add students to class. Internal error.");
                session.setAttribute("typeNoti", "alert-danger");
            }
            response.sendRedirect(request.getContextPath() + "/studentinclass?action=list&classId=" + classId);
        } else if (type.equals("enterscore")) {
            IAssessmentDAO as = new AssessmentDAO();
            Enumeration<String> parameterNames = request.getParameterNames();
            int countUpdated = 0;
            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();
                if (paramName.startsWith("scoreSemester1_") || paramName.startsWith("scoreSemester2_")) {
                    String[] parts = paramName.split("_");
                    String scoreType = parts[0];
                    int studentId = Integer.parseInt(parts[1]);
                    float score = Float.parseFloat(request.getParameter(paramName));

                    StudentClassDAOImpl studentClassDAO = new StudentClassDAOImpl();

                    // Get the current scores
                    float currentScoreSemester1 = 0.0f;
                    float currentScoreSemester2 = 0.0f;

                    // Update the scores accordingly
                    if (scoreType.equals("scoreSemester1")) {
                        currentScoreSemester1 = score;
                        currentScoreSemester2 = studentClassDAO.getScore(Integer.parseInt(classId), studentId).getScoreSemester2(); // Assuming getScore method exists
                    } else if (scoreType.equals("scoreSemester2")) {
                        currentScoreSemester2 = score;
                        currentScoreSemester1 = studentClassDAO.getScore(Integer.parseInt(classId), studentId).getScoreSemester1(); // Assuming getScore method exists
                    }

                    // Update the scores in the database
                    int result = studentClassDAO.updateScore(Integer.parseInt(classId), studentId, currentScoreSemester1, currentScoreSemester2);
                    if (result > 0) {
                        countUpdated++;
                        float averageScore = (currentScoreSemester1 * 1.0f + currentScoreSemester2) / 2;
                        as.updateAssessment(studentId, Integer.parseInt(year), averageScore, "");
                    }

                }
            }
            if (countUpdated > 0) {
                session.setAttribute("notification", "Scores updated successfully.");
                session.setAttribute("typeNoti", "alert-success");
            } else {
                session.setAttribute("notification", "Failed to update scores. Internal error.");
                session.setAttribute("typeNoti", "alert-danger");
            }
            response.sendRedirect(request.getContextPath() + "/studentinclass?action=enterscore&classId=" + classId);
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
