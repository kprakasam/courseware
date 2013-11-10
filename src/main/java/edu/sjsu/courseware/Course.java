package edu.sjsu.courseware;

public class Course {
    long id;
    String canvasCourseId; 
    String canvasLtiCourseId; 
    String canvasLtiCourseCode; 
    String canvasLtiCourseName; 
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getCanvasCourseId() {
        return canvasCourseId;
    }
    public void setCanvasCourseId(String canvasCourseId) {
        this.canvasCourseId = canvasCourseId;
    }
    public String getCanvasLtiCourseId() {
        return canvasLtiCourseId;
    }
    public void setCanvasLtiCourseId(String canvasLtiCourseId) {
        this.canvasLtiCourseId = canvasLtiCourseId;
    }
    public String getCanvasLtiCourseCode() {
        return canvasLtiCourseCode;
    }
    public void setCanvasLtiCourseCode(String canvasLtiCourseCode) {
        this.canvasLtiCourseCode = canvasLtiCourseCode;
    }
    public String getCanvasLtiCourseName() {
        return canvasLtiCourseName;
    }
    public void setCanvasLtiCourseName(String canvasLtiCourseName) {
        this.canvasLtiCourseName = canvasLtiCourseName;
    } 
}
