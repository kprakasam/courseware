package edu.sjsu.courseware.jnlp;

import javax.xml.bind.annotation.XmlAttribute;

//<j2se version="1.7+" />
public class J2SE {
    String version = "1.7+";

    @XmlAttribute
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
