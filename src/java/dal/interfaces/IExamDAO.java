
package dal.interfaces;

import java.util.List;
import models.Exam;
import models.User;

public interface IExamDAO {
    public List<Exam> getExamList();
    public List<Exam> getStudentExam(int studentId);
    public int saveExam(Exam exam);
    public int deleteExam(int examId);
    public Exam getExamById(int examId);
    public List<User> getRegularTeacherList(java.util.Date date, int slotId);

    public List<Exam> getExamOfStudent(int parseInt);

    public List<Exam> getExamOfTeacher(int userId);
}
