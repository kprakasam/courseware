package edu.sjsu.courseware.controller;

import java.util.Collections;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import edu.sjsu.courseware.dao.AssignmentDAO;
import edu.sjsu.courseware.dao.CourseDAO;
import edu.sjsu.courseware.dao.JarDAO;
import edu.sjsu.courseware.json.AssignmentsGridData;
import edu.sjsu.courseware.json.JarsGridData;

@Controller
public class Home {
    @Inject
    JarDAO jarDAO;

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

    @RequestMapping(value="fetch-assignments/{type}/{term}", produces="application/json")
    @ResponseBody
    public AssignmentsGridData fetchAssignments(@PathVariable String type, @PathVariable String term) {
         if ("course-code".equalsIgnoreCase(type))
           return AssignmentsGridData.toAssignmentsGridData(courseDAO.getAssignmentsByCourseCode(term));            

        if ("course-name".equalsIgnoreCase(type))
            return AssignmentsGridData.toAssignmentsGridData(courseDAO.getAssignmentsByCourseName(term));            
        
        if ("assignment-name".equalsIgnoreCase(type))
            return AssignmentsGridData.toAssignmentsGridData(assignmentDao.getAssignmentsByName(term));            
         
        return new AssignmentsGridData();
    }

    @RequestMapping(value="fetch-jars/{id}", produces="application/json")
    @ResponseBody
    public JarsGridData fetchJars(@PathVariable long id) {
        return JarsGridData.toJarsGridData(jarDAO.getJars(id));
    }
    
    @RequestMapping(value="delete-jar/{id}", produces="application/json")
    @ResponseBody
    public Boolean deleteJar(@PathVariable long id) {
        return jarDAO.delete(id);  
    }
       
}
