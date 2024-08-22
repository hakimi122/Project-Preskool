package dal.implement;

import context.DBContext;
import dal.interfaces.ISessionDAO;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import models.Session;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Attendance;
import models.ClassRoom;
import models.Subject;
import models.TimeSlot;
import models.User;

public class SessionDAO extends context.DBContext implements ISessionDAO {

    Connection connection = null;

    @Override
    public List<Session> filter(int teacherId, Date from, Date to) {
        List<Session> sessions = new ArrayList<>();
        String sql = """
                     SELECT ses.sessionId, ses.[date], ses.[index],ses.room, ses.isAttanded 
                     \t,l.userId,l.fullName
                     \t,g.classId,g.className
                     \t,sub.subjectId,sub.subjectName
                     \t,t.slotId,t.[description]
                     FROM [Session] ses 
                     \t\t\tINNER JOIN [User] l ON l.userId = ses.supervisorId
                     \t\t\tINNER JOIN [Class] g ON g.classId = ses.classId
                     \t\t\tINNER JOIN [Subject] sub ON sub.subjectId = ses.subjectId
                     \t\t\tINNER JOIN TimeSlot t ON t.slotId = ses.slotId
                     WHERE
                     l.userId = ?
                     AND ses.[date] >= ?
                     AND ses.[date] <= ?""";
        try {
            connection = openConnection();
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, teacherId);
            stm.setDate(2, from);
            stm.setDate(3, to);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Session session = new Session();
                User l = new User();
                ClassRoom g = new ClassRoom();
                TimeSlot t = new TimeSlot();
                Subject sub = new Subject();

                session.setSessionId(rs.getInt("sessionId"));
                session.setDate(rs.getDate("date"));
                session.setIndex(rs.getInt("index"));
                session.setRoom(rs.getString("room"));
                session.setIsAttended(rs.getBoolean("isAttanded"));

                l.setUserId(rs.getInt("userId"));
                l.setFullName(rs.getString("fullName"));
                session.setTeacher(l);

                g.setClassId(rs.getInt("classId"));
                g.setClassName(rs.getString("className"));
                session.setClassRoom(g);

                sub.setSubjectId(rs.getInt("subjectId"));
                sub.setSubjectName(rs.getString("subjectName"));
                session.setSubject(sub);

                t.setSlotId(rs.getInt("slotId"));
                t.setDescription(rs.getString("description"));
                session.setTimeSlot(t);

                sessions.add(session);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(SessionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sessions;
    }

    @Override
    public Session getSessionById(int sesid) {
        String sql = """
                      SELECT ses.sessionId, ses.[index],ses.date,ses.room,ses.isAttanded    
                                          \t,g.classId,g.className , g.year   
                                          \t,t.slotId, t.[description]     
                                          \t,l.userId as supervisorId,l.fullName  as  lname 
                                          \t,sub.subjectId, sub.subjectName    
                                          \t,s.userId studentId,s.fullName studentName    
                                         \t,ISNULL(a.isPresent,1) isPresent, ISNULL(a.note,'') note, a.recorded_time     
                                         \t\tFROM [Session] ses    
                                          \tINNER JOIN TimeSlot t ON t.slotId = ses.slotId    
                                          \tINNER JOIN [User] l ON l.userId = ses.supervisorId    
                                          \tINNER JOIN Class g ON g.classId = ses.classId    
                                         \t\tINNER JOIN [Subject] sub ON sub.subjectId = ses.subjectId   
                                           \tleft JOIN [StudentClass] sc ON g.classId = sc.classId   
                                          \t\tleft JOIN [User] s ON s.userId = sc.studentId    
                                          \t\tLEFT JOIN Attendance a ON s.userId = a.studentId AND ses.sessionId = a.sessionId    
                                          WHERE ses.sessionId = ?""";
        try {

            connection = openConnection();
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, sesid);
            ResultSet rs = stm.executeQuery();
            Session ses = null;
            while (rs.next()) {
                if (ses == null) {
                    ses = new Session();
                    ses.setSessionId(rs.getInt("sessionId"));

                    TimeSlot t = new TimeSlot();
                    t.setSlotId(rs.getInt("slotId"));
                    t.setDescription(rs.getString("description"));
                    ses.setTimeSlot(t);

                    User l = new User();
                    l.setUserId(rs.getInt("supervisorId"));
                    l.setFullName(rs.getString("lname"));
                    ses.setTeacher(l);

                    ClassRoom g = new ClassRoom();
                    g.setClassId(rs.getInt("classId"));
                    g.setClassName(rs.getString("className"));
                    g.setYear(rs.getInt("year"));
                    ses.setClassRoom(g);

                    Subject sub = new Subject();
                    sub.setSubjectId(rs.getInt("subjectId"));
                    sub.setSubjectName(rs.getString("subjectName"));
                    ses.setSubject(sub);

                    ses.setDate(rs.getDate("date"));
                    ses.setIndex(rs.getInt("index"));
                    ses.setRoom(rs.getString("room"));
                    ses.setIsAttended(rs.getBoolean("isAttanded"));
                }
                //read student
                if (rs.getInt("studentId") != 0) {
                    User s = new User();
                    s.setUserId(rs.getInt("studentId"));
                    s.setFullName(rs.getString("studentName"));
                    //read attandance
                    Attendance a = new Attendance();
                    a.setStudent(s);
                    a.setSession(ses);
                    a.setPresent(rs.getBoolean("isPresent"));
                    a.setNote(rs.getString("note"));
                    a.setRecordedTime(rs.getString("recorded_time"));
                    ses.getAttendances().add(a);
                }

            }
            return ses;
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(SessionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public int updateAttendanceChecking(Session model) {
        String sql = "UPDATE Session SET isAttanded = 1 WHERE sessionId = ?";
        int totalRecordsChanged = 0;
        try {
            connection = openConnection();
            connection.setAutoCommit(false);
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, model.getSessionId());
            totalRecordsChanged += stm.executeUpdate();

            //remove old attandances
            sql = "DELETE Attendance WHERE sessionId = ? ";
            PreparedStatement stm_delete = connection.prepareStatement(sql);
            stm_delete.setInt(1, model.getSessionId());

            totalRecordsChanged += stm_delete.executeUpdate();

            //insert new attandances
            for (Attendance att : model.getAttendances()) {
                sql = """
                      INSERT INTO [Attendance]
                                 ([sessionId]
                                 ,[studentId]
                                 ,[isPresent]
                                 ,[note]
                                 ,[recorded_time])
                           VALUES
                                 (?
                                 ,?
                                 ,?
                                 ,?
                                 ,GETDATE())""";
                PreparedStatement stm_insert = connection.prepareStatement(sql);
                stm_insert.setInt(1, model.getSessionId());
                stm_insert.setInt(2, att.getStudent().getUserId());
                stm_insert.setBoolean(3, att.isPresent());
                stm_insert.setString(4, att.getNote());
                totalRecordsChanged += stm_insert.executeUpdate();
            }
            connection.commit();
            return totalRecordsChanged;
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(SessionDAO.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(SessionDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SessionDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                Logger.getLogger(SessionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return -1;
    }

    @Override
    public List<Session> filterSessionByClassId(int parseInt, Date from, Date to) {
        List<Session> sessions = new ArrayList<>();
        String sql = """
                     SELECT ses.sessionId, ses.[date], ses.[index],ses.room, ses.isAttanded 
                     \t,l.userId,l.fullName
                     \t,g.classId,g.className
                     \t,sub.subjectId,sub.subjectName
                     \t,t.slotId,t.[description]
                     FROM [Session] ses 
                     \t\t\tINNER JOIN [User] l ON l.userId = ses.supervisorId
                     \t\t\tINNER JOIN [Class] g ON g.classId = ses.classId
                     \t\t\tINNER JOIN [Subject] sub ON sub.subjectId = ses.subjectId
                     \t\t\tINNER JOIN TimeSlot t ON t.slotId = ses.slotId
                     WHERE
                     ses.classId = ?
                     AND ses.[date] >= ?
                     AND ses.[date] <= ?""";
        try {
            connection = openConnection();
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, parseInt);
            stm.setDate(2, from);
            stm.setDate(3, to);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Session session = new Session();
                User l = new User();
                ClassRoom g = new ClassRoom();
                TimeSlot t = new TimeSlot();
                Subject sub = new Subject();

                session.setSessionId(rs.getInt("sessionId"));
                session.setDate(rs.getDate("date"));
                session.setIndex(rs.getInt("index"));
                session.setRoom(rs.getString("room"));
                session.setIsAttended(rs.getBoolean("isAttanded"));

                l.setUserId(rs.getInt("userId"));
                l.setFullName(rs.getString("fullName"));
                session.setTeacher(l);

                g.setClassId(rs.getInt("classId"));
                g.setClassName(rs.getString("className"));
                session.setClassRoom(g);

                sub.setSubjectId(rs.getInt("subjectId"));
                sub.setSubjectName(rs.getString("subjectName"));
                session.setSubject(sub);

                t.setSlotId(rs.getInt("slotId"));
                t.setDescription(rs.getString("description"));
                session.setTimeSlot(t);

                sessions.add(session);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(SessionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sessions;
    }

    @Override
    public List<Session> getSessionByClassId(int classId) {
        List<Session> sessions = new ArrayList<>();
        String sql = """
                 SELECT ses.sessionId, ses.[date], ses.[index],ses.room, ses.isAttanded 
                     ,l.userId, l.fullName
                     ,g.classId, g.className
                     ,sub.subjectId, sub.subjectName
                     ,t.slotId, t.[description]
                 FROM [Session] ses 
                     INNER JOIN [User] l ON l.userId = ses.supervisorId
                     INNER JOIN [Class] g ON g.classId = ses.classId
                     INNER JOIN [Subject] sub ON sub.subjectId = ses.subjectId
                     INNER JOIN TimeSlot t ON t.slotId = ses.slotId
                 WHERE ses.classId = ?""";
        try {
            connection = openConnection();
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, classId);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Session session = new Session();
                User l = new User();
                ClassRoom g = new ClassRoom();
                TimeSlot t = new TimeSlot();
                Subject sub = new Subject();

                session.setSessionId(rs.getInt("sessionId"));
                session.setDate(rs.getDate("date"));
                session.setIndex(rs.getInt("index"));
                session.setRoom(rs.getString("room"));
                session.setIsAttended(rs.getBoolean("isAttanded"));

                l.setUserId(rs.getInt("userId"));
                l.setFullName(rs.getString("fullName"));
                session.setTeacher(l);

                g.setClassId(rs.getInt("classId"));
                g.setClassName(rs.getString("className"));
                session.setClassRoom(g);

                sub.setSubjectId(rs.getInt("subjectId"));
                sub.setSubjectName(rs.getString("subjectName"));
                session.setSubject(sub);

                t.setSlotId(rs.getInt("slotId"));
                t.setDescription(rs.getString("description"));
                session.setTimeSlot(t);

                sessions.add(session);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(SessionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sessions;
    }

    @Override
    public int addSession(Session newSession) {
        int rowsAffected = 0;
        String sql = """
                 INSERT INTO [Session]
                        ([date],  isAttanded, supervisorId, classId, subjectId, slotId, room)
                 VALUES (?,  ?, ?, ?, ?, ?,?)
                 """;
        try {
            connection = openConnection();
            PreparedStatement stm = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            stm.setDate(1, newSession.getDate());
            stm.setBoolean(2, newSession.isIsAttended());
            stm.setInt(3, newSession.getSupervisorId());
            stm.setInt(4, newSession.getClassId());
            stm.setInt(5, newSession.getSubjectId());
            stm.setInt(6, newSession.getSlotId());
            stm.setString(7, newSession.getRoom());
            rowsAffected = stm.executeUpdate();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(SessionDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(SessionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return rowsAffected;
    }

    @Override
    public int updateSession(Session sessionToUpdate) {
        int rowsAffected = 0;
        String sql = """
                 UPDATE [Session]
                 SET [date] = ?, isAttanded = ?, supervisorId = ?, classId = ?, subjectId = ?, slotId = ?, room = ?
                 WHERE sessionId = ?
                 """;
        try {
            connection = openConnection();
            PreparedStatement stm = connection.prepareStatement(sql);

            stm.setDate(1, sessionToUpdate.getDate());
            stm.setBoolean(2, sessionToUpdate.isIsAttended());
            stm.setInt(3, sessionToUpdate.getSupervisorId());
            stm.setInt(4, sessionToUpdate.getClassId());
            stm.setInt(5, sessionToUpdate.getSubjectId());
            stm.setInt(6, sessionToUpdate.getSlotId());
            stm.setString(7, sessionToUpdate.getRoom());
            stm.setInt(8, sessionToUpdate.getSessionId());

            rowsAffected = stm.executeUpdate();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(SessionDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(SessionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return rowsAffected;
    }

    @Override
    public int deleteSession(int parseInt) {
        String sql = "DELETE FROM [Session] WHERE sessionId = ?";
        int rowsAffected = 0;
        try {
            connection = openConnection();
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, parseInt);
            rowsAffected = stm.executeUpdate();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(SessionDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(SessionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return rowsAffected;
    }

}
