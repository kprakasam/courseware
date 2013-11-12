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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import edu.sjsu.courseware.Course;

@Repository
public class CourseDAO {
    private Logger logger = LoggerFactory.getLogger(CourseDAO.class);

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
   
    @Inject
    public void init(DataSource dataSource) {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public boolean isCourseExist(String assignmentLtiId, String courseLtiId) {
        String sql = "select count(*) from course where course.canvas_lti_course_id = :courseLtiId";
        Map<String, String> params = Collections.singletonMap("courseLtiId", courseLtiId);
        return namedParameterJdbcTemplate.queryForObject(sql, params ,int.class) != 0;
    }

    /**
     * Insert the course into course table and returns the inserted course object or null when insert failed.
     * @param course being inserted.
     * @return course that was successfully inserted or null.
     */
    public Course insert(Course course) {
        final String sql = "INSERT INTO course " + 
                                "(canvas_course_id," +
                                "canvas_lti_course_id," +
                                "canvas_lti_course_code," +
                                "canvas_lti_course_name) " +
                           "VALUES " +
                                "(:canvasCourseId," +
                                ":canvasLtiCourseId," +
                                ":canvasLtiCourseCode," +
                                ":canvasLtiCourseName)";

        Map<String, String> params = new HashMap<String, String>();
        params.put("canvasCourseId", course.getCanvasCourseId());
        params.put("canvasLtiCourseId", course.getCanvasLtiCourseId());
        params.put("canvasLtiCourseCode", course.getCanvasLtiCourseCode());
        params.put("canvasLtiCourseName", course.getCanvasLtiCourseName());
 
        try {
            namedParameterJdbcTemplate.update(sql, params);     
            return getCourse(course.getCanvasLtiCourseId());
        } catch (DuplicateKeyException dke) {
            logger.debug("Race condition in insert anyway we are safe.");
            return getCourse(course.getCanvasLtiCourseId());
        } catch (DataAccessException dae) {
            logger.error("Exception when inserting assignment record" + dae);
            return null;
        }
    }

    public Course getCourse(String courseLtiId) {
        String sql = "SELECT " +
                        "course_id," + 
                        "canvas_course_id," + 
                        "canvas_lti_course_id," + 
                        "canvas_lti_course_code," + 
                        "canvas_lti_course_name " + 
                     "FROM "+
                        "course " +
                     "WHERE " +
                        "canvas_lti_course_id = :courseLtiId";

        Map<String, String> params = Collections.singletonMap("courseLtiId", courseLtiId);

        try {
            return namedParameterJdbcTemplate.queryForObject(sql, params , new RowMapper<Course>() {
    
                public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Course course = new Course();
                    course.setId(rs.getLong("course_id"));
                    course.setCanvasCourseId(rs.getString("canvas_course_id"));
                    course.setCanvasLtiCourseId(rs.getString("canvas_lti_course_id"));
                    course.setCanvasLtiCourseCode(rs.getString("canvas_lti_course_code"));
                    course.setCanvasLtiCourseName(rs.getString("canvas_lti_course_name"));
                    return course;
                }});
        } catch (EmptyResultDataAccessException ex) { return null;}

    }
}
