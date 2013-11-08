package edu.sjsu.courseware;

import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import edu.sjsu.courseware.dao.ApplicationDAO;

@Component
public class Application {
    @Inject
    ApplicationDAO applicationDAO;    
    Map<String, String> properties = applicationDAO.getSettings();
    
    public String get(String settingName) {
        return properties.get(settingName);
    }    
}
