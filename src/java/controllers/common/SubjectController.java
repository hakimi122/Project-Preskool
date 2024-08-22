
package controllers.common;

import dal.implement.SubjectDAO;
import dal.interfaces.ISubjectDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import models.Subject;

public class SubjectController extends HttpServlet {

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
            out.println("<title>Servlet SubjectController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SubjectController at " + request.getContextPath() + "</h1>");
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
        ISubjectDAO ud = new SubjectDAO();
        if (action == null || action.equals("list")) {
            List<Subject> students = ud.getSubjectList();
            request.setAttribute("subjects", students);
            request.getRequestDispatcher("./views/subject-list.jsp").forward(request, response);
        } else if (action.equals("add")) {
            request.getRequestDispatcher("./views/subject-add.jsp").forward(request, response);
        } else if (action.equals("delete")) {
            String userId_raw = request.getParameter("subjectId");
            int userId = Integer.parseInt(userId_raw);
            int result = ud.DeleteSubject(userId);
            if (result <= 0) {
                session.setAttribute("notification", "delete failed. internal error");
                session.setAttribute("typeNoti", "alert-danger");
                response.sendRedirect(request.getContextPath() + "/subject?action=list");
                return;
            }
            session.setAttribute("notification", "delete success.");
            session.setAttribute("typeNoti", "alert-success");
            response.sendRedirect(request.getContextPath() + "/subject?action=list");
            
        } else if (action.equals("edit")) {
            String userId_editRaw = request.getParameter("subjectId");
            int userId = Integer.parseInt(userId_editRaw);
            Subject userEdit = ud.getSubjectById(userId);
            request.setAttribute("subjectEdit", userEdit);
            request.getRequestDispatcher("./views/subject-edit.jsp").forward(request, response);
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
        ISubjectDAO sd = new SubjectDAO();
        
        String subjectId = request.getParameter("subjectId");
        String subjectName = request.getParameter("subjectName");
        String isTeaching = request.getParameter("isTeaching");
        
        Subject subjectSave = new Subject();
        subjectSave.setSubjectName(subjectName);
        
        if (subjectId == null || subjectId.isEmpty()) {
            subjectSave.setSubjectId(-1);
            subjectSave.setIsTeaching(true);
        } else {
            subjectSave.setSubjectId(Integer.parseInt(subjectId));
            subjectSave.setIsTeaching(Boolean.parseBoolean(isTeaching));
        }
        int count = sd.SaveSubject(subjectSave);
        if (count > 0) {
            session.setAttribute("notification", "Save success.");
            session.setAttribute("typeNoti", "alert-success");
            response.sendRedirect(request.getContextPath() + "/subject?action=list");
            return;
        }
        session.setAttribute("notification", "delete failed. internal error");
        session.setAttribute("typeNoti", "alert-danger");
        response.sendRedirect(request.getContextPath() + "/subject?action=list");
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
