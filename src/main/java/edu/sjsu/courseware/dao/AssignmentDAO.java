package edu.sjsu.courseware.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import edu.sjsu.courseware.Assignment;

@Repository
public class AssignmentDAO {
    private Logger logger = LoggerFactory.getLogger(AssignmentDAO.class);

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private RowMapper<Assignment> assignmentRowMapper = new RowMapper<Assignment>() {
        public Assignment mapRow(ResultSet rs, int rowNum) throws SQLException {
            Assignment assignment = new Assignment();
            assignment.setId(rs.getLong("assignment_id"));
            assignment.setExternalToolId(rs.getLong("external_tool_id"));
            assignment.setCourseId(rs.getLong("course_id"));
            assignment.setName(rs.getString("assignment_name"));
            assignment.setCanvasId(rs.getString("canvas_assignment_id"));
            assignment.setCanvasName(rs.getString("canvas_assignment_name"));
            assignment.setCanvasLtiId(rs.getString("canvas_lti_assignment_id"));
            assignment.setCanvasExternalToolName(rs.getString("canvas_external_tool_name"));
            assignment.setCanvasUserId(rs.getString("canvas_user_id"));
            assignment.setCanvasLtiUserId(rs.getString("canvas_lti_user_id"));
            assignment.setCanvasUserLoginId(rs.getString("canvas_user_login_id"));
            assignment.setCanvasUserRole(rs.getString("canvas_user_role"));
            assignment.setExternalImsLisBasicOutcomUrl(rs.getString("ext_ims_lis_basic_outcome_url"));
            assignment.setLaunchPresentationReturnURL(rs.getString("launch_presentation_return_url"));
            assignment.setLisOutcomeServiceURL(rs.getString("lis_outcome_service_url"));
            assignment.setCanvasInstanceGuid(rs.getString("canvas_instance_guid"));
            assignment.setCanvasInstanceGuid(rs.getString("canvas_instance_name"));
            return assignment;
        }
    };
    
    @Inject
    public void init(DataSource dataSource) {
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
    public Assignment insert(Assignment assignment) {
        String sql = "INSERT INTO assignment " + 
                        "(course_id," +
                        "external_tool_id," +
                        "assignment_name," +
                        "canvas_assignment_id," + 
                        "canvas_assignment_name," +
                        "canvas_lti_assignment_id," +
                        "canvas_external_tool_name," +
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
                        ":externalToolId," +
                        ":name," +
                        ":canvasId," +
                        ":canvasName," +
                        ":canvasLtiId," +
                        ":externalToolName," +
                        ":canvasUserId," +
                        ":canvasLtiUserId," +
                        ":canvasUserLoginId," +
                        ":canvasUserRole," +
                        ":extImsLisBasicOutcomeURL," +
                        ":launchPresentationReturnURL," +
                        ":lisOutcomeServiceURL," +
                        ":canvasInstanceGuid," +
                        ":canvasInstanceName)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        
        params.addValue("courseId", assignment.getCourseId());
        params.addValue("externalToolId", assignment.getExternalToolId());
        params.addValue("canvasId", assignment.getCanvasId());
        params.addValue("canvasLtiId", assignment.getCanvasLtiId());

        params.addValue("name", assignment.getCanvasName());
        params.addValue("canvasName", assignment.getCanvasName());
        params.addValue("externalToolName", assignment.getCanvasExternalToolName());
        
        params.addValue("canvasUserId", assignment.getCanvasUserId());
        params.addValue("canvasLtiUserId", assignment.getCanvasLtiUserId());
        params.addValue("canvasUserLoginId", assignment.getCanvasUserLoginId());
        params.addValue("canvasUserRole", assignment.getCanvasUserRole());
        
        params.addValue("extImsLisBasicOutcomeURL", assignment.getExternalImsLisBasicOutcomUrl());
        params.addValue("launchPresentationReturnURL", assignment.getLaunchPresentationReturnURL());
        params.addValue("lisOutcomeServiceURL", assignment.getLisOutcomeServiceURL());
        
        params.addValue("canvasInstanceGuid", assignment.getCanvasInstanceGuid());        
        params.addValue("canvasInstanceName", assignment.getCanvasInstanceName());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        try {
            namedParameterJdbcTemplate.update(sql, params, keyHolder);
            return getAssignment(keyHolder.getKey().longValue());
        } catch (DuplicateKeyException dke) {
            logger.debug("Race condition in insert anyway we are safe.");
            return getAssignment(assignment.getCourseId(), assignment.getCanvasLtiId());
        } catch (DataAccessException dae) {
            logger.error("Exception when inserting assignment record" + dae);
            return null;
        }
    }

    public Assignment getAssignment(long courseId, String assignmentLtiId) {
        String sql = "SELECT " +
                "assignment_id," +
                "course_id," +
                "external_tool_id," +
                "assignment_name," +
                "canvas_assignment_id," +
                "canvas_assignment_name," +
                "canvas_lti_assignment_id," +
                "canvas_external_tool_name," +
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
                "course_id = :courseId AND canvas_lti_assignment_id = :assignmentLtiId";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("assignmentLtiId", assignmentLtiId);
        params.put("courseId", courseId);
        
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, params, assignmentRowMapper);
        } catch (EmptyResultDataAccessException ex) { return null;}

    }

    public Assignment getAssignment(long assignmentId) {
        String sql = "SELECT " +
                        "assignment_id," +
                        "course_id," +
                        "external_tool_id," +
                        "assignment_name," +
                        "canvas_assignment_id," +
                        "canvas_assignment_name," +
                        "canvas_lti_assignment_id," +
                        "canvas_external_tool_name," +
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
                        "assignment_id = :assignmentId";
        
        Map<String, Long> params = Collections.singletonMap("assignmentId", assignmentId);

        try {
            return namedParameterJdbcTemplate.queryForObject(sql, params, assignmentRowMapper);
        } catch (EmptyResultDataAccessException ex) { return null;}
    }
}
