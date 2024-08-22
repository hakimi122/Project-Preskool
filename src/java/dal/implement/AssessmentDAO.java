
package dal.implement;

import context.DBContext;
import dal.interfaces.IAssessmentDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Assessment;
import models.User;

public class AssessmentDAO extends context.DBContext implements IAssessmentDAO {

    Connection connection = null;
    DBContext dBContext = new DBContext();

    @Override
    public int SaveAssessment(Assessment assess) {
        try {
            connection = dBContext.openConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        PreparedStatement insertOrUpdatePs = null;
        ResultSet rs = null;
        PreparedStatement checkPs = null;

        try {
            connection = dBContext.openConnection();

            // Check if the record already exists
            String checkSql = "SELECT COUNT(*) FROM Assessment WHERE studentId = ? AND [year] = ?";
            checkPs = connection.prepareStatement(checkSql);
            checkPs.setInt(1, assess.getStudentId());
            checkPs.setInt(2, assess.getYear());
            rs = checkPs.executeQuery();

            boolean recordExists = false;
            if (rs.next()) {
                recordExists = rs.getInt(1) > 0;
            }

            // Insert or update based on the existence check
            if (recordExists) {
                String updateSql = "UPDATE Assessment SET status = ?, note = ? WHERE studentId = ? AND [year] = ?";
                insertOrUpdatePs = connection.prepareStatement(updateSql);
                insertOrUpdatePs.setString(1, assess.getStatus());
                insertOrUpdatePs.setString(2, assess.getNote());
                insertOrUpdatePs.setInt(3, assess.getStudentId());
                insertOrUpdatePs.setInt(4, assess.getYear());
            } else {
                String insertSql = "INSERT INTO Assessment(studentId, [year], status, note) VALUES(?, ?, ?, ?)";
                insertOrUpdatePs = connection.prepareStatement(insertSql);
                insertOrUpdatePs.setInt(1, assess.getStudentId());
                insertOrUpdatePs.setInt(2, assess.getYear());
                insertOrUpdatePs.setString(3, assess.getStatus());
                insertOrUpdatePs.setString(4, assess.getNote());
            }

            int count = insertOrUpdatePs.executeUpdate();
            return count;

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        } finally {
            // Close ResultSet, PreparedStatement and Connection in the finally block to ensure they are closed even if an exception occurs
            try {
                if (rs != null) {
                    rs.close();
                }
                if (checkPs != null) {
                    checkPs.close();
                }
                if (insertOrUpdatePs != null) {
                    insertOrUpdatePs.close();
                }
                if (connection != null) {
                    dBContext.closeConnection(connection);
                }
            } catch (SQLException e) {
                Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    @Override
    public List<Assessment> getAssessmentsByClassAndYear(List<User> students, int year) {
        try {
            connection = dBContext.openConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<Assessment> assessments = new ArrayList<>();
        String sql = "SELECT * FROM Assessment WHERE studentId = ? AND [year] = ?";
        try {

            for (User u : students) {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, u.getUserId());
                ps.setInt(2, year);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Assessment assessment = new Assessment();
                    assessment.setStudentId(rs.getInt("studentId"));
                    assessment.setYear(rs.getInt("year"));
                    assessment.setAverageScore(rs.getFloat("averageScore"));
                    assessment.setStatus(rs.getString("status"));
                    assessment.setNote(rs.getString("note"));
                    assessments.add(assessment);
                }
            }

        } catch (SQLException e) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                dBContext.closeConnection(connection);
            } catch (SQLException e) {
                Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return assessments;
    }

    @Override
    public void updateAssessment(int studentId, int year, float score, String note) {
        String status;
        if (score >= 9) {
            status = "Pass";
        } else if (score >= 8) {
            status = "Pass";
        } else if (score >= 7) {
            status = "Pass";
        } else if (score >= 5) {
            status = "Pass";
        } else {
            status = "Not Pass";
        }

        try {
            connection = dBContext.openConnection();

            // Delete existing records with the same studentId and year
            String deleteSql = "DELETE FROM Assessment WHERE studentId = ? AND [year] = ?";
            PreparedStatement deletePs = connection.prepareStatement(deleteSql);
            deletePs.setInt(1, studentId);
            deletePs.setInt(2, year);
            deletePs.executeUpdate();

            // Insert the new record
            String insertSql = "INSERT INTO Assessment(studentId, [year], averageScore, status, note) VALUES(?, ?,?, ?, ?)";
            PreparedStatement insertPs = connection.prepareStatement(insertSql);
            insertPs.setInt(1, studentId);
            insertPs.setInt(2, year);
            insertPs.setFloat(3, score);
            insertPs.setString(4, status);
            insertPs.setString(5, note);
            insertPs.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            Logger.getLogger(AssessmentDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (connection != null) {
                    dBContext.closeConnection(connection);
                }
            } catch (SQLException e) {
                Logger.getLogger(AssessmentDAO.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    @Override
    public Assessment getAssessmentsByStudentAndYear(int studentId, int year) {
        Assessment assessment = null;
        String sql = "SELECT * FROM Assessment WHERE studentId = ? AND [year] = ?";

        try {
            connection = dBContext.openConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, studentId);
            ps.setInt(2, year);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    assessment = new Assessment();
                    assessment.setStudentId(rs.getInt("studentId"));
                    assessment.setYear(rs.getInt("year"));
                    assessment.setAverageScore(rs.getFloat("averageScore"));
                    assessment.setStatus(rs.getString("status"));
                    assessment.setNote(rs.getString("note"));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            Logger.getLogger(AssessmentDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return assessment;
    }

}
