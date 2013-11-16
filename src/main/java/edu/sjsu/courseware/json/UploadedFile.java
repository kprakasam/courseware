package edu.sjsu.courseware.json;

import org.springframework.web.multipart.MultipartFile;

public class UploadedFile {
    Long size;
    String name;
    String url;
    String thumbnail_url;
    String delete_url;
    String delete_type;
    
    public UploadedFile(MultipartFile file) {
        size = file.getSize();
        name = file.getOriginalFilename();
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getSize() {
        return size;
    }
    public void setSize(Long size) {
        this.size = size;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getThumbnail_url() {
        return thumbnail_url;
    }
    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }
    public String getDelete_url() {
        return delete_url;
    }
    public void setDelete_url(String delete_url) {
        this.delete_url = delete_url;
    }
    public String getDelete_type() {
        return delete_type;
    }
    public void setDelete_type(String delete_type) {
        this.delete_type = delete_type;
    }
}
