package edu.sjsu.courseware.jnlp;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

//<?xml version="1.0" encoding="UTF-8"?>
//<jnlp href="">
//    <information>
//        <title>Assignment Applet</title>
//        <vendor>SJSU/vendor>
//    </information>
//    <resources>
//        <!-- Application Resources -->
//        <j2se version="1.7+" />
//        <jar href="jars/AssignmentApplet.jar" main="true" />
//    </resources>
//    <applet-desc 
//         name="Assignment Applet"
//         main-class="appletComponentArch.DynamicTreeApplet"
//         width="300"
//         height="300">
//     </applet-desc>
//     <update check="background"/>
//</jnlp>

@XmlRootElement
@XmlType(propOrder = { "information", "resources", "applet", "update" })
public class JNLP {
    Applet applet = new Applet();
    Update update = new Update();
    Resources resources = new Resources();
    Information information = new Information();
    String href = "applet.jnlp";

    @XmlAttribute
    public String getHref() {
        return href;
    }
    public void setHref(String href) {
        this.href = href;
    }
    public Information getInformation() {
        return information;
    }
    public void setInformation(Information information) {
        this.information = information;
    }
    public Resources getResources() {
        return resources;
    }
    public void setResources(Resources resources) {
        this.resources = resources;
    }

    @XmlElement(name = "applet-desc")
    public Applet getApplet() {
        return applet;
    }
    public void setApplet(Applet applet) {
        this.applet = applet;
    }
    public Update getUpdate() {
        return update;
    }
    public void setUpdate(Update update) {
        this.update = update;
    }
}

