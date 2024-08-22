
package controllers.common;

import dal.implement.SessionDAO;
import dal.implement.UserDAOImpl;
import dal.interfaces.ISessionDAO;
import dal.interfaces.IUserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Attendance;
import models.Session;
import models.User;


public class AttendanceCheckingController extends HttpServlet {

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
            out.println("<title>Servlet AttendanceCheckingController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AttendanceCheckingController at " + request.getContextPath() + "</h1>");
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

        IUserDAO db = new UserDAOImpl();
        User acc = (User) request.getSession().getAttribute("user");

        String sesid_raw = request.getParameter("sessionId");
        int sesid = sesid_raw == null ? -1 : Integer.parseInt(sesid_raw);
        int flag = Integer.parseInt(request.getParameter("flag"));
        ISessionDAO sesDB = new SessionDAO();
        Session ses = sesDB.getSessionById(sesid);
        request.setAttribute("ses", ses);
        request.setAttribute("flag", flag);
        request.getRequestDispatcher("./views/attendance-checking.jsp").forward(request, response);
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
        User loggeduser = (User) session.getAttribute("user");
        Session ses = new Session();
        int flag = -1;

        ses.setSessionId(Integer.parseInt(request.getParameter("sesid")));

        flag = Integer.parseInt(request.getParameter("flag"));
        String[] stdids = request.getParameterValues("stdid");
        if (stdids != null) {
            for (String stdid : stdids) {
                Attendance a = new Attendance();
                User student = new User();
                student.setUserId(Integer.parseInt(stdid));
                a.setNote(request.getParameter("description" + stdid) == null ? "" : request.getParameter("description" + stdid));
                a.setPresent(request.getParameter("present" + stdid).equals("present"));
                a.setStudent(student);
                ses.getAttendances().add(a);
                // response.getWriter().print(request.getParameter("description" + stdid));
            }
        }
        ISessionDAO db = new SessionDAO();
        int result = db.updateAttendanceChecking(ses);
        if (result != -1) {
            session.setAttribute("notification", "attendance checking success.");
            session.setAttribute("typeNoti", "alert-success");
        } else {
            session.setAttribute("notification", "attendance checking failed.");
            session.setAttribute("typeNoti", "alert-danger");
        }
        if (loggeduser.getRole().equals("teacher")) {
            response.sendRedirect(request.getContextPath() + "/schedule-teacher");
        } else {
            response.sendRedirect(request.getContextPath() + "/schedule");
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
