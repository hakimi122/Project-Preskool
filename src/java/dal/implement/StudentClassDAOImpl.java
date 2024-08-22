package dal.implement;

import dal.interfaces.IStudentClassDAO;

import context.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.ClassRoom;
import models.Exam;
import models.StudentClass;
import models.User;

public class StudentClassDAOImpl extends context.DBContext implements IStudentClassDAO {

    Connection connection = null;
    DBContext dBContext = new DBContext();

    /*
    check xem đã có class B nào khác đang active và cùng năm học với class hiện tại, 
	chứa student đó ở trạng thái processing hay chưa
	- Class B phải đang active (tức là lớp đó vẫn đang diễn ra, khi headmaster hay teacher đánh giá hết 
	status của học sinh trong 1 lớp(pass hoăc not pass) thì sẽ chuyển active của 1 class  = false 
	(tức lớp này đã kết thúc trong năm này và không còn hoạt đông)
	- Cùng năm học để đảm bảo là 1 học sinh trong 1 năm chỉ được tồn tại trong 1 class 
	(theo cách tổ chức của cấp 1,2,3.)
     */
    @Override
    public ClassRoom isStudentInClassForYear(String classId, String studentId, String year) {
        try {
            connection = dBContext.openConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sql = "SELECT c.classId,  c.className, c.year from Class c join StudentClass sc "
                + "                on c.classId = sc.classId "
                + "               where  c.isActive = 1 and sc.classId <> ? and sc.studentId = ? "
                + "                and c.year = ? ";

        try {
            connection = dBContext.openConnection();
            // Lấy thông tin class mà sinh viên có studentId hiện tại đang học
            PreparedStatement classPs = connection.prepareStatement(sql);
            classPs.setInt(1, Integer.parseInt(classId));
            classPs.setInt(2, Integer.parseInt(studentId));
            classPs.setInt(3, Integer.parseInt(year));
            ResultSet classRs = classPs.executeQuery();

            if (classRs.next()) {
                ClassRoom c = new ClassRoom();
                c.setClassId(Integer.parseInt(classRs.getString("classId")));
                c.setClassName(classRs.getString("className"));
                c.setYear(Integer.parseInt(classRs.getString("year")));
                return c;
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                dBContext.closeConnection(connection);
            } catch (SQLException e) {
                Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return null;
    }

    @Override
    public int saveStudentToClass(String classId, String studentId) {
        try {
            connection = dBContext.openConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sql = "Insert into StudentClass(studentId, classId) values(?,?) ";

        try {
            connection = dBContext.openConnection();
            // Lấy thông tin class mà sinh viên có studentId hiện tại đang học
            PreparedStatement classPs = connection.prepareStatement(sql);
            classPs.setInt(1, Integer.parseInt(studentId));
            classPs.setInt(2, Integer.parseInt(classId));

            int count = classPs.executeUpdate();
            return count;
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                dBContext.closeConnection(connection);
            } catch (SQLException e) {
                Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return -1;
    }

    @Override
    public int changeClass(String removeClassId, String studentId) {
        try {
            connection = dBContext.openConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sql = "delete from StudentClass where classId =? and studentId =? ";

        try {
            connection = dBContext.openConnection();
            // Lấy thông tin class mà sinh viên có studentId hiện tại đang học
            PreparedStatement classPs = connection.prepareStatement(sql);
            classPs.setInt(1, Integer.parseInt(removeClassId));
            classPs.setInt(2, Integer.parseInt(studentId));

            int count = classPs.executeUpdate();
            return count;
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                dBContext.closeConnection(connection);
            } catch (SQLException e) {
                Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return -1;
    }

    @Override
    public int removeStudentFromClass(String studentId, String classId) {
        try {
            connection = dBContext.openConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sql = "delete from StudentClass where  classId =  ? and studentId = ? ";

        try {
            connection = dBContext.openConnection();
            // Lấy thông tin class mà sinh viên có studentId hiện tại đang học
            PreparedStatement classPs = connection.prepareStatement(sql);
            classPs.setInt(1, Integer.parseInt(classId));
            classPs.setInt(2, Integer.parseInt(studentId));

            int count = classPs.executeUpdate();
            return count;
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                dBContext.closeConnection(connection);
            } catch (SQLException e) {
                Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return -1;
    }

    @Override
    public List<User> getStudentsToAddToClass(String classId_raw) {
        List<User> studentsInClass = new ArrayList<>();
        List<User> studentsToAdd = new ArrayList<>();
        Set<Integer> studentIdsInClass = new HashSet<>();

        try {
            connection = dBContext.openConnection();

            // Step 1: Get list of users already in the class
            String sqlInClass = "SELECT u.userId, u.username, u.password, u.fullName, u.dob, "
                    + "u.gender, u.avatar, u.phone, u.email, u.address, u.role, u.active "
                    + "FROM [User] u "
                    + "INNER JOIN StudentClass sc ON u.userId = sc.studentId "
                    + "WHERE sc.classId = ?";

            PreparedStatement psInClass = connection.prepareStatement(sqlInClass);
            psInClass.setInt(1, Integer.parseInt(classId_raw));
            ResultSet rsInClass = psInClass.executeQuery();

            while (rsInClass.next()) {
                User user = new User();
                user.setUserId(rsInClass.getInt("userId"));
                user.setUsername(rsInClass.getString("username"));
                user.setPassword(rsInClass.getString("password"));
                user.setFullName(rsInClass.getString("fullName"));
                user.setDob(rsInClass.getDate("dob"));
                user.setGender(rsInClass.getString("gender"));
                user.setAvatar(rsInClass.getString("avatar"));
                user.setPhone(rsInClass.getString("phone"));
                user.setEmail(rsInClass.getString("email"));
                user.setAddress(rsInClass.getString("address"));
                user.setRole(rsInClass.getString("role"));
                user.setActive(rsInClass.getBoolean("active"));
                studentsInClass.add(user);
                studentIdsInClass.add(user.getUserId());
            }

            System.out.println(studentIdsInClass.size() + " aa");

            // Step 2: Get list of students who are active and not in the class
            String sqlToAdd = "SELECT u.userId, u.username, u.password, u.fullName, u.dob, "
                    + "u.gender, u.avatar, u.phone, u.email, u.address, u.role, u.active "
                    + "FROM [User] u "
                    + "WHERE u.role = 'student' AND u.active = 1";

            PreparedStatement psToAdd = connection.prepareStatement(sqlToAdd);
            ResultSet rsToAdd = psToAdd.executeQuery();

            while (rsToAdd.next()) {
                int userId = rsToAdd.getInt("userId");
                if (!studentIdsInClass.contains(userId)) {
                    User user = new User();
                    user.setUserId(userId);
                    user.setUsername(rsToAdd.getString("username"));
                    user.setPassword(rsToAdd.getString("password"));
                    user.setFullName(rsToAdd.getString("fullName"));
                    user.setDob(rsToAdd.getDate("dob"));
                    user.setGender(rsToAdd.getString("gender"));
                    user.setAvatar(rsToAdd.getString("avatar"));
                    user.setPhone(rsToAdd.getString("phone"));
                    user.setEmail(rsToAdd.getString("email"));
                    user.setAddress(rsToAdd.getString("address"));
                    user.setRole(rsToAdd.getString("role"));
                    user.setActive(rsToAdd.getBoolean("active"));
                    studentsToAdd.add(user);
                }
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                dBContext.closeConnection(connection);
            } catch (SQLException e) {
                Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return studentsToAdd;
    }

    @Override
    public List<ClassRoom> getClassesStudentStudy(int userId) {
        List<ClassRoom> classes = new ArrayList<>();

        try {
            connection = dBContext.openConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
            return classes; // Return empty list if connection fails
        }

        String sql = "SELECT c.classId, c.className, c.level, c.chargeTeacherId, c.year, c.isActive "
                + "FROM Class c "
                + "INNER JOIN StudentClass sc ON c.classId = sc.classId "
                + "WHERE sc.studentId = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ClassRoom classRoom = new ClassRoom();
                classRoom.setClassId(rs.getInt("classId"));
                classRoom.setClassName(rs.getString("className"));
                classRoom.setLevel(rs.getInt("level"));
                classRoom.setYear(rs.getInt("year"));
                classRoom.setActive(rs.getBoolean("isActive"));

                int chargeTeacherId = rs.getInt("chargeTeacherId");
                User chargeTeacher = new UserDAOImpl().getUserById(chargeTeacherId);
                classRoom.setTeacherInCharge(chargeTeacher);
                classes.add(classRoom);
            }
        } catch (SQLException ex) {
            Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                dBContext.closeConnection(connection);
            } catch (SQLException e) {
                Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        return classes;
    }

    @Override
    public List<User> getStudentsAddToExam(String examId_raw) {
        List<User> studentsToAdd = new ArrayList<>();
        try {
            connection = dBContext.openConnection();

            // Step 1: Get list of users already in the exam
            String sql = "SELECT u.userId, u.username, u.password, u.fullName, u.dob, "
                    + "u.gender, u.avatar, u.phone, u.email, u.address, u.role, u.active "
                    + "FROM [User] u "
                    + "WHERE u.role = 'student' AND u.active = 1";

            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt("userId");

                User user = new User();
                user.setUserId(userId);
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setFullName(rs.getString("fullName"));
                user.setDob(rs.getDate("dob"));
                user.setGender(rs.getString("gender"));
                user.setAvatar(rs.getString("avatar"));
                user.setPhone(rs.getString("phone"));
                user.setEmail(rs.getString("email"));
                user.setAddress(rs.getString("address"));
                user.setRole(rs.getString("role"));
                user.setActive(rs.getBoolean("active"));
                studentsToAdd.add(user);

            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                dBContext.closeConnection(connection);
            } catch (SQLException e) {
                Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return studentsToAdd;
    }

    @Override
    public Exam isStudentInExam(String examDate, String slotId, String studentId) {
        try {
            connection = dBContext.openConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sql = "SELECT c.examId,  c.examType, c.examDate, c.slotId"
                + "  from Exam c join ExamStudent sc "
                + "                on c.examId = sc.examId "
                + "   where   c.examDate = ? and  c.slotId = ? and sc.studentId = ? ";

        try {
            connection = dBContext.openConnection();
            // Lấy thông tin class mà sinh viên có studentId hiện tại đang học
            PreparedStatement classPs = connection.prepareStatement(sql);
            classPs.setDate(1, java.sql.Date.valueOf(examDate));
            classPs.setInt(2, Integer.parseInt(slotId));
            classPs.setInt(3, Integer.parseInt(studentId));
            ResultSet classRs = classPs.executeQuery();

            if (classRs.next()) {
                Exam c = new Exam();
                c.setExamId(classRs.getInt("examId"));
                c.setExamType(classRs.getString("examType"));
                c.setExamDate(classRs.getDate("examDate"));
                c.setSlotId(classRs.getInt("slotId"));
                return c;
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                dBContext.closeConnection(connection);
            } catch (SQLException e) {
                Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return null;

    }

    @Override
    public int saveStudentToExam(Exam c, String studentId, String classId) {
        String deleteExamSQL = "";
        int count = 0;
        if (c != null) {
            deleteExamSQL = "delete from Exam where examId = ?";
        }
        String insertExamStudent = "Insert into ExamStudent(examId,StudentId) values(?,?)";
        try {
            connection = dBContext.openConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            connection = dBContext.openConnection();
            if (!deleteExamSQL.isEmpty()) {
                PreparedStatement classPs = connection.prepareStatement(deleteExamSQL);
                classPs.setInt(1, c.getExamId());
                classPs.executeUpdate();
            }
            PreparedStatement classPs = connection.prepareStatement(insertExamStudent);
            classPs.setInt(1, Integer.parseInt(classId));
            classPs.setInt(2, Integer.parseInt(studentId));
            count = classPs.executeUpdate();

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                dBContext.closeConnection(connection);
            } catch (SQLException e) {
                Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return count;
    }

    @Override
    public List<StudentClass> getStudentClassScore(int classId) {
        List<StudentClass> studentClasses = new ArrayList<>();
        String sql = "SELECT u.userId, u.username, u.password, u.fullName, u.dob, "
                + "u.gender, u.avatar, u.phone, u.email, u.address, u.role, u.active, "
                + "c.classId, c.className, c.level, c.chargeTeacherId, c.year, c.isActive,"
                + " sc.scoreSemester1, sc.scoreSemester2 "
                + " FROM [User] u "
                + "JOIN StudentClass sc ON u.userId = sc.studentId "
                + "JOIN Class c ON sc.classId = c.classId "
                + "WHERE c.classId = ?";

        try {
            connection = dBContext.openConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, classId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User student = new User();
                student.setUserId(rs.getInt("userId"));
                student.setFullName(rs.getString("fullName"));
                student.setDob(rs.getDate("dob"));
                student.setEmail(rs.getString("email"));

                ClassRoom classRoom = new ClassRoom();
                classRoom.setClassId(rs.getInt("classId"));
                classRoom.setClassName(rs.getString("className"));

                int chargeTeacherId = rs.getInt("chargeTeacherId");
                User chargeTeacher = new UserDAOImpl().getUserById(chargeTeacherId);
                classRoom.setTeacherInCharge(chargeTeacher);

                StudentClass studentClass = new StudentClass();
                studentClass.setClassrom(classRoom);
                studentClass.setStudent(student);
                studentClass.setScoreSemester1(rs.getFloat("scoreSemester1"));
                studentClass.setScoreSemester2(rs.getFloat("scoreSemester2"));

                studentClasses.add(studentClass);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                dBContext.closeConnection(connection);
            } catch (SQLException e) {
                Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return studentClasses;
    }

    @Override
    public int updateScore(int classId, int studentId, float s1, float s2) {
        try {
            connection = dBContext.openConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }

        String sql = "UPDATE StudentClass SET scoreSemester1 = ?, scoreSemester2 = ? WHERE classId = ? AND studentId = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setFloat(1, s1);
            ps.setFloat(2, s2);
            ps.setInt(3, classId);
            ps.setInt(4, studentId);

            int count = ps.executeUpdate();
            return count;
        } catch (SQLException ex) {
            Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                dBContext.closeConnection(connection);
            } catch (SQLException e) {
                Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return -1;
    }

    @Override
    public StudentClass getScore(int classId, int studentId) {
        StudentClass sc = null;
        try {
            connection = dBContext.openConnection();
            String sql = "SELECT * FROM StudentClass WHERE classId = ? AND studentId = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, classId);
            ps.setInt(2, studentId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                sc = new StudentClass();
                sc.setClassId(rs.getInt("classId"));
                sc.setStudentId(rs.getInt("studentId"));
                sc.setScoreSemester1(rs.getFloat("scoreSemester1"));
                sc.setScoreSemester2(rs.getFloat("scoreSemester2"));
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                dBContext.closeConnection(connection);
            } catch (SQLException e) {
                Logger.getLogger(StudentClassDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return sc;
    }

}
