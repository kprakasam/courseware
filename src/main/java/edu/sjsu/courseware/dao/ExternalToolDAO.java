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

import edu.sjsu.courseware.ExternalTool;

@Repository
public class ExternalToolDAO {
    private Logger logger = LoggerFactory.getLogger(AssignmentDAO.class);
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<ExternalTool> getExternalTools() {
        String sql = "SELECT " +
                        "id," +
                        "name," +
                        "consumer_key," +
                        "shared_secret " +
                     "FROM external_tool";
        
        return namedParameterJdbcTemplate.query(sql, new RowMapper<ExternalTool>() {

            public ExternalTool mapRow(ResultSet rs, int rowNum) throws SQLException {
                ExternalTool externalTool = new ExternalTool();
                externalTool.setId(rs.getLong("id"));
                externalTool.setName(rs.getString("name"));
                externalTool.setConsumerKey(rs.getString("consumer_key"));
                externalTool.setSharedSecret(rs.getString("shared_secret"));
                return externalTool;
            }
         });
    }
    
    public ExternalTool getExternalTool(String consumerKey) {
        String sql = "SELECT " +
                        "id," +
                        "name," +
                        "consumer_key," +
                        "shared_secret " +
                     "FROM " +
                        "external_tool " +
                     "WHERE "+
                        "consumer_key = :consumerKey";
        
        Map<String, String> param = Collections.singletonMap("consumerKey", consumerKey);
        return namedParameterJdbcTemplate.queryForObject(sql, param, new RowMapper<ExternalTool>() {

            public ExternalTool mapRow(ResultSet rs, int rowNum) throws SQLException {
                ExternalTool externalTool = new ExternalTool();
                externalTool.setId(rs.getLong("id"));
                externalTool.setName(rs.getString("name"));
                externalTool.setConsumerKey(rs.getString("consumer_key"));
                externalTool.setSharedSecret(rs.getString("shared_secret"));
                return externalTool;
            }
         });
    }

    public ExternalTool insert(ExternalTool externalTool) {
        String sql = "INSERT INTO external_tool " +
                        "(name," +
                        "consumer_key," +
                        "shared_secret) " +
                        "VALUES " +
                        "(:name," +
                        ":consumerKey," +
                        ":sharedSecret)";
        
        Map<String, String> param = new HashMap<String, String>();
        param.put("name", externalTool.getName());
        param.put("consumerKey", externalTool.getConsumerKey());
        param.put("sharedSecret", externalTool.getSharedSecret());

         try {
            namedParameterJdbcTemplate.update(sql, param);
            return getExternalTool(externalTool.getConsumerKey());
        } catch (DuplicateKeyException dke) {
            logger.debug("Race condition in insert anyway we are safe.");
            return getExternalTool(externalTool.getConsumerKey());
        } catch (DataAccessException dae) {
            logger.error("Exception when inserting external tool record" + dae);
            return null;
        }
        
    }
}
