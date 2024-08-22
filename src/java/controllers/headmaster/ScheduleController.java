
package controllers.headmaster;

import dal.implement.ClassDAOImpl;
import dal.implement.SessionDAO;
import dal.implement.SubjectDAO;
import dal.implement.TimeSlotDAO;
import dal.implement.UserDAOImpl;
import dal.interfaces.IClassDAO;
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


public class ScheduleController extends HttpServlet {

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
        IClassDAO cd = new ClassDAOImpl();
        ISessionDAO sd = new SessionDAO();
        ISubjectDAO sud = new SubjectDAO();
        ITimeSlotDAO td = new TimeSlotDAO();
        List<ClassRoom> classes = cd.getClassList();
        // Extract unique years
        Set<Integer> uniqueYears = new HashSet<>();
        for (ClassRoom classRoom : classes) {
            uniqueYears.add(classRoom.getYear());
        }
        List<ClassRoom> filteredClasses = new ArrayList();
        request.setAttribute("uniqueYears", uniqueYears);
        String action = request.getParameter("action");
        if (action == null || action.equals("filteryear")) {
            String yearParam = request.getParameter("year");
            int year = -1;
            if (yearParam != null && !yearParam.isEmpty()) {
                year = Integer.parseInt(yearParam);
            }
            if (year != -1) {
                for (ClassRoom classRoom : classes) {
                    if (classRoom.getYear() == year) {
                        filteredClasses.add(classRoom);
                    }
                }
            } else {
                filteredClasses = classes;
            }
            request.setAttribute("year", year);
            request.setAttribute("classes", filteredClasses);
            request.getRequestDispatcher("./views/manage-schedule-list.jsp").forward(request, response);
        } else if (action.equals("getsessions")) {
            String classId = request.getParameter("classId");
            ClassRoom cl = cd.getClassById(Integer.parseInt(classId));
            List<Session> sessions = sd.getSessionByClassId(Integer.parseInt(classId));
            request.setAttribute("sessions", sessions);
            request.setAttribute("cl", cl);
            request.getRequestDispatcher("./views/session-list.jsp").forward(request, response);
        } else if (action.equals("addsession")) {
            String classId = request.getParameter("classId");
            ClassRoom cl = cd.getClassById(Integer.parseInt(classId));
            List<Subject> subjects = sud.getSubjectList();
            // Lấy danh sách supervisors cho subject có Id = 2
            List<User> defaultSupervisors = sud.getTeachersBySubject(2);
            request.setAttribute("cl", cl);
            request.setAttribute("subjects", subjects);
            request.setAttribute("defaultSupervisors", defaultSupervisors);
            request.getRequestDispatcher("./views/session-add.jsp").forward(request, response);
        } else if ("getTeachers".equals(action)) {
            int subjectId = Integer.parseInt(request.getParameter("subjectId"));
            IUserDAO ud = new UserDAOImpl();
            List<User> teachers = sud.getTeachersBySubject(subjectId);
            JSONArray jsonArray = new JSONArray();
            for (User teacher : teachers) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", teacher.getUserId());
                jsonObject.put("fullName", teacher.getFullName());
                jsonArray.put(jsonObject);
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonArray.toString());
        } else if ("getSlots".equals(action)) {
            int supervisorId = Integer.parseInt(request.getParameter("supervisorId"));
            String date = request.getParameter("date");

            TimeSlotDAO timeSlotDAO = new TimeSlotDAO();
            List<TimeSlot> availableSlots = timeSlotDAO.getAvailableSlotsByTeacherAndDate(supervisorId, date);

            JSONArray jsonArray = new JSONArray();
            for (TimeSlot slot : availableSlots) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("slotId", slot.getSlotId());
                jsonObject.put("description", slot.getDescription());
                jsonArray.put(jsonObject);
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonArray.toString());
        } else if ("editsession".equals(action)) {
            String sessionId = request.getParameter("sessionId");
            Session ss = sd.getSessionById(Integer.parseInt(sessionId));
            List<Subject> subjects = sud.getSubjectList();
            request.setAttribute("subjects", subjects);
            request.setAttribute("ss", ss);
            request.getRequestDispatcher("./views/session-edit.jsp").forward(request, response);
        } else if ("deletesession".equals(action)) {
            String sessionId = request.getParameter("sessionId");
            String classId = request.getParameter("classId");
            int result = sd.deleteSession(Integer.parseInt(sessionId));
            if (result > 0) {
                session.setAttribute("notification", "Session deleted successfully.");
                session.setAttribute("typeNoti", "alert-success");
            } else {
                session.setAttribute("notification", "Session deleted failed. Internal error");
                session.setAttribute("typeNoti", "alert-success");
            }
            response.sendRedirect(request.getContextPath() + "/schedule-manage?action=getsessions&classId=" + Integer.parseInt(classId));
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
        String classIdStr = request.getParameter("classId");
        int classId = Integer.parseInt(classIdStr);

        String action = request.getParameter("action");
        if (action != null && action.equals("addsession")) {
            String subjectIdStr = request.getParameter("subjectId");
            String room = request.getParameter("room");
            String supervisorIdStr = request.getParameter("supervisorId");
            String dateStr = request.getParameter("date");
            String slotIdStr = request.getParameter("slotId");
            String isActiveStr = request.getParameter("isActive");

            int subjectId = Integer.parseInt(subjectIdStr);
            int supervisorId = Integer.parseInt(supervisorIdStr);
            int slotId = Integer.parseInt(slotIdStr);
            boolean isActive = Boolean.parseBoolean(isActiveStr);

            Session newSession = new Session();
            newSession.setClassId(classId);
            newSession.setSubjectId(subjectId);
            newSession.setSupervisorId(supervisorId);
            newSession.setDate(java.sql.Date.valueOf(dateStr));
            newSession.setSlotId(slotId);
            newSession.setRoom(room);
            newSession.setIsAttended(isActive);

            ISessionDAO sessionDAO = new SessionDAO();
            try {
                int result = sessionDAO.addSession(newSession);
                if (result > 0) {
                    session.setAttribute("notification", "Session added successfully.");
                    session.setAttribute("typeNoti", "alert-success");
                }
            } catch (Exception e) {
                session.setAttribute("notification", "Failed to add session.");
                session.setAttribute("typeNoti", "alert-danger");
            }
        } else if (action.equals("editsession")) {
            String sessionIdStr = request.getParameter("sessionId");
            String subjectIdStr = request.getParameter("subjectId");
             String room = request.getParameter("room");
            String supervisorIdStr = request.getParameter("supervisorId");
            String dateStr = request.getParameter("date");
            String slotIdStr = request.getParameter("slotId");
            String isActiveStr = request.getParameter("isActive");

            int sessionId = Integer.parseInt(sessionIdStr);
            int subjectId = Integer.parseInt(subjectIdStr);

            int supervisorId = Integer.parseInt(supervisorIdStr);
            int slotId = Integer.parseInt(slotIdStr);
            boolean isActive = Boolean.parseBoolean(isActiveStr);

            // Update session details in the database
            ISessionDAO sessionDAO = new SessionDAO();
            Session sessionToUpdate = sessionDAO.getSessionById(sessionId);
            sessionToUpdate.setSubjectId(subjectId);
            sessionToUpdate.setSupervisorId(supervisorId);
            sessionToUpdate.setDate(java.sql.Date.valueOf(dateStr));
            sessionToUpdate.setSlotId(slotId);
            sessionToUpdate.setClassId(classId);
            sessionToUpdate.setRoom(room);
            sessionToUpdate.setIsAttended(isActive);

            try {
                int result = sessionDAO.updateSession(sessionToUpdate);
                if (result > 0) {
                    session.setAttribute("notification", "Session updated successfully.");
                    session.setAttribute("typeNoti", "alert-success");
                } else {
                    session.setAttribute("notification", "Failed to update session.");
                    session.setAttribute("typeNoti", "alert-danger");
                }
            } catch (Exception e) {
                session.setAttribute("notification", "Failed to update session.");
                session.setAttribute("typeNoti", "alert-danger");
            }
        }
        response.sendRedirect(request.getContextPath() + "/schedule-manage");
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
