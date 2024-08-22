
package dal.interfaces;

import java.sql.Date;
import java.util.List;
import models.Session;


public interface ISessionDAO {
    public List<Session> filter(int teacherId, java.sql.Date from, java.sql.Date to);

    public Session getSessionById(int sesid);

    public int updateAttendanceChecking(Session ses);

    public List<Session> filterSessionByClassId(int parseInt, Date from, Date to);

    public List<Session> getSessionByClassId(int parseInt);

    public int addSession(Session newSession);

    public int updateSession(Session sessionToUpdate);

    public int deleteSession(int parseInt);

}
