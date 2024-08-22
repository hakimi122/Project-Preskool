
package dal.implement;

import context.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import dal.interfaces.IReportingDAO;
import java.util.ArrayList;
import java.util.List;
import models.Reporting;


public class ReportingDAOImpl extends context.DBContext implements IReportingDAO {

    Connection connection = null;
    DBContext dBContext = new DBContext();

    public int getTotalStudents() {
        int totalStudents = 0;
        try {
            connection = dBContext.openConnection();
            String sql = "SELECT COUNT(*) FROM [User] WHERE role = 'student' AND active = 1";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                totalStudents = rs.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ReportingDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                dBContext.closeConnection(connection);
            } catch (SQLException e) {
                Logger.getLogger(ReportingDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return totalStudents;
    }

    public int getTotalTeachers() {
        int totalTeachers = 0;
        try {
            connection = dBContext.openConnection();
            String sql = "SELECT COUNT(*) FROM [User] WHERE role = 'teacher' AND active = 1";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                totalTeachers = rs.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ReportingDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                dBContext.closeConnection(connection);
            } catch (SQLException e) {
                Logger.getLogger(ReportingDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return totalTeachers;
    }

    public int getTotalClasses() {
        int totalClasses = 0;
        try {
            connection = dBContext.openConnection();
            String sql = "SELECT COUNT(*) FROM [Class] WHERE isActive = 1";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                totalClasses = rs.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ReportingDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                dBContext.closeConnection(connection);
            } catch (SQLException e) {
                Logger.getLogger(ReportingDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return totalClasses;
    }

    public int getTotalMaterial() {
        int totalMaterial = 0;
        try {
            connection = dBContext.openConnection();
            String sql = "SELECT COUNT(*) FROM [Material]";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                totalMaterial = rs.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ReportingDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                dBContext.closeConnection(connection);
            } catch (SQLException e) {
                Logger.getLogger(ReportingDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return totalMaterial;
    }

    @Override
    public Reporting getReporting() {
        Reporting reporting = new Reporting();
        reporting.setTotalClasses(this.getTotalClasses());
        reporting.setTotalStudents(this.getTotalStudents());
        reporting.setTotalMaterial(this.getTotalMaterial());
        reporting.setTotalTeachers(this.getTotalTeachers());
        return reporting;
    }

    @Override
    public List<Reporting.StudentAnalysis> getStudentAnalysis() {
        List<Reporting.StudentAnalysis> studentAnalysis = new ArrayList<>();

        try {
            connection = dBContext.openConnection();
            String sql = "SELECT "
                    + "Assessment.studentId ,"
                    + "Assessment.year ,Assessment.status "
                    + ",Assessment.note "
                    + ", studentClass.classId"
                    + ", Class.className "
                    + "FROM [Assessment]  "
                    + "left join [StudentClass]  on Assessment.studentId = StudentClass.studentId "
                    + "left join Class on Class.classId = StudentClass.classId";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reporting.StudentAnalysis analysis = new Reporting.StudentAnalysis();
                analysis.setClassId(rs.getInt("classId"));
                analysis.setClassName(rs.getString("className"));
                analysis.setNote(rs.getString("note"));
                analysis.setStatus(rs.getString("status"));
                analysis.setStudentId(rs.getInt("studentId"));
                analysis.setYear(rs.getInt("year"));
                studentAnalysis.add(analysis);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(TimeSlotDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                dBContext.closeConnection(connection);
            } catch (SQLException e) {
                Logger.getLogger(TimeSlotDAO.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        return studentAnalysis;
    }

    @Override
    public List<Reporting.StudentAnalysis> getStudentStar() {
        List<Reporting.StudentAnalysis> studentAnalysis = new ArrayList<>();

        try {
            connection = dBContext.openConnection();
            String sql = "SELECT [User].username, Assessment.studentId ,Assessment.year ,Assessment.status ,Assessment.note, studentClass.classId, Class.className FROM [Assessment]  \n"
                    + "left join [StudentClass]  on Assessment.studentId = StudentClass.studentId \n"
                    + "left join Class on Class.classId = StudentClass.classId\n"
                    + "left join [User] on Assessment.studentId = [User].userId where Assessment.rank like 'Very Good'";

            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reporting.StudentAnalysis analysis = new Reporting.StudentAnalysis();
                analysis.setClassId(rs.getInt("classId"));
                analysis.setUsername(rs.getString("username"));
                analysis.setClassName(rs.getString("className"));
                analysis.setNote(rs.getString("note"));
                analysis.setStatus(rs.getString("status"));
                analysis.setStudentId(rs.getInt("studentId"));
                analysis.setYear(rs.getInt("year"));
                studentAnalysis.add(analysis);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(TimeSlotDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                dBContext.closeConnection(connection);
            } catch (SQLException e) {
                Logger.getLogger(TimeSlotDAO.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        return studentAnalysis;
    }

}
