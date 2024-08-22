
package controllers.teacher;

import dal.implement.SessionDAO;
import dal.implement.TimeSlotDAO;
import dal.implement.UserDAOImpl;
import dal.interfaces.ISessionDAO;
import dal.interfaces.ITimeSlotDAO;
import dal.interfaces.IUserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import models.Session;
import models.TimeSlot;
import models.User;
import utils.DateTimeHelper;


public class TeacherScheduleController extends HttpServlet {

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
            out.println("<title>Servlet TeacherScheduleController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet TeacherScheduleController at " + request.getContextPath() + "</h1>");
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
        
        IUserDAO ud = new UserDAOImpl();
        User user = (User) session.getAttribute("user");

        String from_raw = request.getParameter("from");
        String to_raw = request.getParameter("to");
        int lid = user.getUserId();
        java.sql.Date from = null;
        java.sql.Date to = null;
        try {

            if (from_raw == null || from_raw.length() == 0) { // khi user khong nhap date thi mac dinh lay date hien tai
                Date today = new Date();
                int todayOfWeek = DateTimeHelper.getDayofWeek(today);
                if (todayOfWeek == 1) {
                    todayOfWeek = 8;
                }
                Date u_from = DateTimeHelper.addDays(today, 2 - todayOfWeek);
                Date u_to = DateTimeHelper.addDays(today, 8 - todayOfWeek);
                from = DateTimeHelper.toDateSql(u_from);
                to = DateTimeHelper.toDateSql(u_to);
            } else {
                from = java.sql.Date.valueOf(from_raw);
                to = java.sql.Date.valueOf(to_raw);
            }

            session.setAttribute("from", from);
            session.setAttribute("to", to);

            request.setAttribute("dates", DateTimeHelper.getDateList(from, to));

            ITimeSlotDAO slotDB = new TimeSlotDAO();
            List<TimeSlot> slots = slotDB.getTimeSlotList();
            request.setAttribute("slots", slots);

            ISessionDAO sesDB = new SessionDAO();
            List<Session> sessions = sesDB.filter(lid, from, to);

            request.setAttribute("sessions", sessions);
        } catch (NumberFormatException e) {
            System.out.println(e);
        }

        request.getRequestDispatcher("./views/schedule-teacher.jsp").forward(request, response);
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
        User user = (User) session.getAttribute("user");

        String from_raw = request.getParameter("from");
        String to_raw = request.getParameter("to");
        int lid = user.getUserId();
        java.sql.Date from = null;
        java.sql.Date to = null;
        try {

            if (from_raw == null || from_raw.length() == 0) { // khi user khong nhap date thi mac dinh lay date hien tai
                Date today = new Date();
                int todayOfWeek = DateTimeHelper.getDayofWeek(today);
                if (todayOfWeek == 1) {
                    todayOfWeek = 8;
                }
                Date u_from = DateTimeHelper.addDays(today, 2 - todayOfWeek);
                Date u_to = DateTimeHelper.addDays(today, 8 - todayOfWeek);
                from = DateTimeHelper.toDateSql(u_from);
                to = DateTimeHelper.toDateSql(u_to);
            } else {
                from = java.sql.Date.valueOf(from_raw);
                to = java.sql.Date.valueOf(to_raw);
            }

            session.setAttribute("from", from);
            session.setAttribute("to", to);

            request.setAttribute("dates", DateTimeHelper.getDateList(from, to));

            ITimeSlotDAO slotDB = new TimeSlotDAO();
            List<TimeSlot> slots = slotDB.getTimeSlotList();
            request.setAttribute("slots", slots);

            ISessionDAO sesDB = new SessionDAO();
            List<Session> sessions = sesDB.filter(lid, from, to);

            request.setAttribute("sessions", sessions);
        } catch (NumberFormatException e) {
            System.out.println(e);
        }

        request.getRequestDispatcher("./views/schedule-teacher.jsp").forward(request, response);
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
