package main.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Student implements Serializable {
    private String name;
    private String registerNumber;
    private String department;
    private double cgpa;
    private String year;
    private String batch;
    private ArrayList<Project> projects;

    public Student() {
        this.projects = new ArrayList<>();
    }

    public Student(String name, String registerNumber, String department, double cgpa, String year, String batch) {
        this.name = name;
        this.registerNumber = registerNumber;
        this.department = department;
        this.cgpa = cgpa;
        this.year = year;
        this.batch = batch;
        this.projects = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getCgpa() {
        return cgpa;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public ArrayList<Project> getProjects() {
        return projects;
    }

    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
    }

    public void addProject(Project project) {
        this.projects.add(project);
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", registerNumber='" + registerNumber + '\'' +
                ", department='" + department + '\'' +
                ", cgpa=" + cgpa +
                ", year='" + year + '\'' +
                ", batch='" + batch + '\'' +
                ", projects=" + projects.size() +
                '}';
    }

    public String toFileString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("|")
                .append(registerNumber).append("|")
                .append(department).append("|")
                .append(cgpa).append("|")
                .append(year).append("|")
                .append(batch).append("|");

        for (int i = 0; i < projects.size(); i++) {
            sb.append(projects.get(i).toFileString());
            if (i < projects.size() - 1) {
                sb.append(";");
            }
        }
        return sb.toString();
    }

    public static Student fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length >= 6) {
            Student student = new Student(
                    parts[0], parts[1], parts[2],
                    Double.parseDouble(parts[3]), parts[4], parts[5]);

            if (parts.length > 6 && !parts[6].isEmpty()) {
                String[] projects = parts[6].split(";");
                for (String projectStr : projects) {
                    if (!projectStr.trim().isEmpty()) {
                        Project project = Project.fromFileString(projectStr);
                        if (project != null) {
                            student.addProject(project);
                        }
                    }
                }
            }
            return student;
        }
        return null;
    }
}
