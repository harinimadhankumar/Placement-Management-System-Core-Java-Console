package main.service;

import main.model.Company;
import main.util.DatabaseHandler;
import java.util.ArrayList;
import java.util.Scanner;

public class CompanyService {
    private ArrayList<Company> companies;

    public CompanyService(ArrayList<Company> companies) {
        this.companies = companies;
    }

    public void addCompany(Scanner scanner) {
        System.out.println("\n========== Add Company ==========");

        System.out.print("Enter Company Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Eligible CGPA: ");
        double eligibleCGPA = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter Job Role: ");
        String jobRole = scanner.nextLine();

        System.out.print("Enter Drive Date (DD-MM-YYYY): ");
        String driveDate = scanner.nextLine();

        Company company = new Company(name, eligibleCGPA, jobRole, driveDate);
        companies.add(company);
        DatabaseHandler.saveCompany(company);

        System.out.println("Company added successfully!");
    }

    public void viewCompany(Scanner scanner) {
        System.out.println("\n========== View Company ==========");
        System.out.print("Enter Company Name: ");
        String name = scanner.nextLine();

        Company company = findCompanyByName(name);
        if (company != null) {
            displayCompanyDetails(company);
        } else {
            System.out.println("Company Not Found");
        }
    }

    public void updateCompany(Scanner scanner) {
        System.out.println("\n========== Update Company ==========");
        System.out.print("Enter Company Name: ");
        String name = scanner.nextLine();

        Company company = findCompanyByName(name);
        if (company == null) {
            System.out.println("Company Not Found");
            return;
        }

        System.out.println("\nCurrent Details:");
        displayCompanyDetails(company);

        System.out.println("\n--- Update Options ---");
        System.out.println("1. Update Eligible CGPA");
        System.out.println("2. Update Job Role");
        System.out.println("3. Update Drive Date");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.print("Enter New Eligible CGPA: ");
                company.setEligibleCGPA(scanner.nextDouble());
                scanner.nextLine();
                break;
            case 2:
                System.out.print("Enter New Job Role: ");
                company.setJobRole(scanner.nextLine());
                break;
            case 3:
                System.out.print("Enter New Drive Date: ");
                company.setDriveDate(scanner.nextLine());
                break;
            default:
                System.out.println("Invalid choice");
                return;
        }

        DatabaseHandler.saveCompany(company);
        System.out.println("Company updated successfully!");
    }

    public void deleteCompany(Scanner scanner) {
        System.out.println("\n========== Delete Company ==========");
        System.out.print("Enter Company Name: ");
        String name = scanner.nextLine();

        Company company = findCompanyByName(name);
        if (company != null) {
            companies.remove(company);
            DatabaseHandler.deleteCompany(name);
            System.out.println("Company deleted successfully!");
        } else {
            System.out.println("Company Not Found");
        }
    }

    public void viewAllCompanies() {
        System.out.println("\n========== All Companies ==========");
        if (companies.isEmpty()) {
            System.out.println("No companies found.");
            return;
        }

        System.out.println(String.format("%-30s | %-15s | %-25s | %-15s",
                "Company Name", "Eligible CGPA", "Job Role", "Drive Date"));
        System.out.println("=".repeat(100));

        for (Company company : companies) {
            System.out.println(String.format("%-30s | %-15.2f | %-25s | %-15s",
                    company.getName(), company.getEligibleCGPA(),
                    company.getJobRole(), company.getDriveDate()));
        }
    }

    public Company findCompanyByName(String name) {
        for (Company company : companies) {
            if (company.getName().equalsIgnoreCase(name)) {
                return company;
            }
        }
        return null;
    }

    public void displayCompanyDetails(Company company) {
        System.out.println("\n--- Company Details ---");
        System.out.println("Company Name: " + company.getName());
        System.out.println("Eligible CGPA: " + company.getEligibleCGPA());
        System.out.println("Job Role: " + company.getJobRole());
        System.out.println("Drive Date: " + company.getDriveDate());
    }

    public ArrayList<Company> getCompanies() {
        return companies;
    }
}
