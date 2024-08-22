
package dal.interfaces;

import java.util.List;
import models.TimeSlot;

public interface ITimeSlotDAO {
    public List<TimeSlot> getTimeSlotList();

    public List<TimeSlot> getAvailableSlotsByTeacherAndDate(int supervisorId, String date);
}
