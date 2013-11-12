package edu.sjsu.courseware.jnlp;

import javax.xml.bind.annotation.XmlAttribute;

//<jar href="jars/AssignmentApplet.jar" main="true" />
public class Jar {
    String href;;
    boolean main = true;

    @XmlAttribute
    public String getHref() {
        return href;
    }
    public void setHref(String href) {
        this.href = href;
    }

    @XmlAttribute
    public boolean isMain() {
        return main;
    }
    public void setMain(boolean main) {
        this.main = main;
    }
}
