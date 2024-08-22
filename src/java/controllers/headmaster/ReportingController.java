
package controllers.headmaster;

import dal.implement.ClassDAOImpl;
import dal.implement.ReportingDAOImpl;
import dal.implement.SessionDAO;
import dal.implement.SubjectDAO;
import dal.implement.TimeSlotDAO;
import dal.implement.UserDAOImpl;
import dal.interfaces.IClassDAO;
import dal.interfaces.IReportingDAO;
import dal.interfaces.ISessionDAO;
import dal.interfaces.ISubjectDAO;
import dal.interfaces.ITimeSlotDAO;
import dal.interfaces.IUserDAO;
import java.util.List;
import models.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import utils.DateTimeHelper;
import jakarta.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import models.ClassRoom;
import models.Session;
import models.Subject;
import models.TimeSlot;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.DateTimeHelper;


public class ReportingController extends HttpServlet {

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
            out.println("<title>Servlet ScheduleController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ScheduleController at " + request.getContextPath() + "</h1>");
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
        IReportingDAO reportingDAO = new ReportingDAOImpl();
        request.setAttribute("reporting", reportingDAO.getReporting());
        request.setAttribute("studentStar", reportingDAO.getStudentStar());
        request.getRequestDispatcher("./views/headmaster-dashboard.jsp").forward(request, response);
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
