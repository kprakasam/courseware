package edu.sjsu.courseware.json;

import org.springframework.web.multipart.MultipartFile;

public class UploadedFile {
    long id;
    long size;
    boolean success;
    String name;
    
    public UploadedFile(long id, boolean success, MultipartFile file) {
        this.id = id;
        this.success = success;
        this.size = file.getSize();
        this.name = file.getOriginalFilename();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
