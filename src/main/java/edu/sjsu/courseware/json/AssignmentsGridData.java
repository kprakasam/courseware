package edu.sjsu.courseware.json;

import java.util.List;

import edu.sjsu.courseware.AssignmentCourse;

public class AssignmentsGridData {
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
    
    public static AssignmentsGridData toAssignmentsGridData(List<AssignmentCourse> assignments) {
        AssignmentsGridData assignmentsGridData = new AssignmentsGridData();
        
        if (assignments == null || assignments.isEmpty())
            return assignmentsGridData;
        
        assignmentsGridData.total = String.valueOf((assignments.size() % 10 == 0) ?  assignments.size() / 10 : assignments.size() / 10 + 1);
        assignmentsGridData.records =  String.valueOf(assignments.size());
        assignmentsGridData.rows = new Row[assignments.size()];
        
        int i = 0;
        for (AssignmentCourse assignmentCourse : assignments) {
            Row row = new Row();
            row.id = String.valueOf(assignmentCourse.getId());
            
            row.cell = new String[6];
            row.cell[0] = String.valueOf(assignmentCourse.getId());
            row.cell[1] = assignmentCourse.getCourseCode();
            row.cell[2] = assignmentCourse.getCourseName();
            row.cell[3] = assignmentCourse.getName();
            row.cell[4] = assignmentCourse.getExternalTool();
            row.cell[5] = assignmentCourse.getCanvasInstance();
            assignmentsGridData.rows[i++] = row;
        }
        
        return assignmentsGridData;
    }

}
