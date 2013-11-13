package edu.sjsu.courseware.controller;

import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.Marshaller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import edu.sjsu.courseware.Assignment;
import edu.sjsu.courseware.Course;
import edu.sjsu.courseware.ExternalTool;
import edu.sjsu.courseware.InvalidConsumerException;
import edu.sjsu.courseware.Jar;
import edu.sjsu.courseware.OAuthSignatureVerificationFailException;
import edu.sjsu.courseware.dao.AssignmentDAO;
import edu.sjsu.courseware.dao.CourseDAO;
import edu.sjsu.courseware.dao.ExternalToolDAO;
import edu.sjsu.courseware.dao.JarDAO;
import edu.sjsu.courseware.jnlp.JNLP;
import edu.sjsu.courseware.util.OAuthSignatureValidator;

@Controller
public class Launcher {
    @Inject
    OAuthSignatureValidator oauthValidator;
    
    @Inject
    ExternalToolDAO externalToolDAO;
    
    @Inject
    AssignmentDAO assignmentDAO;

    @Inject
    CourseDAO courseDAO;

    @Inject
    JarDAO jarDAO;

    @Inject
    Marshaller marshaller;
 
    private Logger logger = LoggerFactory.getLogger(Launcher.class);

    private static final String COURSE_ID = "custom_canvas_course_id";
    private static final String COURSE_LTI_ID = "context_id";
    private static final String COURSE_LTI_CODE = "context_label";        
    private static final String COURSE_LTI_NAME = "context_title";
    
    private static final String ASSIGNMENT_ID = "custom_canvas_assignment_id";
    private static final String ASSIGNMENT_NAME = "custom_canvas_assignment_title";
    private static final String ASSIGNMENT_LTI_ID = "resource_link_id";
    private static final String ASSIGNMENT_EXTERNAL_TOOL_NAME = "resource_link_title";
  
    private static final String USER_ID = "custom_canvas_user_id";
    private static final String USER_LTI_ID = "user_id";
    private static final String USER_LOGIN_ID = "custom_canvas_user_login_id";
    private static final String USER_ROLE = "roles";

    private static final String OUTCOME_URL = "lis_outcome_service_url";
    private static final String EXT_OUTCOME_URL = "ext_ims_lis_basic_outcome_url";
    private static final String RETURN_URL = "launch_presentation_return_url";
    
    private static final String CANVAS_INSTANCE_GUID = "tool_consumer_instance_guid";
    private static final String CANVAS_INSTANCE_NAME = "tool_consumer_instance_name";

    @RequestMapping("launch")
    public ModelAndView launch(@RequestParam("oauth_consumer_key") String consumerKey, @RequestParam("context_id") String courseLtiId, @RequestParam("resource_link_id") String assignmentLtiId, ModelAndView model, HttpServletRequest request, HttpServletResponse response) {
        ExternalTool externalTool = externalToolDAO.getExternalTool(consumerKey);
        
        if (externalTool == null)
            throw new InvalidConsumerException();
        
        if (!oauthValidator.verifySignature(request, externalTool.getSharedSecret()))
            throw new OAuthSignatureVerificationFailException();

        Course course = courseDAO.getCourse(courseLtiId);
        
        if (course == null) {
            course = courseDAO.insert(toCourse(request));
            assignmentDAO.insert(toAssignment(externalTool, course, request));
            model.setViewName("nojars");
            return model;
        }
      
        Assignment assignment = assignmentDAO.getAssignment(course.getId(), assignmentLtiId);
        
        if (assignment == null) {
            assignmentDAO.insert(toAssignment(externalTool, course, request));
            model.setViewName("nojars");
            return model;
        }
        
        List<Jar> jars = jarDAO.getJars(assignment.getId());
        
        if (jars == null || jars.isEmpty()) {
            model.setViewName("nojars");
            return model;
        }

        model.addObject("jars", jars);
        model.addObject("course", course);
        model.addObject("jnlp", getJNLP(jars));
        model.addObject("assignment", assignment);
        model.setViewName("launch");
        return model;
    }

    private String getJNLP(List<Jar> jars) {
        JNLP jnlp = new JNLP();
        
        for (Jar jar : jars) {
            edu.sjsu.courseware.jnlp.Jar jarFile  = new edu.sjsu.courseware.jnlp.Jar();
            jarFile.setHref(jar.getAssignmentId() + "/" + jar.getName());
            jnlp.getResources().getJar().add(jarFile);
        }
        
        ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
        
        try {
            marshaller.marshal(jnlp, new StreamResult(out));
        } catch (Exception e) {
            logger.error("Exception while marshalling JNLP file" + e);
        }
        
        return new String(Base64.encodeBase64(out.toByteArray()));
    }

    private Course toCourse(HttpServletRequest request) {
        Course course = new Course();
        course.setCanvasCourseId(request.getParameter(COURSE_ID));
        course.setCanvasLtiCourseId(request.getParameter(COURSE_LTI_ID));
        course.setCanvasLtiCourseCode(request.getParameter(COURSE_LTI_CODE));
        course.setCanvasLtiCourseName(request.getParameter(COURSE_LTI_NAME));
        return course;
    }

    private Assignment toAssignment(ExternalTool externalTool, Course course, HttpServletRequest request) {
        Assignment assignment = new Assignment();

        assignment.setCourseId(course.getId());
        assignment.setExternalToolId(externalTool.getId());
        
        assignment.setCanvasId(request.getParameter(ASSIGNMENT_ID));
        assignment.setCanvasLtiId(request.getParameter(ASSIGNMENT_LTI_ID));
        
        assignment.setName(request.getParameter(ASSIGNMENT_NAME));
        assignment.setCanvasName(request.getParameter(ASSIGNMENT_NAME));
        assignment.setCanvasExternalToolName(request.getParameter(ASSIGNMENT_EXTERNAL_TOOL_NAME));

        assignment.setCanvasUserId(request.getParameter(USER_ID));
        assignment.setCanvasUserRole(request.getParameter(USER_ROLE));
        assignment.setCanvasLtiUserId(request.getParameter(USER_LTI_ID));
        assignment.setCanvasUserLoginId(request.getParameter(USER_LOGIN_ID));

        assignment.setLisOutcomeServiceURL(request.getParameter(OUTCOME_URL));
        assignment.setLaunchPresentationReturnURL(request.getParameter(RETURN_URL));
        assignment.setExternalImsLisBasicOutcomUrl(request.getParameter(EXT_OUTCOME_URL));
 
        assignment.setCanvasInstanceGuid(request.getParameter(CANVAS_INSTANCE_GUID));
        assignment.setCanvasInstanceName(request.getParameter(CANVAS_INSTANCE_NAME));

        return assignment;
    }
}
