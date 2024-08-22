
package dal.implement;

import context.DBContext;
import dal.interfaces.IAttendanceDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Attendance;
import models.Session;
import models.User;


public class AttendanceDAO extends context.DBContext implements IAttendanceDAO {

    Connection connection = null;
    DBContext dBContext = new DBContext();

    @Override
    public Attendance getAttendance(int sessionId, int studentId) {
        String sql = "select * from Attendance where sessionId = ? and studentId = ?";

        try {
            connection = openConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, sessionId);
            ps.setInt(2, studentId);
            ResultSet rs = ps.executeQuery();
            Attendance a = new Attendance();
            User student = new UserDAOImpl().getUserById(studentId);
            a.setStudent(student);
            Session s = new SessionDAO().getSessionById(sessionId);
            a.setSession(s);

            if (rs.next()) {
                a.setPresent(rs.getBoolean("isPresent"));
                a.setNote(rs.getString("note"));
                a.setRecordedTime(rs.getString("recorded_time"));
            }
            return a;
        } catch (SQLException e) {

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AttendanceDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
