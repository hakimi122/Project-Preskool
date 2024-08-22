
package dal.interfaces;

import java.util.List;
import models.ClassRoom;
import models.Exam;
import models.StudentClass;
import models.User;

public interface IStudentClassDAO {

    public ClassRoom isStudentInClassForYear(String classId, String studentId, String year);

    public int saveStudentToClass(String classId, String studentId);

    public int changeClass(String removeClassId, String studentId);

    public int removeStudentFromClass(String studentId, String classId);

    public List<User> getStudentsToAddToClass(String classId_raw);

    public List<ClassRoom> getClassesStudentStudy(int userId);

    public List<User> getStudentsAddToExam(String classId_raw);

    public Exam isStudentInExam(String examDate, String slotId, String studentId);

    public int saveStudentToExam(Exam c, String studentId, String classId);

    public List<StudentClass> getStudentClassScore(int parseInt);

    public int updateScore(int classId, int studentId, float s1, float s2);

    public StudentClass getScore(int classId, int studentId);
}
