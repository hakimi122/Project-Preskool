
package dal.interfaces;

import models.Attendance;


public interface IAttendanceDAO {
    public Attendance getAttendance(int sessionId, int studentId);
}
