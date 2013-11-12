package edu.sjsu.courseware.jnlp;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

//<applet-desc 
//name="Assignment Applet"
//main-class="appletComponentArch.DynamicTreeApplet"
//width="300"
//height="300">
//</applet-desc>

@XmlRootElement(name = "applet-desc")
public class Applet {
    String name = "Assignment Applet";
    String mainClass = "appletComponentArch.DynamicTreeApplet";
    short width = 300;
    short height = 300;

    @XmlAttribute
       public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute(name="main-class")
    public String getMainClass() {
        return mainClass;
    }
    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    @XmlAttribute
    public short getWidth() {
        return width;
    }
    public void setWidth(short width) {
        this.width = width;
    }

    @XmlAttribute
    public short getHeight() {
        return height;
    }
    public void setHeight(short height) {
        this.height = height;
    }
    
}
