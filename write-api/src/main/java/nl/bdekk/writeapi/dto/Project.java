package nl.bdekk.writeapi.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class Project {

    @XmlElement
    String name;

    @XmlElement
    List<String> files;


    @XmlElement
    List<String> committers;

    @XmlElement
    int complete;

    @XmlElement
    String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public List<String> getCommitters() {
        return committers;
    }

    public void setCommitters(List<String> committers) {
        this.committers = committers;
    }

    public int getComplete() {
        return complete;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
