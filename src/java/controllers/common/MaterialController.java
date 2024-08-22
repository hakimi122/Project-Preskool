
package controllers.common;

import dal.implement.MaterialDAO;
import dal.implement.SubjectDAO;
import dal.interfaces.IMaterialDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import models.Material;
import models.Subject;
import models.User;

@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50) // 50MB
public class MaterialController extends HttpServlet {
    // Thư mục để lưu trữ các tệp đã tải lên

    public static int BUFFER_SIZE = 1024 * 100;
    public static final String UPLOAD_DIR = "resources/uploadFiles";

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
            out.println("<title>Servlet MaterialController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet MaterialController at " + request.getContextPath() + "</h1>");
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
        User u = (User) session.getAttribute("user");
        String action = request.getParameter("action");
        IMaterialDAO ud = new MaterialDAO();
        if (action == null || action.equals("list")) {
            List<Material> students = ud.getMaterialListForRole(u.getRole());
            request.setAttribute("materials", students);
            request.getRequestDispatcher("./views/material-list.jsp").forward(request, response);
        } else if (action.equals("add")) {
            List<Subject> subjects = new SubjectDAO().getSubjectList();
            request.setAttribute("subjects", subjects);
            request.getRequestDispatcher("./views/material-add.jsp").forward(request, response);
        } else if (action.equals("delete")) {
            String userId_raw = request.getParameter("materialId");
            int userId = Integer.parseInt(userId_raw);
            int result = ud.DeleteMaterial(userId);
            if (result <= 0) {
                session.setAttribute("notification", "delete failed. internal error");
                session.setAttribute("typeNoti", "alert-danger");
                response.sendRedirect(request.getContextPath() + "/material?action=list");
                return;
            }
            session.setAttribute("notification", "delete success.");
            session.setAttribute("typeNoti", "alert-success");
            response.sendRedirect(request.getContextPath() + "/material?action=list");

        } else if (action.equals("edit")) {
            List<Subject> subjects = new SubjectDAO().getSubjectList();
            request.setAttribute("subjects", subjects);
            String userId_editRaw = request.getParameter("materialId");
            int userId = Integer.parseInt(userId_editRaw);
            Material userEdit = ud.getMaterialById(userId);
            request.setAttribute("materialEdit", userEdit);
            request.getRequestDispatcher("./views/material-edit.jsp").forward(request, response);
        } else if ("download".equals(action)) {
            String fileName = request.getParameter("fileName");

            String applicationPath = getServletContext().getRealPath("");
            String downloadPath = applicationPath + File.separator + UPLOAD_DIR;
            String filePath = downloadPath + File.separator + fileName;
            System.out.println(fileName);
            System.out.println(filePath);
            System.out.println("fileName:" + fileName);
            System.out.println("filePath :" + filePath);
            File file = new File(filePath);
            OutputStream outStream = null;
            FileInputStream inputStream = null;

            if (file.exists()) {
                String mimeType = "application/octet-stream";
                response.setContentType(mimeType);

                String headerKey = "Content-Disposition";
                String headerValue = String.format("attachment; filename=\"%s\"", file.getName());
                response.setHeader(headerKey, headerValue);

                try {
                    outStream = response.getOutputStream();
                    inputStream = new FileInputStream(file);
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int bytesRead = -1;

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, bytesRead);
                    }
                } catch (IOException ioExObj) {
                    System.out.println("Exception While Performing The I/O Operation?= " + ioExObj.getMessage());
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }

                    outStream.flush();
                    if (outStream != null) {
                        outStream.close();
                    }
                }

                // Thiết lập thông báo và chuyển hướng sau khi tải xuống
                session.setAttribute("notification", "Download success to your downloads folder.");
                session.setAttribute("typeNoti", "alert-success");
                response.sendRedirect(request.getContextPath() + "/material?action=list");
            } else {
                session.setAttribute("notification", "File does not existed.");
                session.setAttribute("typeNoti", "alert-warning");
                response.sendRedirect(request.getContextPath() + "/material?action=list");
            }
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

        IMaterialDAO md = new MaterialDAO();

        String materialId_raw = request.getParameter("materialId");

        String subjectName = request.getParameter("materialName");
        String materialDes = request.getParameter("materialDescription");
        String materialForRole = request.getParameter("materialForRole");
        String subjectId_Raw = request.getParameter("subjectId");
        Material materialSave = new Material();
        if (materialId_raw == null || materialId_raw.isEmpty()) {
            materialSave.setMaterialId(-1);
        } else {
            materialSave.setMaterialId(Integer.parseInt(materialId_raw));
        }
        materialSave.setMaterialName(subjectName);
        materialSave.setMaterialForRole(materialForRole);
        materialSave.setSubjectId(Integer.parseInt(subjectId_Raw));
        materialSave.setMaterialDescription(materialDes);
        User u = (User) session.getAttribute("user");
        materialSave.setCreatedById(u.getUserId());

        // Handle file upload
        Part filePart = request.getPart("materialUrl");

        if (filePart != null && filePart.getSize() > 0) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String mimeType = filePart.getContentType();

            // Chỉ cho phép tải lên ảnh, file Word, file Excel
            if (mimeType.equals("image/jpeg") || mimeType.equals("image/png") || mimeType.equals("image/gif")
                    || mimeType.equals("application/msword") || mimeType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                    || mimeType.equals("application/vnd.ms-excel")
                    || mimeType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    || mimeType.equals("text/plain")) {

                String uploadPath = request.getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;//for netbeans use this code

                File dir = new File(uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                // Save the file in the directory
                String filePath = UPLOAD_DIR + File.separator + fileName;
                InputStream is = filePart.getInputStream();
                Files.copy(is, Paths.get(uploadPath + File.separator + fileName), StandardCopyOption.REPLACE_EXISTING);
                // Ensure unique file name by adding UUID if file already exists

                // Save the file path to the database
                materialSave.setFileName(fileName);

            } else {
                session.setAttribute("notification", "File type not supported. Please upload an image, Word or Excel file.");
                session.setAttribute("typeNoti", "alert-danger");
                response.sendRedirect(request.getContextPath() + "/material?action=add");
                return;
            }

        } else {
            Material m = md.getMaterialById(Integer.parseInt(materialId_raw));
            materialSave.setFileName(m.getFileName());
        }

        if (materialId_raw == null || materialId_raw.isEmpty()) {
            materialSave.setMaterialId(-1);
        } else {
            materialSave.setMaterialId(Integer.parseInt(materialId_raw));
        }

        int count = md.SaveMaterial(materialSave);
        if (count > 0) {
            session.setAttribute("notification", "save material success.");
            session.setAttribute("typeNoti", "alert-success");
            response.sendRedirect(request.getContextPath() + "/material?action=list");
            return;
        }
        session.setAttribute("notification", "save material failed. internal error");
        session.setAttribute("typeNoti", "alert-danger");
        response.sendRedirect(request.getContextPath() + "/material?action=list");
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
