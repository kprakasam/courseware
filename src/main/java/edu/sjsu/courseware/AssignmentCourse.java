package edu.sjsu.courseware;

public class AssignmentCourse {
    long id;
    String Name;
    String courseCode;
    String courseName;
    String externalTool;
    String canvasInstance;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
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
    public String getCanvasInstance() {
        return canvasInstance;
    }
    public void setCanvasInstance(String canvasInstance) {
        this.canvasInstance = canvasInstance;
    }
}
