package nl.bdekk.writeapi.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.io.File;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY )
public class Project {
    private String name;
    private List<File> files;
    private List<String> committers;
    private int complete;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
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
}
