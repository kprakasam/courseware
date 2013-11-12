package edu.sjsu.courseware.jnlp;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlType;

//<resources>
//<!-- Application Resources -->
//<j2se version="1.7+" />
//<jar href="jars/AssignmentApplet.jar" main="true" />
//</resources>

@XmlType(propOrder = { "j2se", "jar" })
public class Resources {
    J2SE j2se = new J2SE();
    List<Jar> jar = new ArrayList<Jar>();

    public J2SE getJ2se() {
        return j2se;
    }
    public void setJ2se(J2SE j2se) {
        this.j2se = j2se;
    }
    
    public List<Jar> getJar() {
        return jar;
    }
    
    public void setJar(List<Jar> jar) {
        this.jar = jar;
    }
}
