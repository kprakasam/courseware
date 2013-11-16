package edu.sjsu.courseware.json;

import java.util.List;

import edu.sjsu.courseware.AssignmentCourse;

public class Assignment {
    String id;
    String courseCode;
    String courseName;
    String externalTool;
    String assignmentName;
    String canvasInstance;
        
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }


    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }


    public String getCourseName() {
        return courseName;
    }


    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }


    public String getExternalTool() {
        return externalTool;
    }


    public void setExternalTool(String externalTool) {
        this.externalTool = externalTool;
    }


    public String getAssignmentName() {
        return assignmentName;
    }


    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }


    public String getCanvasInstance() {
        return canvasInstance;
    }


    public void setCanvasInstance(String canvasInstance) {
        this.canvasInstance = canvasInstance;
    }


    public static Assignment[] toAssignment(List<AssignmentCourse> assignmentCourseList) {
        if (assignmentCourseList == null || assignmentCourseList.isEmpty())
            return new Assignment[]{};;

        int i = 0;
        Assignment[] assignments = new Assignment[assignmentCourseList.size()];
                
        for (AssignmentCourse assignmentCourse : assignmentCourseList) {
            Assignment assignment = new Assignment();
            assignment.id = String.valueOf(assignmentCourse.getId());
            assignment.courseCode = assignmentCourse.getCourseCode();
            assignment.courseName = assignmentCourse.getCourseName();
            assignment.assignmentName = assignmentCourse.getName();
            assignment.externalTool = assignmentCourse.getExternalTool();
            assignment.canvasInstance = assignmentCourse.getCanvasInstance();
            assignments[i++] = assignment;
        }
        
        return assignments;
    }

}
