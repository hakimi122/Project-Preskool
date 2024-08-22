
package controllers.common;

import dal.implement.AssessmentDAO;
import dal.implement.ClassDAOImpl;
import dal.interfaces.IAssessmentDAO;
import dal.interfaces.IClassDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import models.Assessment;
import models.ClassRoom;
import models.User;

public class AssessmentController extends HttpServlet {

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
            out.println("<title>Servlet AssessmentController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AssessmentController at " + request.getContextPath() + "</h1>");
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
        IClassDAO cd = new ClassDAOImpl();
        User userLogged = (User) session.getAttribute("user");
        List<ClassRoom> classes = cd.getClassList();
        String action = request.getParameter("action");
        if (action.equals("list")) {

            // Step to get distinct years
            Set<Integer> distinctYears = new HashSet<>();
            for (ClassRoom classRoom : classes) {
                distinctYears.add(classRoom.getYear());
            }

            // Convert Set to List to pass it to the view if needed
            List<Integer> yearList = new ArrayList<>(distinctYears);

            // Now you can set this list as an attribute for request to use it in your JSP or view layer
            request.setAttribute("yearList", yearList);

            // Get the selected year from the request
            String yearParam = request.getParameter("year");
            Integer selectedYear = -1;
            if (yearParam != null && !yearParam.isEmpty()) {
                selectedYear = Integer.parseInt(yearParam);
            }
            request.setAttribute("selectedYear", selectedYear);

            if (userLogged.getRole().equals("headmaster")) {
                List<ClassRoom> cl = classes;
                if (selectedYear != -1) {
                    cl = new ArrayList();
                    for (ClassRoom classRoom : classes) {
                        if (classRoom.getYear() == selectedYear) {
                            cl.add(classRoom);
                        }
                    }
                }

                request.setAttribute("classes", cl);
            } else if (userLogged.getRole().equals("teacher")) {
                List<ClassRoom> classesTaughtByTeacher = new ArrayList<>();
                int userId = userLogged.getUserId();

                // Filter the classes by chargeId
                for (ClassRoom classRoom : classes) {
                    if (classRoom.getChargeId() == userId) {
                        classesTaughtByTeacher.add(classRoom);
                    }
                }
                List<ClassRoom> cl = classesTaughtByTeacher;
                if (selectedYear != -1) {
                    cl = new ArrayList();
                    for (ClassRoom classRoom : classesTaughtByTeacher) {
                        if (classRoom.getYear() == selectedYear) {
                            cl.add(classRoom);
                        }
                    }
                }
                request.setAttribute("classes", cl);
            }
            request.getRequestDispatcher("./views/assess-class-list.jsp").forward(request, response);
        } else if (action.equals("assess")) {
            String classId_raw = request.getParameter("classId");
            String year_Raw = request.getParameter("year");
            int classId = Integer.parseInt(classId_raw);
            ClassRoom clr = null;
            for (ClassRoom cl : classes) {
                if (cl.getClassId() == classId) {
                    clr = cl;
                    break;
                }
            }
            if (clr != null) {
                request.setAttribute("c", clr);
                IAssessmentDAO ad = new AssessmentDAO();
                List<Assessment> assessments = ad.getAssessmentsByClassAndYear(clr.getStudents(), Integer.parseInt(year_Raw));
                request.setAttribute("assessments", assessments);
            }
            request.getRequestDispatcher("./views/assess.jsp").forward(request, response);
        } else if (action.equals("close")) {
            String classId_raw = request.getParameter("classId");
            int count = cd.DeleteClass(Integer.parseInt(classId_raw));
            if (count > 0) {
                session.setAttribute("notification", "Close class success");
                session.setAttribute("typeNoti", "alert-success");
            } else {
                session.setAttribute("notification", "Close class Failed. Internal error");
                session.setAttribute("typeNoti", "alert-danger");
            }
            response.sendRedirect(request.getContextPath() + "/assessment?action=list");
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
        IClassDAO cd = new ClassDAOImpl();
        IAssessmentDAO ad = new AssessmentDAO();
        String classId_raw = request.getParameter("classId");
        String year_raw = request.getParameter("yearB");
        int year = Integer.parseInt(year_raw);
        int classId = Integer.parseInt(classId_raw);
        ClassRoom classRoom = cd.getClassById(classId);
        int count = 0;
        if (classRoom != null) {
            for (User student : classRoom.getStudents()) {
                String status = request.getParameter("status" + student.getUserId());
                String rank = request.getParameter("note" + student.getUserId());

                Assessment a = new Assessment(student.getUserId(), year, status, rank);
                count += ad.SaveAssessment(a);
            }
        }
        if (count <= 0) {
            session.setAttribute("notification", "Assess failed. internal error");
            session.setAttribute("typeNoti", "alert-danger");
        } else {
            session.setAttribute("notification", "Assess success");
            session.setAttribute("typeNoti", "alert-success");
        }
        response.sendRedirect(request.getContextPath() + "/assessment?action=assess&classId="+classId+"&year="+year);
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
