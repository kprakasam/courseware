package edu.sjsu.courseware.json;

import java.util.List;

public class Jar {
    String id;
    String name;
    String mainClass;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public static Jar[] toJars(List<edu.sjsu.courseware.Jar> jarDOs) {
        if (jarDOs == null || jarDOs.isEmpty())
            return new Jar[]{};
        
        int i = 0;
        Jar[] jars = new Jar[jarDOs.size()];
        
        for (edu.sjsu.courseware.Jar jarDO : jarDOs) {
            Jar jar = new Jar();
            jar.id = String.valueOf(jarDO.getId());
            jar.name = jarDO.getName();
            jar.mainClass = jarDO.getMainClass();
            jars[i++] = jar;
        }
        
        return jars;
    }

}
