
package dal.interfaces;

import java.util.List;
import models.Subject;
import models.User;

public interface ISubjectDAO {

    public List<Subject> getSubjectList();

    public Subject getSubjectById(int subject);

    public int DeleteSubject(int subjectId);

    public int SaveSubject(Subject subjectSave);

    public int AssignSubjectsToTeacher(int teacherId, int[] subjectId);

    public List<User> getTeachersBySubject(int subjectId);
}
