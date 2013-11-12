package edu.sjsu.courseware.jnlp;

import javax.xml.bind.annotation.XmlAttribute;

//<update check="background"/>

public class Update {
    String check = "background";

    @XmlAttribute
    public String getCheck() {
        return check;
    }
    public void setCheck(String check) {
        this.check = check;
    }
}
