package edu.sjsu.courseware.json;

import java.util.List;

import edu.sjsu.courseware.AssignmentCourse;
import edu.sjsu.courseware.Jar;

public class JarsGridData {
    String page = "0";
    String total = "0";
    String records = "0";
    Row[] rows = new Row[]{};

    public String getPage() {
        return page;
    }
    public void setPage(String page) {
        this.page = page;
    }
    public String getTotal() {
        return total;
    }
    public void setTotal(String total) {
        this.total = total;
    }
    public String getRecords() {
        return records;
    }
    public void setRecords(String records) {
        this.records = records;
    }
    public Row[] getRows() {
        return rows;
    }
    public void setRows(Row[] rows) {
        this.rows = rows;
    }
    
    public static JarsGridData toJarsGridData(List<Jar> jars) {
        JarsGridData jarssGridData = new JarsGridData();
        
        if (jars == null || jars.isEmpty())
            return jarssGridData;
        
        jarssGridData.total = String.valueOf((jars.size() % 10 == 0) ?  jars.size() / 10 : jars.size() / 10 + 1);
        jarssGridData.records =  String.valueOf(jars.size());
        jarssGridData.rows = new Row[jars.size()];
        
        int i = 0;
        for (Jar jar : jars) {
            Row row = new Row();
            row.id = String.valueOf(jar.getId());
            
            row.cell = new String[4];
            row.cell[0] = String.valueOf(jar.getId());
            row.cell[1] = String.valueOf(jar.getAssignmentId());
            row.cell[2] = jar.getName();
            row.cell[3] = jar.getMainClass();
            jarssGridData.rows[i++] = row;
        }
        
        return jarssGridData;
    }

}
