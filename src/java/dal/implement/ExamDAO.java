
package dal.implement;

import dal.interfaces.IExamDAO;
import models.Exam;
import context.DBContext;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Subject;
import models.TimeSlot;
import models.User;


public class ExamDAO extends context.DBContext implements IExamDAO {

    Connection connection = null;
    DBContext dBContext = new DBContext();

    @Override
    public List<Exam> getExamList() {
        List<Exam> exams = new ArrayList();
        String sql = "select e.examId, e.examType, e.subjectId, s.SubjectName, "
                + " e.teacherId, t.fullName, e.slotId, sl.description, e.examDate, e.room "
                + " from Exam e join Subject s on e.subjectId = s.subjectId"
                + " join [User] t on e.teacherId = t.userId"
                + " join TimeSlot sl on e.slotId = sl.slotId";
        try {
            connection = openConnection();
            PreparedStatement classPs = connection.prepareStatement(sql);
            ResultSet classRs = classPs.executeQuery();

            while (classRs.next()) {
                Exam e = new Exam();
                e.setExamId(classRs.getInt("examId"));
                e.setExamType(classRs.getString("examType"));
                e.setExamDate(classRs.getDate("examDate"));
                e.setRoom(classRs.getString("room"));

                Subject s = new Subject();
                s.setSubjectId(classRs.getInt("subjectId"));
                s.setSubjectName(classRs.getString("subjectName"));

                User teacher = new User();
                teacher.setUserId(classRs.getInt("teacherId"));
                teacher.setFullName(classRs.getString("fullName"));

                TimeSlot sl = new TimeSlot();
                sl.setSlotId(classRs.getInt("slotId"));
                sl.setDescription(classRs.getString("description"));

                e.setSlot(sl);
                e.setSubject(s);
                e.setTeacher(teacher);

                exams.add(e);
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
        return exams;
    }

    @Override
    public List<Exam> getStudentExam(int studentId) {
        List<Exam> exams = new ArrayList();
        String sql = "select e.examId, e.examType, e.subjectId, s.SubjectName, "
                + " e.teacherId, t.fullName, e.slotId, sl.description, e.examDate, e.room ,"
                + " from ExamStudent es "
                + " join Exam e on es.examId = e.examId"
                + " join Subject s on e.subjectId = s.subjectId"
                + " join [User] t on e.teacherId = t.userId"
                + " join TimeSlot sl on e.slotId = sl.slotId "
                + " where es.studentId = ?";
        try {
            connection = openConnection();
            PreparedStatement classPs = connection.prepareStatement(sql);
            classPs.setInt(1, studentId);
            ResultSet classRs = classPs.executeQuery();

            while (classRs.next()) {
                Exam e = new Exam();
                e.setExamId(classRs.getInt("examId"));
                e.setExamType(classRs.getString("examType"));
                e.setExamDate(classRs.getDate("examDate"));
                e.setRoom(classRs.getString("room"));

                Subject s = new Subject();
                s.setSubjectId(classRs.getInt("subjectId"));
                s.setSubjectName(classRs.getString("subjectName"));

                User teacher = new User();
                teacher.setUserId(classRs.getInt("teacherId"));
                teacher.setFullName(classRs.getString("fullName"));

                TimeSlot sl = new TimeSlot();
                sl.setSlotId(classRs.getInt("slotId"));
                sl.setDescription(classRs.getString("description"));

                e.setSlot(sl);
                e.setSubject(s);
                e.setTeacher(teacher);

                exams.add(e);
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
        return exams;
    }

    @Override
    public int saveExam(Exam exam) {
        int numberOfChange = 0;
        try {
            connection = dBContext.openConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "";
        if (exam.getExamId() != -1) { //update
            sql = "Update Exam set examType = ?, subjectId = ?, teacherId = ?, slotId = ?, examDate =?, room = ? where examId = ?";
        } else {
            sql = "Insert into Exam(examType, subjectId, teacherId, slotId, examDate,room) values(?,?,?,?,?,?)";
        }

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            int count = 0;
            st.setString(++count, exam.getExamType());
            st.setInt(++count, exam.getSubjectId());
            st.setInt(++count, exam.getTeacherId());
            st.setInt(++count, exam.getSlotId());
            st.setDate(++count, (java.sql.Date) exam.getExamDate());
            st.setString(++count, exam.getRoom());
            if (exam.getExamId() != -1) {
                st.setInt(++count, exam.getExamId());
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
    public int deleteExam(int examId) {
        int numberOfChange = 0;
        try {
            connection = dBContext.openConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sqlDeleteExamStudent = "Delete from ExamStudent where examId = ?";
        String sqlDeleteExam = "Delete from Exam where examId = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sqlDeleteExamStudent);
            st.setInt(1, examId);
            numberOfChange = st.executeUpdate();

            st = connection.prepareStatement(sqlDeleteExam);
            st.setInt(1, examId);
            numberOfChange += st.executeUpdate();
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
    public Exam getExamById(int examId) {
        String sql = "select e.examId, e.examType, e.subjectId, s.SubjectName, "
                + " e.teacherId, t.fullName, e.slotId, sl.description, e.examDate, e.room "
                + " from Exam e join Subject s on e.subjectId = s.subjectId"
                + " join [User] t on e.teacherId = t.userId"
                + " join TimeSlot sl on e.slotId = sl.slotId where examId = ?";

        String studentSql = "SELECT es.studentId, u.fullName, u.dob, u.gender, u.email, u.phone  "
                + "FROM ExamStudent es "
                + "JOIN [User] u ON es.StudentId = u.userId "
                + "WHERE es.examId = ?";
        try {
            connection = openConnection();
            PreparedStatement classPs = connection.prepareStatement(sql);
            classPs.setInt(1, examId);
            ResultSet classRs = classPs.executeQuery();

            if (classRs.next()) {
                Exam e = new Exam();
                e.setExamId(classRs.getInt("examId"));
                e.setExamType(classRs.getString("examType"));
                e.setExamDate(classRs.getDate("examDate"));
                e.setRoom(classRs.getString("room"));

                Subject s = new Subject();
                s.setSubjectId(classRs.getInt("subjectId"));
                s.setSubjectName(classRs.getString("subjectName"));

                User teacher = new User();
                teacher.setUserId(classRs.getInt("teacherId"));
                teacher.setFullName(classRs.getString("fullName"));

                TimeSlot sl = new TimeSlot();
                sl.setSlotId(classRs.getInt("slotId"));
                sl.setDescription(classRs.getString("description"));

                e.setSlot(sl);
                e.setSubject(s);
                e.setTeacher(teacher);

                PreparedStatement studentPs = connection.prepareStatement(studentSql);
                studentPs.setInt(1, examId);
                ResultSet studentRs = studentPs.executeQuery();

                List<User> students = new ArrayList<>();
                while (studentRs.next()) {
                    User student = new User();
                    student.setUserId(studentRs.getInt("studentId"));
                    student.setFullName(studentRs.getString("fullName"));
                    student.setDob(studentRs.getDate("dob"));
                    student.setGender(studentRs.getString("gender"));
                    student.setEmail(studentRs.getString("email"));
                    student.setPhone(studentRs.getString("phone"));
                    students.add(student);
                }
                e.setStudents(students);

                return e;
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
    public List<User> getRegularTeacherList(java.util.Date date, int slotId) {
        List<User> regularTeachers = new ArrayList<>();
        try {
            connection = dBContext.openConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ClassDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Truy vấn để lấy các giáo viên đã có trong kết quả ban đầu
        String sqlExistingTeachers = "SELECT u.userId "
                + "FROM [User] u "
                + "JOIN Exam c ON u.userId = c.teacherId "
                + "WHERE role = 'teacher' AND u.active = 1 "
                + "AND (c.examDate = ? AND c.slotId = ?)";

// Truy vấn để lấy các giáo viên khác với các giáo viên từ kết quả trên
        String sqlOtherTeachers = "SELECT u.userId, u.fullName "
                + "FROM [User] u "
                + "WHERE role = 'teacher' AND u.active = 1 "
                + "AND u.userId NOT IN (" + sqlExistingTeachers + ")";
        try {
            PreparedStatement ps = connection.prepareStatement(sqlOtherTeachers);
            int count = 0;
            ps.setDate(++count, new java.sql.Date(date.getTime()));
            ps.setInt(++count, slotId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User teacher = new User();
                teacher.setUserId(rs.getInt("userId"));
                teacher.setFullName(rs.getString("fullName"));
                regularTeachers.add(teacher);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClassDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                dBContext.closeConnection(connection);
            } catch (SQLException e) {
                Logger.getLogger(ClassDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return regularTeachers;
    }

    @Override
    public List<Exam> getExamOfStudent(int studentId) {
        List<Exam> exams = new ArrayList<>();
        String sql = "SELECT e.examId, e.examType, e.subjectId, s.SubjectName, "
                + "e.teacherId, t.fullName, e.slotId, sl.description, e.examDate, e.room "
                + "FROM ExamStudent es "
                + "JOIN Exam e ON es.examId = e.examId "
                + "JOIN Subject s ON e.subjectId = s.subjectId "
                + "JOIN [User] t ON e.teacherId = t.userId "
                + "JOIN TimeSlot sl ON e.slotId = sl.slotId "
                + "WHERE es.studentId = ?";
        try {
            connection = openConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Exam exam = new Exam();
                exam.setExamId(rs.getInt("examId"));
                exam.setExamType(rs.getString("examType"));
                exam.setExamDate(rs.getDate("examDate"));
                exam.setRoom(rs.getString("room"));

                Subject subject = new Subject();
                subject.setSubjectId(rs.getInt("subjectId"));
                subject.setSubjectName(rs.getString("subjectName"));

                User teacher = new User();
                teacher.setUserId(rs.getInt("teacherId"));
                teacher.setFullName(rs.getString("fullName"));

                TimeSlot timeSlot = new TimeSlot();
                timeSlot.setSlotId(rs.getInt("slotId"));
                timeSlot.setDescription(rs.getString("description"));

                exam.setSlot(timeSlot);
                exam.setSubject(subject);
                exam.setTeacher(teacher);

                exams.add(exam);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ExamDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                closeConnection(connection);
            } catch (SQLException e) {
                Logger.getLogger(ExamDAO.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return exams;
    }

    @Override
    public List<Exam> getExamOfTeacher(int userId) {
        List<Exam> exams = new ArrayList<>();
        String sql = "SELECT e.examId, e.examType, e.subjectId, s.SubjectName, "
                + "e.teacherId, t.fullName, e.slotId, sl.description, e.examDate, e.room "
                + "From Exam e "
                + "JOIN Subject s ON e.subjectId = s.subjectId "
                + "JOIN [User] t ON e.teacherId = t.userId "
                + "JOIN TimeSlot sl ON e.slotId = sl.slotId "
                + "WHERE e.teacherId = ?";
        try {
            connection = openConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Exam exam = new Exam();
                exam.setExamId(rs.getInt("examId"));
                exam.setExamType(rs.getString("examType"));
                exam.setExamDate(rs.getDate("examDate"));
                exam.setRoom(rs.getString("room"));

                Subject subject = new Subject();
                subject.setSubjectId(rs.getInt("subjectId"));
                subject.setSubjectName(rs.getString("subjectName"));

                User teacher = new User();
                teacher.setUserId(rs.getInt("teacherId"));
                teacher.setFullName(rs.getString("fullName"));

                TimeSlot timeSlot = new TimeSlot();
                timeSlot.setSlotId(rs.getInt("slotId"));
                timeSlot.setDescription(rs.getString("description"));

                exam.setSlot(timeSlot);
                exam.setSubject(subject);
                exam.setTeacher(teacher);

                exams.add(exam);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ExamDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                closeConnection(connection);
            } catch (SQLException e) {
                Logger.getLogger(ExamDAO.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return exams;
    }
}
