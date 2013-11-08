package edu.sjsu.courseware.dao;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import edu.sjsu.courseware.Assignment;

@Repository
public class AssignmentDAO {
    private Logger logger = LoggerFactory.getLogger(AssignmentDAO.class);

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    
    public void setDataSource(DataSource dataSource) {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public boolean isAssignmentExist(String assignmentLtiId, String courseLtiId) {
        String sql = "select count(*) from assignment where canvas_lti_assignment_id = :ltiAssignmentId and canvas_lti_course_id = :ltiCourseId";
        
        Map<String, String> params = new HashMap<String, String>();
        params.put("ltiAssignmentId", assignmentLtiId);
        params.put("ltiCourseId", courseLtiId);
        
        return namedParameterJdbcTemplate.queryForObject(sql, params ,int.class) != 0;
    }

    public boolean insertAssignment(Assignment assignment) {
        String sql = "INSERT INTO assignment " + 
                        "(assignment_name," +
                        "canvas_assignment_id," + 
                        "canvas_assignment_title," +
                        "canvas_lti_assignment_id," +
                        "canvas_lti_assignment_name," +
                        "canvas_course_id," +
                        "canvas_lti_course_id," +
                        "canvas_lti_course_code," +
                        "canvas_lti_course_title," +
                        "canvas_user_id," +
                        "canvas_lti_user_id," +
                        "canvas_user_login_id," +
                        "canvas_user_role," +
                        "ext_ims_lis_basic_outcome_url," +
                        "launch_presentation_return_url," +
                        "lis_outcome_service_url," +
                        "canvas_instance_guid," +
                        "canvas_instance_name)" +
                     "VALUES " +
                        "(:name," +
                        ":canvasId," +
                        ":canvasName," +
                        ":canvasLtiId," +
                        ":canvasLtiName," +
                        ":canvasCourseId," +
                        ":canvasLtiCourseId," +
                        ":canvasLtiCourseCode," +
                        ":canvasLtiCourseName," +
                        ":canvasUserId," +
                        ":canvasLtiUserId," +
                        ":canvasUserLoginId," +
                        ":canvasUserRole," +
                        ":extImsLisBasicOutcomeURL," +
                        ":launchPresentationReturnURL," +
                        ":lisOutcomeServiceURL," +
                        ":canvasInstanceGuid," +
                        ":canvasInstanceName)";

        Map<String, String> params = new HashMap<String, String>();
        
        params.put("canvasId", assignment.getCanvasId());
        params.put("canvasLtiId", assignment.getCanvasLtiId());

        params.put("name", assignment.getName());
        params.put("canvasName", assignment.getCanvasName());
        params.put("canvasLtiName", assignment.getCanvasName());
        
        params.put("canvasCourseId", assignment.getCanvasCourseId());
        params.put("canvasLtiCourseId", assignment.getCanvasLtiCourseId());
        params.put("canvasLtiCourseCode", assignment.getCanvasLtiCourseCode());
        params.put("canvasLtiCourseName", assignment.getCanvasLtiCourseName());
        
        params.put("canvasUserId", assignment.getCanvasUserId());
        params.put("canvasLtiUserId", assignment.getCanvasLtiUserId());
        params.put("canvasUserLoginId", assignment.getCanvasUserLoginId());
        params.put("canvasUserRole", assignment.getCanvasUserRole());
        
        params.put("extImsLisBasicOutcomeURL", assignment.getExtendedImsLisBasicOutcomUrl());
        params.put("launchPresentationReturnURL", assignment.getLaunchPresentationReturnURL());
        params.put("lisOutcomeServiceURL", assignment.getLisOutcomeServiceURL());
        
        params.put("canvasInstanceGuid", assignment.getCanvasInstanceGuid());        
        params.put("canvasInstanceName", assignment.getCanvasInstanceName());

        try {
            return namedParameterJdbcTemplate.update(sql, params) != 0;
        } catch (DuplicateKeyException dke) {
            logger.debug("Race condition in insert anyway we are safe.");
            return true;
        } catch (DataAccessException dae) {
            logger.error("Exception when inserting assignment record" + dae);
            return false;
        }
    }
}
