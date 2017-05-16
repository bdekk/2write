package nl.bdekk.productapi.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class Project {

    @XmlElement
    long id;

    @XmlElement
    String title;

    @XmlElement
    String description;

    @XmlElement
    List<ProjectFile> files;


    @XmlElement
    List<User> committers;

    @XmlElement
    int complete;

    public List<ProjectFile> getFiles() {
        return files;
    }

    public void setFiles(List<ProjectFile> files) {
        this.files = files;
    }

    public List<User> getCommitters() {
        return committers;
    }

    public void setCommitters(List<User> committers) {
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
