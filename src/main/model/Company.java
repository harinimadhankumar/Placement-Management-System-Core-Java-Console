package main.model;

import java.io.Serializable;

public class Company implements Serializable {
    private String name;
    private double eligibleCGPA;
    private String jobRole;
    private String driveDate;

    public Company() {
    }

    public Company(String name, double eligibleCGPA, String jobRole, String driveDate) {
        this.name = name;
        this.eligibleCGPA = eligibleCGPA;
        this.jobRole = jobRole;
        this.driveDate = driveDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getEligibleCGPA() {
        return eligibleCGPA;
    }

    public void setEligibleCGPA(double eligibleCGPA) {
        this.eligibleCGPA = eligibleCGPA;
    }

    public String getJobRole() {
        return jobRole;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    public String getDriveDate() {
        return driveDate;
    }

    public void setDriveDate(String driveDate) {
        this.driveDate = driveDate;
    }

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                ", eligibleCGPA=" + eligibleCGPA +
                ", jobRole='" + jobRole + '\'' +
                ", driveDate='" + driveDate + '\'' +
                '}';
    }

    public String toFileString() {
        return name + "|" + eligibleCGPA + "|" + jobRole + "|" + driveDate;
    }

    public static Company fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length == 4) {
            return new Company(parts[0], Double.parseDouble(parts[1]), parts[2], parts[3]);
        }
        return null;
    }
}
