
package dal.implement;

import context.DBContext;
import dal.interfaces.ISubjectDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Subject;
import models.User;

public class SubjectDAO extends context.DBContext implements ISubjectDAO {

    Connection connection = null;
    DBContext dBContext = new DBContext();

    @Override
    public List<Subject> getSubjectList() {
        List<Subject> subjects = new ArrayList<>();
        String classSql = "select * from Subject where isTeaching = 1";

        try {
            connection = openConnection();
            PreparedStatement classPs = connection.prepareStatement(classSql);
            ResultSet classRs = classPs.executeQuery();

            while (classRs.next()) {
                Subject classObj = new Subject();
                classObj.setSubjectId(classRs.getInt("subjectId"));
                classObj.setSubjectName(classRs.getString("subjectName"));
                classObj.setIsTeaching(classRs.getBoolean("isTeaching"));

                subjects.add(classObj);
            }
            return subjects;
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ClassDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                closeConnection(connection);
            } catch (SQLException e) {
                Logger.getLogger(ClassDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return null;
    }

    @Override
    public Subject getSubjectById(int subject) {
        String sql = "Select * from Subject where subjectId = ?";

        try {
            connection = openConnection();
            PreparedStatement classPs = connection.prepareStatement(sql);
            classPs.setInt(1, subject);
            ResultSet classRs = classPs.executeQuery();

            if (classRs.next()) {
                Subject subjectGet = new Subject();
                subjectGet.setSubjectId(classRs.getInt("subjectId"));
                subjectGet.setSubjectName(classRs.getString("subjectName"));
                subjectGet.setIsTeaching(classRs.getBoolean("isTeaching"));
                return subjectGet;
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ClassDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                closeConnection(connection);
            } catch (SQLException e) {
                Logger.getLogger(ClassDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return null;
    }

    @Override
    public int DeleteSubject(int subjectId) {
        int numberOfChange = 0;
        try {
            connection = dBContext.openConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "Update Subject set isTeaching = 0 WHERE subjectId = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, subjectId);
            numberOfChange = st.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                dBContext.closeConnection(connection);
            } catch (SQLException e) {
            }
        }
        return numberOfChange;
    }

    @Override
    public int SaveSubject(Subject subjectSave) {
        int numberOfChange = 0;
        try {
            connection = dBContext.openConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "";
        if (subjectSave.getSubjectId() != -1) { //update
            sql = "Update Subject set subjectName = ?, isTeaching = ? where subjectId = ?";
        } else {
            sql = "Insert into Subject(subjectName,isTeaching) values(?,?)";
        }
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            int count = 0;
            st.setString(++count, subjectSave.getSubjectName());
            st.setBoolean(++count, subjectSave.isIsTeaching());

            if (subjectSave.getSubjectId() != -1) {
                st.setInt(++count, subjectSave.getSubjectId());
            }
            numberOfChange = st.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                dBContext.closeConnection(connection);
            } catch (SQLException e) {
                Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return numberOfChange;
    }

    @Override
    public int AssignSubjectsToTeacher(int teacherId, int[] subjectIds) {
        int numberOfChanges = 0;
        try {
            connection = dBContext.openConnection();

            // Bắt đầu transaction
            connection.setAutoCommit(false);

            // Xóa các bản ghi hiện có của giáo viên trong bảng TeacherSubject
            String deleteSql = "DELETE FROM TeacherSubject WHERE teacherId = ?";
            PreparedStatement deleteSt = connection.prepareStatement(deleteSql);
            deleteSt.setInt(1, teacherId);
            deleteSt.executeUpdate();

            // Thêm các bản ghi mới vào bảng TeacherSubject
            String insertSql = "INSERT INTO TeacherSubject(teacherId, subjectId) VALUES(?, ?)";
            PreparedStatement insertSt = connection.prepareStatement(insertSql);
            for (int subjectId : subjectIds) {
                insertSt.setInt(1, teacherId);
                insertSt.setInt(2, subjectId);
                numberOfChanges += insertSt.executeUpdate();
            }
            // Commit transaction
            connection.commit();

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
            try {
                // Rollback transaction nếu có lỗi xảy ra
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException e) {
                Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        } finally {
            try {
                dBContext.closeConnection(connection);
            } catch (SQLException e) {
                Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return numberOfChanges;
    }

    @Override
    public List<User> getTeachersBySubject(int subjectId) {
        String sql = "Select * from TeacherSubject ts join [User] u on ts.teacherId = u.userId"
                + " where ts.subjectId = ?";
        List<User> teachers = new ArrayList();
        try {
            connection = openConnection();
            PreparedStatement classPs = connection.prepareStatement(sql);
            classPs.setInt(1, subjectId);
            ResultSet rs = classPs.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("userId"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setFullName(rs.getString("fullName"));
                user.setDob(rs.getDate("dob"));
                user.setGender(rs.getString("gender"));
                user.setPhone(rs.getString("phone"));
                user.setEmail(rs.getString("email"));
                user.setAddress(rs.getString("address"));
                user.setRole(rs.getString("role"));
                user.setActive(rs.getBoolean("active"));

                teachers.add(user);
            }
            return teachers;
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ClassDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                closeConnection(connection);
            } catch (SQLException e) {
                Logger.getLogger(ClassDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return null;
    }
}
