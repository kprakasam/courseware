package edu.sjsu.courseware.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

@Repository
public class ApplicationDAO {
    private JdbcTemplate jdbcTemplate;
    Logger logger = LoggerFactory.getLogger(AssignmentDAO.class);

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Map<String, String> getSettings() {
        final Map<String, String> settings = new HashMap<String, String>();

        try {
            return this.jdbcTemplate.query("select name, value from application", new ResultSetExtractor<Map<String, String>>() {

                public Map<String, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
                    while (rs.next())
                        settings.put(rs.getString(1), rs.getString(2));

                    return settings;
                }
            });
        } catch (DataAccessException dae) {
            logger.error("Unable to read application settings " + dae);
        }

        return settings;
    }
}
