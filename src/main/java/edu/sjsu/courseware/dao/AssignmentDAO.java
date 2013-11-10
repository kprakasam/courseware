package edu.sjsu.courseware.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
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
        String sql = "SELECT "+
                        "COUNT(*) " +
                     "FROM " +
                        "course, " +
                        "assignment " +
                      "WHERE " +
                        "course.couse_id = assignment.course_id AND canvas_lti_assignment_id = :assignmentLtiId' AND course.canvas_lti_course_id = courseLtiId";
        
        Map<String, String> params = new HashMap<String, String>();
        params.put("assignmentLtiId", assignmentLtiId);
        params.put("courseLtiId", courseLtiId);
        
        return namedParameterJdbcTemplate.queryForObject(sql, params ,int.class) != 0;
    }

    /**
     * Inserts assignment for a given course into assignment table and returns the inserted assignment or null when insert failed. 
     * @param assignment to be inserted into the assignment table;
     * @return inserted assignment or null.
     */
    public Assignment insertAssignment(Assignment assignment) {
        String sql = "INSERT INTO assignment " + 
                        "(course_id," +
                        "assignment_name," +
                        "canvas_assignment_id," + 
                        "canvas_assignment_name," +
                        "canvas_lti_assignment_id," +
                        "canvas_lti_assignment_name," +
                        "canvas_user_id," +
                        "canvas_lti_user_id," +
                        "canvas_user_login_id," +
                        "canvas_user_role," +
                        "ext_ims_lis_basic_outcome_url," +
                        "launch_presentation_return_url," +
                        "lis_outcome_service_url," +
                        "canvas_instance_guid," +
                        "canvas_instance_name) " +
                     "VALUES " +
                        "(:courseId," +
                        ":name," +
                        ":canvasId," +
                        ":canvasName," +
                        ":canvasLtiId," +
                        ":canvasLtiName," +
                        ":canvasUserId," +
                        ":canvasLtiUserId," +
                        ":canvasUserLoginId," +
                        ":canvasUserRole," +
                        ":extImsLisBasicOutcomeURL," +
                        ":launchPresentationReturnURL," +
                        ":lisOutcomeServiceURL," +
                        ":canvasInstanceGuid," +
                        ":canvasInstanceName)";

        Map<String, Object> params = new HashMap<String, Object>();
        
        params.put("courseId", assignment.getCourseId());
        params.put("canvasId", assignment.getCanvasId());
        params.put("canvasLtiId", assignment.getCanvasLtiId());

        params.put("name", assignment.getName());
        params.put("canvasName", assignment.getCanvasName());
        params.put("canvasLtiName", assignment.getCanvasName());
        
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
            namedParameterJdbcTemplate.update(sql, params);
            return getAssignment(assignment.getCanvasLtiId());
        } catch (DuplicateKeyException dke) {
            logger.debug("Race condition in insert anyway we are safe.");
            return getAssignment(assignment.getCanvasLtiId());
        } catch (DataAccessException dae) {
            logger.error("Exception when inserting assignment record" + dae);
            return null;
        }
    }

    public Assignment getAssignment(String canvasLtiId) {
        String sql = "SELECT " +
                        "assignment_id," +
                        "course_id," +
                        "assignment_name," +
                        "canvas_assignment_id," +
                        "canvas_assignment_name," +
                        "canvas_lti_assignment_id," +
                        "canvas_lti_assignment_name," +
                        "canvas_user_id," +
                        "canvas_lti_user_id," +
                        "canvas_user_login_id," +
                        "canvas_user_role," +
                        "ext_ims_lis_basic_outcome_url," +
                        "launch_presentation_return_url," +
                        "lis_outcome_service_url," +
                        "canvas_instance_guid," +
                        "canvas_instance_name " +
                     "FROM " +
                        "assignment " +
                     "WHERE " +
                        "canvas_lti_assignment_id = :canvasLtiId";
        
        Map<String, String> params = Collections.singletonMap("canvasLtiId", canvasLtiId);

        return namedParameterJdbcTemplate.queryForObject(sql, params, new RowMapper<Assignment>() {

            public Assignment mapRow(ResultSet rs, int rowNum) throws SQLException {
                Assignment assignment = new Assignment();
                assignment.setId(rs.getLong("assignment_id"));
                assignment.setCourseId(rs.getLong("course_id"));
                assignment.setName(rs.getString("assignment_name"));
                assignment.setCanvasId(rs.getString("canvas_assignment_id"));
                assignment.setCanvasName(rs.getString("canvas_assignment_name"));
                assignment.setCanvasLtiId(rs.getString("canvas_lti_assignment_id"));
                assignment.setCanvasLtiName(rs.getString("canvas_lti_assignment_name"));
                assignment.setCanvasUserId(rs.getString("canvas_user_id"));
                assignment.setCanvasLtiUserId(rs.getString("canvas_lti_user_id"));
                assignment.setCanvasUserLoginId(rs.getString("canvas_user_login_id"));
                assignment.setCanvasUserRole(rs.getString("canvas_user_role"));
                assignment.setExtendedImsLisBasicOutcomUrl(rs.getString("ext_ims_lis_basic_outcome_url"));
                assignment.setLaunchPresentationReturnURL(rs.getString("launch_presentation_return_url"));
                assignment.setLisOutcomeServiceURL(rs.getString("lis_outcome_service_url"));
                assignment.setCanvasInstanceGuid(rs.getString("canvas_instance_guid"));
                assignment.setCanvasInstanceGuid(rs.getString("canvas_instance_name"));
                return assignment;
            }
        });
    }
}
