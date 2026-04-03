package main.service;

import main.model.Company;
import main.model.Project;
import main.model.Student;
import java.util.ArrayList;
import java.util.Scanner;

public class ShortlistService {
    private StudentService studentService;
    private CompanyService companyService;

    public ShortlistService(StudentService studentService, CompanyService companyService) {
        this.studentService = studentService;
        this.companyService = companyService;
    }

    public void shortlistStudents(Scanner scanner) {
        System.out.println("\n========== Shortlist Students for Company ==========");

        if (companyService.getCompanies().isEmpty()) {
            System.out.println("No companies available. Please add companies first.");
            return;
        }

        System.out.println("\n--- Available Companies ---");
        int index = 1;
        for (Company company : companyService.getCompanies()) {
            System.out.println(index++ + ". " + company.getName() +
                    " (Eligible CGPA: " + company.getEligibleCGPA() + ")");
        }

        System.out.print("\nSelect a company (enter number): ");
        int companyChoice = scanner.nextInt();
        scanner.nextLine();

        if (companyChoice < 1 || companyChoice > companyService.getCompanies().size()) {
            System.out.println("Invalid selection");
            return;
        }

        Company selectedCompany = companyService.getCompanies().get(companyChoice - 1);
        System.out.println("\nSelected Company: " + selectedCompany.getName());

        showFilterMenu(scanner, selectedCompany);
    }

    private void showFilterMenu(Scanner scanner, Company company) {
        System.out.println("\n--- Filtering Options ---");
        System.out.println("a) Filter by CGPA");
        System.out.println("b) Filter by Number of Projects");
        System.out.println("c) Filter by Project Domain");
        System.out.println("d) Filter by Technologies");
        System.out.println("e) Filter by Department");
        System.out.println("f) Filter by Batch");
        System.out.println("g) Combined Criteria (CGPA >= X and Projects >= Y)");
        System.out.println("h) Display ALL eligible students (no filter)");
        System.out.print("Enter choice: ");

        String choice = scanner.nextLine().toLowerCase();

        ArrayList<Student> eligibleStudents = new ArrayList<>();

        switch (choice) {
            case "a":
                System.out.print("Enter Minimum CGPA: ");
                double minCGPA = scanner.nextDouble();
                scanner.nextLine();
                eligibleStudents = filterByCGPA(minCGPA);
                break;

            case "b":
                System.out.print("Enter Minimum Number of Projects: ");
                int minProjects = scanner.nextInt();
                scanner.nextLine();
                eligibleStudents = filterByProjectCount(minProjects);
                break;

            case "c":
                System.out.print("Enter Project Domain: ");
                String domain = scanner.nextLine();
                eligibleStudents = filterByDomain(domain);
                break;

            case "d":
                System.out.print("Enter Technology: ");
                String technology = scanner.nextLine();
                eligibleStudents = filterByTechnology(technology);
                break;

            case "e":
                System.out.print("Enter Department: ");
                String department = scanner.nextLine();
                eligibleStudents = filterByDepartment(department);
                break;

            case "f":
                System.out.print("Enter Batch: ");
                String batch = scanner.nextLine();
                eligibleStudents = filterByBatch(batch);
                break;

            case "g":
                System.out.print("Enter Minimum CGPA: ");
                double cgpa = scanner.nextDouble();
                System.out.print("Enter Minimum Number of Projects: ");
                int projects = scanner.nextInt();
                scanner.nextLine();
                eligibleStudents = filterByCombinedCriteria(cgpa, projects);
                break;

            case "h":
                eligibleStudents = filterByCGPA(company.getEligibleCGPA());
                break;

            default:
                System.out.println("Invalid choice");
                return;
        }

        displayShortlistedStudents(eligibleStudents, company);
    }

    private ArrayList<Student> filterByCGPA(double minCGPA) {
        ArrayList<Student> result = new ArrayList<>();
        for (Student student : studentService.getStudents()) {
            if (student.getCgpa() >= minCGPA) {
                result.add(student);
            }
        }
        return result;
    }

    private ArrayList<Student> filterByProjectCount(int minProjects) {
        ArrayList<Student> result = new ArrayList<>();
        for (Student student : studentService.getStudents()) {
            if (student.getProjects().size() >= minProjects) {
                result.add(student);
            }
        }
        return result;
    }

    private ArrayList<Student> filterByDomain(String domain) {
        ArrayList<Student> result = new ArrayList<>();
        for (Student student : studentService.getStudents()) {
            for (Project project : student.getProjects()) {
                if (project.getDomain().toLowerCase().contains(domain.toLowerCase())) {
                    result.add(student);
                    break;
                }
            }
        }
        return result;
    }

    private ArrayList<Student> filterByTechnology(String technology) {
        ArrayList<Student> result = new ArrayList<>();
        for (Student student : studentService.getStudents()) {
            for (Project project : student.getProjects()) {
                if (project.getTechnologies().toLowerCase().contains(technology.toLowerCase())) {
                    result.add(student);
                    break;
                }
            }
        }
        return result;
    }

    private ArrayList<Student> filterByDepartment(String department) {
        ArrayList<Student> result = new ArrayList<>();
        for (Student student : studentService.getStudents()) {
            if (student.getDepartment().equalsIgnoreCase(department)) {
                result.add(student);
            }
        }
        return result;
    }

    private ArrayList<Student> filterByBatch(String batch) {
        ArrayList<Student> result = new ArrayList<>();
        for (Student student : studentService.getStudents()) {
            if (student.getBatch().equalsIgnoreCase(batch)) {
                result.add(student);
            }
        }
        return result;
    }

    private ArrayList<Student> filterByCombinedCriteria(double minCGPA, int minProjects) {
        ArrayList<Student> result = new ArrayList<>();
        for (Student student : studentService.getStudents()) {
            if (student.getCgpa() >= minCGPA && student.getProjects().size() >= minProjects) {
                result.add(student);
            }
        }
        return result;
    }

    private void displayShortlistedStudents(ArrayList<Student> students, Company company) {
        System.out.println("\n========== Shortlisted Students for " + company.getName() + " ==========");

        if (students.isEmpty()) {
            System.out.println("No students match the criteria.");
            return;
        }

        System.out.println("Total Shortlisted: " + students.size());
        System.out.println();
        System.out.println(String.format("%-20s | %-15s | %-15s | %-6s | %-6s | %-12s | %-10s",
                "Name", "Register No", "Department", "CGPA", "Year", "Batch", "Projects"));
        System.out.println("=".repeat(120));

        for (Student student : students) {
            System.out.println(String.format("%-20s | %-15s | %-15s | %-6.2f | %-6s | %-12s | %-10d",
                    student.getName(), student.getRegisterNumber(), student.getDepartment(),
                    student.getCgpa(), student.getYear(), student.getBatch(),
                    student.getProjects().size()));
        }
    }
}
