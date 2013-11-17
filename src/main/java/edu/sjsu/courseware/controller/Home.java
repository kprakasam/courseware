package edu.sjsu.courseware.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import edu.sjsu.courseware.dao.AssignmentDAO;
import edu.sjsu.courseware.dao.CourseDAO;
import edu.sjsu.courseware.dao.JarDAO;
import edu.sjsu.courseware.json.Assignment;
import edu.sjsu.courseware.json.Jar;
import edu.sjsu.courseware.json.UploadedFile;

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
    public Assignment[] fetchAssignments(@PathVariable String type, @PathVariable String term) {
         if ("course-code".equalsIgnoreCase(type))
           return Assignment.toAssignment(courseDAO.getAssignmentsByCourseCode(term));            

        if ("course-name".equalsIgnoreCase(type))
            return Assignment.toAssignment(courseDAO.getAssignmentsByCourseName(term));            
        
        if ("assignment-name".equalsIgnoreCase(type))
            return Assignment.toAssignment(assignmentDao.getAssignmentsByName(term));            
         
        return new Assignment[] {};
    }

    @RequestMapping(value="fetch-jars/{id}", produces="application/json")
    @ResponseBody
    public Jar[] fetchJars(@PathVariable long id) {
        return Jar.toJars(jarDAO.getJars(id));
    }
    
    @RequestMapping(value="delete-jar/{id}", produces="application/json")
    @ResponseBody
    public Boolean deleteJar(@PathVariable long id) {
        return jarDAO.delete(id);  
    }
    
    @RequestMapping(value="/upload-jar", method=RequestMethod.POST)
    public @ResponseBody UploadedFile upload(@RequestParam("file") MultipartFile file, MultipartHttpServletRequest request) throws IOException {      
 
        edu.sjsu.courseware.Jar jar = new edu.sjsu.courseware.Jar();
        String mainclass = request.getParameter("mainclass").toString();
        Long assignmentId = Long.valueOf(request.getParameter("assignmentId").toString());
        
        jar.setMainClass(mainclass.toString());
        jar.setAssignmentId(assignmentId);
        jar.setName(file.getOriginalFilename());
        jar = jarDAO.insert(jar, file.getBytes());
        
        if (jar == null)
            return new UploadedFile(-1, false, file);
        
        if (mainclass != null && !mainclass.isEmpty())
            jarDAO.updatePreviousMainClass(jar.getId(), assignmentId);
        
        return new UploadedFile(jar.getId(), true, file);
    }
}
