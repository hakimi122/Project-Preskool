
package controllers.headmaster;

import dal.implement.ClassDAOImpl;
import dal.implement.ExamDAO;
import dal.implement.ReportingDAOImpl;
import dal.implement.SubjectDAO;
import dal.implement.TimeSlotDAO;
import dal.implement.UserDAOImpl;
import dal.interfaces.IClassDAO;
import dal.interfaces.IExamDAO;
import dal.interfaces.IReportingDAO;
import dal.interfaces.ISubjectDAO;
import dal.interfaces.ITimeSlotDAO;
import dal.interfaces.IUserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import models.Exam;
import models.Reporting;
import models.Subject;
import models.TimeSlot;
import models.User;
import org.json.JSONArray;
import org.json.JSONObject;

public class StudentAnalysisController extends HttpServlet {

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
            out.println("<title>Servlet ExamController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ExamController at " + request.getContextPath() + "</h1>");
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

        String dateSelect = request.getParameter("dateSelect");
        String slot = request.getParameter("slotId");
        IReportingDAO reportingDAO = new ReportingDAOImpl();
        List<Reporting.StudentAnalysis> analysises = new ArrayList();
        analysises = reportingDAO.getStudentAnalysis();
        JSONArray jsonArray = new JSONArray();
        for (Reporting.StudentAnalysis analysis : analysises) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("studentId", analysis.getStudentId());
            jsonObject.put("year", analysis.getYear());
            jsonObject.put("status", analysis.getStatus());
            jsonObject.put("classId", analysis.getClassId());
            jsonObject.put("className", analysis.getClassName());
            jsonArray.put(jsonObject);
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonArray.toString());

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
        String examType = request.getParameter("examType");
        String examId = request.getParameter("examId");
        String teacherId = request.getParameter("teacherId");
        String slotId = request.getParameter("slotId");
        String subjectId = request.getParameter("subjectId");
        String examDate = request.getParameter("examDate");
        String room = request.getParameter("room");
        Exam e = new Exam();
        if (examId == null || examId.isEmpty()) {
            e.setExamId(-1);
        } else {
            e.setExamId(Integer.parseInt(examId));
        }
        e.setExamType(examType);
        e.setTeacherId(Integer.parseInt(teacherId));
        e.setSlotId(Integer.parseInt(slotId));
        e.setSubjectId(Integer.parseInt(subjectId));

        // Validate and convert the date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            java.util.Date parsedDate = sdf.parse(examDate);
            java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());
            e.setExamDate(sqlDate);
        } catch (ParseException ex) {
            session.setAttribute("notification", "Invalid date format. Please use yyyy-MM-dd.");
            session.setAttribute("typeNoti", "alert-danger");
            response.sendRedirect(request.getContextPath() + "/exam?action=add");
            return;
        }
        e.setRoom(room);

        IExamDAO ed = new ExamDAO();
        int result = ed.saveExam(e);
        if (result <= 0) {
            session.setAttribute("notification", "Save Failed. Internal error");
            session.setAttribute("typeNoti", "alert-danger");
        } else {
            session.setAttribute("notification", "Save successfully.");
            session.setAttribute("typeNoti", "alert-success");
        }
        response.sendRedirect(request.getContextPath() + "/exam?action=list");
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
