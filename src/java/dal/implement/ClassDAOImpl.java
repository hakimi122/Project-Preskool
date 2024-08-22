package dal.implement;

import dal.interfaces.IClassDAO;
import context.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.ClassRoom;
import models.User;

public class ClassDAOImpl extends context.DBContext implements IClassDAO {

    Connection connection = null;
    DBContext dBContext = new DBContext();

    @Override
    /**
     * Hàm này dùng để dùng để lấy danh sách các lớp học đang hoạt động, cùng
     * với thông tin về giáo viên phụ trách và danh sách học sinh của mỗi lớp.
     */
    public List<ClassRoom> getClassList() {
        //Tạo ra một ArrayList đe luu thong tin danh sach lop hoc
        List<ClassRoom> classes = new ArrayList<>();
        //Câu truy vấn dùng để lấy thông tin của lớp học dang active và giáo viên phụ trách cua lop do.
        String classSql = "SELECT c.classId, c.className,c.chargeTeacherId, c.level, c.year, c.limitNumberOfStudent, c.isActive, "
                + "u.userId, u.username, u.password, u.fullName, u.dob, u.gender, u.avatar, "
                + "u.phone, u.email, u.address, u.role, u.active "
                + "FROM Class c "
                + "JOIN [User] u ON u.userId = c.chargeTeacherId WHERE c.isActive = 1";
        //Câu truy vấn dùng để Lấy thông tin của tat ca học sinh trong một lớp học dựa trên classId.
        String studentSql = "SELECT s.userId, s.username, s.password, s.fullName, s.dob, s.gender, "
                + "s.avatar, s.phone, s.email, s.address, s.role, s.active "
                + "FROM StudentClass sc "
                + "JOIN [User] s ON s.userId = sc.studentId "
                + "WHERE sc.classId = ?";

        try {
            connection = openConnection();//Mở kết nối với cơ sở dữ liệu.
            //Thực hiện truy vấn lấy thông tin lớp học
            PreparedStatement classPs = connection.prepareStatement(classSql);
            //Thực thi truy vấn và lưu kết quả vào array classPs.
            ResultSet classRs = classPs.executeQuery();

            while (classRs.next()) {
                //Tạo một Object ClassRoom mới và add thông tin của từng bản ghi vào(classRs)
                ClassRoom classObj = new ClassRoom();
                classObj.setClassId(classRs.getInt("classId"));
                classObj.setClassName(classRs.getString("className"));
                classObj.setLevel(classRs.getInt("level"));
                classObj.setYear(classRs.getInt("year"));
                classObj.setChargeId(classRs.getInt("chargeTeacherId"));
                classObj.setLimitNumberOfStudent(classRs.getInt("limitNumberOfStudent"));
                classObj.setActive(classRs.getBoolean("isActive"));

                //Tạo một User mới(role: teacher) và add thông tin của từng bản ghi vào
                User teacher = new User();//giáo viên phụ trách
                teacher.setUserId(classRs.getInt("userId"));
                teacher.setUsername(classRs.getString("username"));
                teacher.setPassword(classRs.getString("password"));
                teacher.setFullName(classRs.getString("fullName"));
                teacher.setDob(classRs.getDate("dob"));
                teacher.setGender(classRs.getString("gender"));
                teacher.setAvatar(classRs.getString("avatar"));
                teacher.setPhone(classRs.getString("phone"));
                teacher.setEmail(classRs.getString("email"));
                teacher.setAddress(classRs.getString("address"));
                teacher.setRole(classRs.getString("role"));
                teacher.setActive(classRs.getBoolean("active"));

                //add User teacher vừa tạo vào trường TeacherInCharge của Object ClassRoom classObj
                classObj.setTeacherInCharge(teacher);

                // Get students for the class
                PreparedStatement studentPs = connection.prepareStatement(studentSql);
                //Gán giá trị classId cho truy vấn
                studentPs.setInt(1, classObj.getClassId());
                //Thực thi truy vấn và lưu kết quả vào studentRs
                ResultSet studentRs = studentPs.executeQuery();

                //Tạo một list chứa các student từ truy vấn
                List<User> students = new ArrayList<>();
                while (studentRs.next()) {
                    User student = new User();
                    //Gán các giá trị từ studentRs vào các thuộc tính của từng bản ghi User.
                    student.setUserId(studentRs.getInt("userId"));
                    student.setUsername(studentRs.getString("username"));
                    student.setPassword(studentRs.getString("password"));
                    student.setFullName(studentRs.getString("fullName"));
                    student.setDob(studentRs.getDate("dob"));
                    student.setGender(studentRs.getString("gender"));
                    student.setAvatar(studentRs.getString("avatar"));
                    student.setPhone(studentRs.getString("phone"));
                    student.setEmail(studentRs.getString("email"));
                    student.setAddress(studentRs.getString("address"));
                    student.setRole(studentRs.getString("role"));
                    student.setActive(studentRs.getBoolean("active"));

                    students.add(student);
                }
                //add list of student vào thuộc tính List<User> students của Object ClassRoom classObj
                classObj.setStudents(students);

                //add ClassRoom : classObj vừa được tạo ra vào List of ClassRoom: classes
                classes.add(classObj);
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
        return classes;//return List of ClassRoom
    }

    @Override
    /**
     *
     */
    public ClassRoom getClassById(int classId) {
        //Object classObj để chứa thông tin của lớp học.
        ClassRoom classObj = null;
        //Câu truy vấn dùng để lấy thông tin của lớp học và giáo viên phụ trách cua lop voi classId truyen vao
        String classSql = "SELECT c.classId, c.isActive, c.className, c.level, c.year, c.limitNumberOfStudent, "
                + "u.userId, u.username, u.password, u.fullName, u.dob, u.gender, u.avatar, "
                + "u.phone, u.email, u.address, u.role, u.active "
                + "FROM Class c "
                + "JOIN [User] u ON u.userId = c.chargeTeacherId "
                + "WHERE c.classId = ?";
        //Câu truy vấn dùng để Lấy thông tin của tat ca học sinh trong một lớp học dựa trên classId.
        String studentSql = "SELECT s.userId, s.username, s.password, s.fullName, s.dob, s.gender, "
                + "s.avatar, s.phone, s.email, s.address, s.role, s.active "
                + "FROM StudentClass sc "
                + "JOIN [User] s ON s.userId = sc.studentId "
                + "WHERE sc.classId = ?";

        try {
            connection = openConnection();
            PreparedStatement classPs = connection.prepareStatement(classSql);
            classPs.setInt(1, classId);
            //luu ket qua truy van vao trong classRs
            ResultSet classRs = classPs.executeQuery();

            if (classRs.next()) {
                classObj = new ClassRoom();//khoi tao Object classObj
                //Thiết lập các thuộc tính của ClassRoom từ kết quả truy vấn classRs
                classObj.setClassId(classRs.getInt("classId"));
                classObj.setClassName(classRs.getString("className"));
                classObj.setLevel(classRs.getInt("level"));
                classObj.setYear(classRs.getInt("year"));
                classObj.setLimitNumberOfStudent(classRs.getInt("limitNumberOfStudent"));
                classObj.setActive(classRs.getBoolean("isActive"));

                User teacher = new User();//giao vien phu trach
                teacher.setUserId(classRs.getInt("userId"));
                teacher.setUsername(classRs.getString("username"));
                teacher.setPassword(classRs.getString("password"));
                teacher.setFullName(classRs.getString("fullName"));
                teacher.setDob(classRs.getDate("dob"));
                teacher.setGender(classRs.getString("gender"));
                teacher.setAvatar(classRs.getString("avatar"));
                teacher.setPhone(classRs.getString("phone"));
                teacher.setEmail(classRs.getString("email"));
                teacher.setAddress(classRs.getString("address"));
                teacher.setRole(classRs.getString("role"));
                teacher.setActive(classRs.getBoolean("active"));

                //Assign Object User for attitude teacherInCharge of ClassRoom.
                classObj.setTeacherInCharge(teacher);

                // Get students for the class
                PreparedStatement studentPs = connection.prepareStatement(studentSql);
                studentPs.setInt(1, classId);
                ResultSet studentRs = studentPs.executeQuery();//luu ket qua truy van vao studentRs

                List<User> students = new ArrayList<>();//tao list of student
                while (studentRs.next()) {
                    User student = new User();//khoi tao Object student
                    student.setUserId(studentRs.getInt("userId"));
                    student.setUsername(studentRs.getString("username"));
                    student.setPassword(studentRs.getString("password"));
                    student.setFullName(studentRs.getString("fullName"));
                    student.setDob(studentRs.getDate("dob"));
                    student.setGender(studentRs.getString("gender"));
                    student.setAvatar(studentRs.getString("avatar"));
                    student.setPhone(studentRs.getString("phone"));
                    student.setEmail(studentRs.getString("email"));
                    student.setAddress(studentRs.getString("address"));
                    student.setRole(studentRs.getString("role"));
                    student.setActive(studentRs.getBoolean("active"));

                    students.add(student);//add student to list
                }
                //add Arraylist Student vừa tạo vào trường students của Object ClassRoom classObj
                classObj.setStudents(students);
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
        return classObj;
    }

    @Override
    /**
     * This function use to get class information and teacher in charge by
     * teacherId Return a ClassRoom with teacher in charge
     */
    public ClassRoom isCharged(int teacherId) {
        try {
            connection = dBContext.openConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ClassDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        //lấy thông tin lớp học và giáo viên phụ trách dựa trên teacherId.
        String sql = "SELECT c.classId, c.className, c.level, c.year, c.limitNumberOfStudent, "
                + "u.userId, u.username, u.password, u.fullName, u.dob, u.gender, u.avatar, "
                + "u.phone, u.email, u.address, u.role, u.active "
                + "FROM Class c "
                + "JOIN [User] u ON u.userId = c.chargeTeacherId "
                + "WHERE role='teacher' and c.chargeTeacherId = ?";

        //Khai báo đối tượng ClassRoom để chứa thông tin lớp học.
        ClassRoom classObj = null;

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, teacherId);
            ResultSet rs = ps.executeQuery();//lưu trữ kết quả truy vấn vào 'rs'

            if (rs.next()) {
                classObj = new ClassRoom();
                classObj.setClassId(rs.getInt("classId"));
                classObj.setClassName(rs.getString("className"));
                classObj.setLevel(rs.getInt("level"));
                classObj.setYear(rs.getInt("year"));
                classObj.setLimitNumberOfStudent(rs.getInt("limitNumberOfStudent"));

                User teacher = new User();//teacher in charge
                teacher.setUserId(rs.getInt("userId"));
                teacher.setUsername(rs.getString("username"));
                teacher.setPassword(rs.getString("password"));
                teacher.setFullName(rs.getString("fullName"));
                teacher.setDob(rs.getDate("dob"));
                teacher.setGender(rs.getString("gender"));
                teacher.setAvatar(rs.getString("avatar"));
                teacher.setPhone(rs.getString("phone"));
                teacher.setEmail(rs.getString("email"));
                teacher.setAddress(rs.getString("address"));
                teacher.setRole(rs.getString("role"));
                teacher.setActive(rs.getBoolean("active"));

                classObj.setTeacherInCharge(teacher);
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

        return classObj;
    }

    @Override
    public List<User> getRegularTeacherList(int teacherId) {
        List<User> regularTeachers = new ArrayList<>();
        try {
            connection = dBContext.openConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ClassDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        //lấy thông tin giáo viên không phụ trách lớp học
        String sql = "SELECT * \n"
                + "    FROM [User] u \n"
                + "    LEFT JOIN Class c ON u.userId = c.chargeTeacherId \n"
                + "   WHERE role= 'teacher' and u.active = 1  AND c.chargeTeacherId IS NULL";

        if (teacherId != -1) {
            sql += " and  u.userId != ? ";
        }

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            if (teacherId != -1) {
                ps.setInt(1, teacherId);
            }
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User teacher = new User();
                teacher.setUserId(rs.getInt("userId"));
                teacher.setUsername(rs.getString("username"));
                teacher.setPassword(rs.getString("password"));
                teacher.setFullName(rs.getString("fullName"));
                teacher.setDob(rs.getDate("dob"));
                teacher.setGender(rs.getString("gender"));
                teacher.setAvatar(rs.getString("avatar"));
                teacher.setPhone(rs.getString("phone"));
                teacher.setEmail(rs.getString("email"));
                teacher.setAddress(rs.getString("address"));
                teacher.setRole(rs.getString("role"));
                teacher.setActive(rs.getBoolean("active"));

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
    public int SaveClass(ClassRoom updateClass) {
        int numberOfChange = 0;
        try {
            connection = dBContext.openConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "";
        if (updateClass.getClassId() != -1) { //update
            sql = "Update Class set level = ?, className = ?, chargeTeacherId = ?, year = ?, limitNumberOfStudent =?, isActive = ? where classId = ?";
        } else {
            sql = "Insert into Class(level, className, chargeTeacherId, year, limitNumberOfStudent,isActive) values(?,?,?,?,?,?)";
        }
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            int count = 0;
            st.setInt(++count, updateClass.getLevel());
            st.setString(++count, updateClass.getClassName());
            st.setInt(++count, updateClass.getChargeId());
            st.setInt(++count, updateClass.getYear());
            st.setInt(++count, updateClass.getLimitNumberOfStudent());
            st.setBoolean(++count, updateClass.IsActive());
            if (updateClass.getClassId() != -1) {
                st.setInt(++count, updateClass.getClassId());
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
    public int DeleteClass(int classId) {
        int numberOfChange = 0;
        try {
            connection = dBContext.openConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "UPDATE Class SET isActive = 0 WHERE classId = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, classId);
            numberOfChange = st.executeUpdate();
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
    public List<ClassRoom> getClassInCharge(int userId) {
        List<ClassRoom> classes = new ArrayList<>();
        Connection connection = null;

        String sql = "SELECT c.classId, c.className, c.level, c.year, c.limitNumberOfStudent, c.isActive, "
                + "u.userId, u.username, u.password, u.fullName, u.dob, u.gender, u.avatar, "
                + "u.phone, u.email, u.address, u.role, u.active "
                + "FROM Class c "
                + "JOIN [User] u ON u.userId = c.chargeTeacherId "
                + "WHERE u.userId = ? AND c.isActive = 1";

        String studentSql = "SELECT s.userId, s.username, s.password, s.fullName, s.dob, s.gender, "
                + "s.avatar, s.phone, s.email, s.address, s.role, s.active "
                + "FROM StudentClass sc "
                + "JOIN [User] s ON s.userId = sc.studentId "
                + "WHERE sc.classId = ?";
        try {
            connection = dBContext.openConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ClassRoom classObj = new ClassRoom();
                classObj.setClassId(rs.getInt("classId"));
                classObj.setClassName(rs.getString("className"));
                classObj.setLevel(rs.getInt("level"));
                classObj.setYear(rs.getInt("year"));
                classObj.setLimitNumberOfStudent(rs.getInt("limitNumberOfStudent"));
                classObj.setActive(rs.getBoolean("isActive"));

                User teacher = new User();
                teacher.setUserId(rs.getInt("userId"));
                teacher.setUsername(rs.getString("username"));
                teacher.setPassword(rs.getString("password"));
                teacher.setFullName(rs.getString("fullName"));
                teacher.setDob(rs.getDate("dob"));
                teacher.setGender(rs.getString("gender"));
                teacher.setAvatar(rs.getString("avatar"));
                teacher.setPhone(rs.getString("phone"));
                teacher.setEmail(rs.getString("email"));
                teacher.setAddress(rs.getString("address"));
                teacher.setRole(rs.getString("role"));
                teacher.setActive(rs.getBoolean("active"));

                classObj.setTeacherInCharge(teacher);

                // Get students for the class
                PreparedStatement studentPs = connection.prepareStatement(studentSql);
                studentPs.setInt(1, classObj.getClassId());
                ResultSet studentRs = studentPs.executeQuery();

                List<User> students = new ArrayList<>();
                while (studentRs.next()) {
                    User student = new User();
                    student.setUserId(studentRs.getInt("userId"));
                    student.setUsername(studentRs.getString("username"));
                    student.setPassword(studentRs.getString("password"));
                    student.setFullName(studentRs.getString("fullName"));
                    student.setDob(studentRs.getDate("dob"));
                    student.setGender(studentRs.getString("gender"));
                    student.setAvatar(studentRs.getString("avatar"));
                    student.setPhone(studentRs.getString("phone"));
                    student.setEmail(studentRs.getString("email"));
                    student.setAddress(studentRs.getString("address"));
                    student.setRole(studentRs.getString("role"));
                    student.setActive(studentRs.getBoolean("active"));

                    students.add(student);
                }

                classObj.setStudents(students);
                classes.add(classObj);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ClassDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                dBContext.closeConnection(connection);
            } catch (SQLException e) {
                Logger.getLogger(ClassDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return classes;
    }

    @Override
    public Boolean isClassNameExisted(String className, int year, int classId) {
        boolean exists = false;
        String sql = "SELECT 1 FROM Class WHERE className = ? AND year = ? AND classId <> ?";

        try {
            connection = dBContext.openConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, className);
            ps.setInt(2, year);
            ps.setInt(3, classId); // Add this line to set the classId parameter
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                exists = true;
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ClassDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                dBContext.closeConnection(connection);
            } catch (SQLException e) {
                Logger.getLogger(ClassDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return exists;
    }

}
