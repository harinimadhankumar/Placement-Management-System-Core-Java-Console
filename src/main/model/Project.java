package main.model;

import java.io.Serializable;

public class Project implements Serializable {
    private String title;
    private String description;
    private String domain;
    private String technologies;

    public Project() {
    }

    public Project(String title, String description, String domain, String technologies) {
        this.title = title;
        this.description = description;
        this.domain = domain;
        this.technologies = technologies;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getTechnologies() {
        return technologies;
    }

    public void setTechnologies(String technologies) {
        this.technologies = technologies;
    }

    @Override
    public String toString() {
        return "Project{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", domain='" + domain + '\'' +
                ", technologies='" + technologies + '\'' +
                '}';
    }

    public String toFileString() {
        return title + "~" + description + "~" + domain + "~" + technologies;
    }

    public static Project fromFileString(String line) {
        String[] parts = line.split("~", 4);
        if (parts.length == 4) {
            return new Project(parts[0], parts[1], parts[2], parts[3]);
        }
        return null;
    }
}
