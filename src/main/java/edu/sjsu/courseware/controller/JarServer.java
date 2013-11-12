package edu.sjsu.courseware.controller;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.sjsu.courseware.dao.JarDAO;

@Controller
public class JarServer {
    @Inject
    JarDAO jarDAO;
    
    private Logger logger = LoggerFactory.getLogger(Launcher.class);

    @RequestMapping("jars/{assignmentId}/{jarFile:[A-Za-z]+.jar}")
    public void  serve(@PathVariable long assignmentId, @PathVariable String jarFile, HttpServletResponse response) {
        try {
            response.getOutputStream().write(jarDAO.getJarFile(assignmentId, jarFile));
        } catch (IOException e) {
            logger.error("Error reading jarfile " + jarFile + ".jar" + e);
        }
    }
}
