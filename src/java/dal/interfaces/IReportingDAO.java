
package dal.interfaces;

import java.util.List;
import models.Reporting;
import models.User;


public interface IReportingDAO { 
    public Reporting getReporting();      
    
    public List<Reporting.StudentAnalysis> getStudentAnalysis();      
    public List<Reporting.StudentAnalysis> getStudentStar();      
    
}
