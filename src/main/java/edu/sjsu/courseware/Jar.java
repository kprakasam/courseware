package edu.sjsu.courseware;

public class Jar {
    long id;
    long assignmentId;
    String name;
    String mainClass;
    
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getAssignmentId() {
        return assignmentId;
    }
    public void setAssignmentId(long assignmentId) {
        this.assignmentId = assignmentId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMainClass() {
        return mainClass;
    }
    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }
}
