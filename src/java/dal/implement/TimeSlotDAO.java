
package dal.implement;

import dal.interfaces.ITimeSlotDAO;
import models.TimeSlot;
import context.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.User;


public class TimeSlotDAO extends context.DBContext implements ITimeSlotDAO {

    Connection connection = null;
    DBContext dBContext = new DBContext();

    @Override
    public List<TimeSlot> getTimeSlotList() {
        List<TimeSlot> students = new ArrayList<>();

        try {
            connection = dBContext.openConnection();
            String sql = "SELECT * FROM TimeSlot ";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TimeSlot user = new TimeSlot();
                user.setSlotId(rs.getInt("slotId"));
                user.setDescription(rs.getString("description"));
                students.add(user);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                dBContext.closeConnection(connection);
            } catch (SQLException e) {
                Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return students;

    }

    @Override
    public List<TimeSlot> getAvailableSlotsByTeacherAndDate(int supervisorId, String date) {
        List<TimeSlot> availableSlots = new ArrayList<>();

        try {
            connection = dBContext.openConnection();
            String sql = "SELECT ts.slotId, ts.description "
                    + "FROM TimeSlot ts "
                    + "WHERE ts.slotId NOT IN ("
                    + "    SELECT s.slotId "
                    + "    FROM Session s "
                    + "    WHERE s.supervisorId = ? AND s.date = ?"
                    + ")";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, supervisorId);
            ps.setString(2, date);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TimeSlot slot = new TimeSlot();
                slot.setSlotId(rs.getInt("slotId"));
                slot.setDescription(rs.getString("description"));
                availableSlots.add(slot);
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

        return availableSlots;
    }

}
