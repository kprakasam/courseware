package edu.sjsu.courseware.controller;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import edu.sjsu.courseware.AssignmentCourse;
import edu.sjsu.courseware.dao.AssignmentDAO;
import edu.sjsu.courseware.dao.CourseDAO;

@Controller
public class Home {
    @Inject
    CourseDAO courseDAO;
    
    @Inject
    AssignmentDAO assignmentDao;
    
    @RequestMapping("home")
    public String home(ModelAndView model) {
        return "home";
    }
    
    @RequestMapping("search/{type}/{term}")
    @ResponseBody
    public Set<String> search(@PathVariable String type, @PathVariable String term) {
        if ("course-code".equalsIgnoreCase(type))
            return courseDAO.getCourseIdsByCodeStartingWith(term).keySet();

        if ("course-name".equalsIgnoreCase(type))
            return courseDAO.getCourseIdsByNamesStartingWith(term).keySet();
        
        if ("assignment-name".equalsIgnoreCase(type))
            return assignmentDao.getAssignmentIdsByNamesStartingWith(term).keySet();
         
        return Collections.emptySet();
    }

    @RequestMapping(value="fetch/{type}/{term}", produces="application/json")
    @ResponseBody
    public AssignmentsJson fetch(@PathVariable String type, @PathVariable String term) {
         if ("course-code".equalsIgnoreCase(type))
           return toJsonObject(courseDAO.getAssignmentsByCourseCode(term));            

        if ("course-name".equalsIgnoreCase(type))
            return toJsonObject(courseDAO.getAssignmentsByCourseName(term));            
        
        if ("assignment-name".equalsIgnoreCase(type))
            return toJsonObject(assignmentDao.getAssignmentsByName(term));            
         
        return new AssignmentsJson();
    }

    private AssignmentsJson toJsonObject(List<AssignmentCourse> assignments) {
        AssignmentsJson assignmentsJson = new AssignmentsJson();
        
        if (assignments == null || assignments.isEmpty())
            return assignmentsJson;
        
        assignmentsJson.total = String.valueOf((assignments.size() % 10 == 0) ?  assignments.size() / 10 : assignments.size() / 10 + 1);
        assignmentsJson.records =  String.valueOf(assignments.size());
        assignmentsJson.rows = new AssignmentJson[assignments.size()];
        
        for (AssignmentCourse assignmentCourse : assignments) {
            AssignmentJson assignmentJson = new AssignmentJson();
            assignmentJson.id = String.valueOf(assignmentCourse.getId());
            assignmentJson.cell = new String[5];
            assignmentJson.cell[0] = assignmentCourse.getCourseCode();
            assignmentJson.cell[1] = assignmentCourse.getCourseName();
            assignmentJson.cell[2] = assignmentCourse.getName();
            assignmentJson.cell[3] = assignmentCourse.getExternalTool();
            assignmentJson.cell[4] = assignmentCourse.getCanvasInstance();
                    
        }
        
        return assignmentsJson;
    }

    static public class AssignmentJson {
        String id;
        String[] cell;
    }
    
    static  public class AssignmentsJson {
        String page = "0";
        String total = "0";
        String records = "0";
        AssignmentJson[] rows = new AssignmentJson[]{};
    }
}
