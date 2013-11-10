package edu.sjsu.courseware.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import edu.sjsu.courseware.Jar;

@Repository
public class JarDAO {
    private Logger logger = LoggerFactory.getLogger(JarDAO.class);

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    
    public void setDataSource(DataSource dataSource) {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<Jar> getJars(long assignmentId) {
        String sql = "SELECT " +
                        "jar_id," +
                        "assignment_id," +
                        "jar_name," +
                        "jar_main_classs " +
                     "FROM " +
                        "jar "+
                     "WHERE " +
                        "assignment_id = :assignmentId";

        Map<String, Long> params = Collections.singletonMap("assignmentId", assignmentId);

        return namedParameterJdbcTemplate.query(sql, params, new RowMapper<Jar>() {

            public Jar mapRow(ResultSet rs, int rowNum) throws SQLException {
                Jar jar = new Jar();
                jar.setId(rs.getLong("jar_id"));
                jar.setAssignmentId(rs.getLong("assignment_id"));
                jar.setName(rs.getString("jar_name"));
                jar.setMainClass(rs.getString("jar_main_class"));
                return jar;
            }
        });
    }

    public byte[] getJarFile(long assignmentId, String jarName) {
        String sql = "SELECT " +
                        "jar_file " +
                     "FROM " +
                        "jar "+
                     "WHERE " +
                        "assignment_id = :assignmentId AND jar_name = :jarName";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("jarName", jarName);
        params.put("assignmentId", assignmentId);
        return namedParameterJdbcTemplate.queryForObject(sql, params, byte[].class);
    }

    //TODO too big byte[] array can cause OOM need to stream this later
    /**
     * Insert jar and jarfile into table jar and returns the inserted jar or null when insert fails 
     * @param jar to be inserted
     * @param jarfile to be inserted
     * @return jar that has been inserted or null
     */
    public Jar insert(Jar jar, byte[] jarfile) {
        final String sql = "INSERT INTO jar " + 
                "(jar_id," +
                "assignment_id," +
                "jar_name," +
                "jar_main_class" +
                "jar_file) " +
           "VALUES " +
                "(:jarId," +
                ":assignmentId," +
                ":jarName," +
                ":jarMainClass" +
                ":jarFile)";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("jarId", jar.getId());
        params.put("assignmentId", jar.getAssignmentId());
        params.put("jarName", jar.getName());
        params.put("jarMainClass", jar.getMainClass());
        params.put("jar", jarfile);

        try {
            namedParameterJdbcTemplate.update(sql, params);
            return getJar(jar.getAssignmentId(), jar.getName());
        } catch (DuplicateKeyException dke) {
            logger.debug("Race condition in insert anyway we are safe.");
            return getJar(jar.getAssignmentId(), jar.getName());
        } catch (DataAccessException dae) {
            logger.error("Exception when inserting assignment record" + dae);
            return null;
        } 
    }

    public Jar getJar(long assignmentId, String jarName) {
        String sql = "SELECT " +
                "jar_id," +
                "assignment_id," +
                "jar_name," +
                "jar_main_classs " +
             "FROM " +
                "jar "+
             "WHERE " +
                "assignment_id = :assignmentId AND jar_name = :jarName";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("jarName", jarName);
        params.put("assignmentId", assignmentId);

        return namedParameterJdbcTemplate.queryForObject(sql, params, new RowMapper<Jar>() {

            public Jar mapRow(ResultSet rs, int rowNum) throws SQLException {
                Jar jar = new Jar();
                jar.setId(rs.getLong("jar_id"));
                jar.setAssignmentId(rs.getLong("assignment_id"));
                jar.setName(rs.getString("jar_name"));
                jar.setMainClass(rs.getString("jar_main_class"));
                return jar;
            }
        });
    }
}
