package controllers.headmaster;

import dal.implement.ClassDAOImpl;
import dal.interfaces.IClassDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import models.ClassRoom;
import models.User;

public class ManageClassController extends HttpServlet {

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
        //kiem tra neu phien het han
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        IClassDAO cd = new ClassDAOImpl();
        if (action == null || action.equals("list")) {
            List<ClassRoom> classes = cd.getClassList();//lay ra list class
            request.setAttribute("classes", classes);
            request.getRequestDispatcher("./views/class-list.jsp").forward(request, response);
        }
        if (action.equals("delete")) {
            String userId_raw = request.getParameter("classId");
            int classId = Integer.parseInt(userId_raw);
            int result = cd.DeleteClass(classId);
            if (result <= 0) {
                session.setAttribute("notification", "delete failed. internal error");
                session.setAttribute("typeNoti", "alert-danger");
                response.sendRedirect(request.getContextPath() + "/class?action=list");
                return;
            }
            session.setAttribute("notification", "delete success.");
            session.setAttribute("typeNoti", "alert-success");
            response.sendRedirect(request.getContextPath() + "/class?action=list");
        } else if (action.equals("edit")) {
            String classId_editRaw = request.getParameter("classId");
            int classId = Integer.parseInt(classId_editRaw);
            ClassRoom c = cd.getClassById(classId);
            List<User> regularTeachers = cd.getRegularTeacherList(c.getTeacherInCharge().getUserId());
            regularTeachers.add(c.getTeacherInCharge());
            request.setAttribute("regularTeachers", regularTeachers);
            request.setAttribute("c", c);
            request.getRequestDispatcher("./views/class-edit.jsp").forward(request, response);
        } else if (action.equals("add")) {
            List<User> regularTeachers = cd.getRegularTeacherList(-1);
            request.setAttribute("regularTeachers", regularTeachers);
            request.getRequestDispatcher("./views/class-add.jsp").forward(request, response);

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
        String classId = request.getParameter("classId");

        IClassDAO ud = new ClassDAOImpl();
        String year = request.getParameter("year");
        String className = request.getParameter("className");
        Boolean isClassNameExist = false;
        if (classId != null) {
            isClassNameExist = ud.isClassNameExisted(className, Integer.parseInt(year), Integer.parseInt(classId));
        } else {
            isClassNameExist = ud.isClassNameExisted(className, Integer.parseInt(year), -1);
        }
        if (isClassNameExist) {
            session.setAttribute("notification", "Save Failed. Class is existed. Please change className or year");
            session.setAttribute("typeNoti", "alert-danger");
            if (classId == null) {
                response.sendRedirect(request.getContextPath() + "/class?action=add");
            } else {
                response.sendRedirect(request.getContextPath() + "/class?action=edit&classId=" + classId);
            }
            return;
        }

        String level = request.getParameter("level");
        String chargeTeacherId = request.getParameter("chargeTeacherId");
        String isActive = request.getParameter("isActive");
        String limitNumberOfStudent = request.getParameter("limitNumberOfStudent");
        ClassRoom saveClass = new ClassRoom();
        int classIdNumber = -1;
        int limit = 0;
        try {
            limit = Integer.parseInt(limitNumberOfStudent);
        } catch (NumberFormatException e) {
            session.setAttribute("notification", "Limit Number of student must be  a integer number");
            session.setAttribute("typeNoti", "alert-danger");
            if (classId == null) {
                response.sendRedirect(request.getContextPath() + "/class?action=add");
            } else {
                response.sendRedirect(request.getContextPath() + "/class?action=edit&classId=" + classId);
            }
            return;
        }
        int cur = 0;
        if (classId != null && classId.length() > 0) {
            String currentNOS = request.getParameter("currentNOS");

            try {
                cur = Integer.parseInt(currentNOS);
            } catch (NumberFormatException e) {
                cur = 0;
            }

            if (limit < cur) {
                session.setAttribute("notification", "Limit Number of student must be > " + cur);
                session.setAttribute("typeNoti", "alert-danger");
                response.sendRedirect(request.getContextPath() + "/class?action=list");
                return;
            }
            classIdNumber = Integer.parseInt(classId);
        }

        saveClass.setClassId(classIdNumber);
        saveClass.setLevel(Integer.parseInt(level));
        saveClass.setClassName(className);
        saveClass.setChargeId(Integer.parseInt(chargeTeacherId));
        saveClass.setYear(Integer.parseInt(year));
        saveClass.setLimitNumberOfStudent(limit);
        saveClass.setIsActive(Boolean.parseBoolean(isActive));

        int result = ud.SaveClass(saveClass);
        if (result <= 0) {
            session.setAttribute("notification", "Save Failed. Internal error");
            session.setAttribute("typeNoti", "alert-danger");
        } else {
            session.setAttribute("notification", "Save successfully.");
            session.setAttribute("typeNoti", "alert-success");
        }
        response.sendRedirect(request.getContextPath() + "/class?action=list");
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
