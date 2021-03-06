package edu.sjsu.courseware;

public class Assignment {
    long id;
    long courseId;
    long externalToolId;
    
    String canvasId;
    String canvasLtiId;

    String name;
    String canvasName;

    String canvasExternalToolName;

    String canvasUserId; 
    String canvasLtiUserId; 
    String canvasUserLoginId; 
    String canvasUserRole;
    
    String lisOutcomeServiceURL;
    String externalImsLisBasicOutcomUrl;
    String launchPresentationReturnURL;

    String canvasInstanceGuid;
    String canvasInstanceName;
    
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getCourseId() {
        return courseId;
    }
    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }
    public long getExternalToolId() {
        return externalToolId;
    }
    public void setExternalToolId(long externalToolId) {
        this.externalToolId = externalToolId;
    }
    public String getCanvasId() {
        return canvasId;
    }
    public void setCanvasId(String canvasId) {
        this.canvasId = canvasId;
    }
    public String getCanvasLtiId() {
        return canvasLtiId;
    }
    public void setCanvasLtiId(String canvasLtiId) {
        this.canvasLtiId = canvasLtiId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCanvasName() {
        return canvasName;
    }
    public void setCanvasName(String canvasName) {
        this.canvasName = canvasName;
    }
    public String getCanvasExternalToolName() {
        return canvasExternalToolName;
    }
    public void setCanvasExternalToolName(String canvasExternalToolName) {
        this.canvasExternalToolName = canvasExternalToolName;
    }
    public String getCanvasUserId() {
        return canvasUserId;
    }
    public void setCanvasUserId(String canvasUserId) {
        this.canvasUserId = canvasUserId;
    }
    public String getCanvasLtiUserId() {
        return canvasLtiUserId;
    }
    public void setCanvasLtiUserId(String canvasLtiUserId) {
        this.canvasLtiUserId = canvasLtiUserId;
    }
    public String getCanvasUserLoginId() {
        return canvasUserLoginId;
    }
    public void setCanvasUserLoginId(String canvasUserLoginId) {
        this.canvasUserLoginId = canvasUserLoginId;
    }
    public String getCanvasUserRole() {
        return canvasUserRole;
    }
    public void setCanvasUserRole(String canvasUserRole) {
        this.canvasUserRole = canvasUserRole;
    }
    public String getLaunchPresentationReturnURL() {
        return launchPresentationReturnURL;
    }
    public void setLaunchPresentationReturnURL(String launchPresentationReturnURL) {
        this.launchPresentationReturnURL = launchPresentationReturnURL;
    }
    public String getExternalImsLisBasicOutcomUrl() {
        return externalImsLisBasicOutcomUrl;
    }
    public void setExternalImsLisBasicOutcomUrl(String externalImsLisBasicOutcomUrl) {
        this.externalImsLisBasicOutcomUrl = externalImsLisBasicOutcomUrl;
    }
    public String getCanvasInstanceGuid() {
        return canvasInstanceGuid;
    }
    public void setCanvasInstanceGuid(String canvasInstanceGuid) {
        this.canvasInstanceGuid = canvasInstanceGuid;
    }
    public String getCanvasInstanceName() {
        return canvasInstanceName;
    }
    public void setCanvasInstanceName(String canvasInstanceName) {
        this.canvasInstanceName = canvasInstanceName;
    }
    public String getLisOutcomeServiceURL() {
        return lisOutcomeServiceURL;
    }
    public void setLisOutcomeServiceURL(String lisOutcomeServiceURL) {
        this.lisOutcomeServiceURL = lisOutcomeServiceURL;
    } 
}
