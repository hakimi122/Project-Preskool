
package dal.interfaces;

import java.util.List;
import models.Assessment;
import models.User;

public interface IAssessmentDAO {

    public int SaveAssessment(Assessment assess);

    public List<Assessment> getAssessmentsByClassAndYear(List<User> students, int year);

    public void updateAssessment( int parseInt0,int year, float score, String note);

    public Assessment getAssessmentsByStudentAndYear(int studentId, int classId);
}
