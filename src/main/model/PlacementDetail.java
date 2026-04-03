package main.model;

import java.io.Serializable;

public class PlacementDetail implements Serializable {
    private String registerNumber;
    private String name;
    private String department;
    private String batch;
    private String companyName;
    private double salaryPackage;

    public PlacementDetail() {
    }

    public PlacementDetail(String registerNumber, String name, String department, String batch,
            String companyName, double salaryPackage) {
        this.registerNumber = registerNumber;
        this.name = name;
        this.department = department;
        this.batch = batch;
        this.companyName = companyName;
        this.salaryPackage = salaryPackage;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public double getSalaryPackage() {
        return salaryPackage;
    }

    public void setSalaryPackage(double salaryPackage) {
        this.salaryPackage = salaryPackage;
    }

    @Override
    public String toString() {
        return String.format("%-15s | %-20s | %-15s | %-12s | %-25s | %.2f LPA",
                registerNumber, name, department, batch, companyName, salaryPackage);
    }

    public String toFileString() {
        return registerNumber + "|" + name + "|" + department + "|" + batch + "|" +
                companyName + "|" + salaryPackage;
    }

    public static PlacementDetail fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length == 6) {
            return new PlacementDetail(
                    parts[0], parts[1], parts[2], parts[3],
                    parts[4], Double.parseDouble(parts[5]));
        }
        return null;
    }
}
