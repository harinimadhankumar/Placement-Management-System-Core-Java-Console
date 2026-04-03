package main;

import main.model.Company;
import main.model.PlacementDetail;
import main.model.Student;
import main.service.*;
import main.util.DatabaseHandler;
import java.util.ArrayList;
import java.util.Scanner;

public class PlacementSystem {
    private ArrayList<Student> students;
    private ArrayList<Company> companies;
    private ArrayList<PlacementDetail> placements;

    private StudentService studentService;
    private CompanyService companyService;
    private PlacementService placementService;
    private SearchSortService searchSortService;
    private ShortlistService shortlistService;

    private Scanner scanner;

    public PlacementSystem() {
        initializeSystem();

        studentService = new StudentService(students);
        companyService = new CompanyService(companies);
        placementService = new PlacementService(placements, studentService);
        searchSortService = new SearchSortService(studentService);
        shortlistService = new ShortlistService(studentService, companyService);

        scanner = new Scanner(System.in);
    }

    private void initializeSystem() {
        System.out.println("\n========== Placement Management System ==========");
        System.out.println("Initializing database connection...");

        // Test database connection
        if (DatabaseHandler.testConnection()) {
            System.out.println("Database connection successful!");

            // Initialize database tables
            DatabaseHandler.initializeDatabase();

            // Load data from database
            students = DatabaseHandler.loadStudents();
            companies = DatabaseHandler.loadCompanies();
            placements = DatabaseHandler.loadPlacements();

            System.out.println("Data loaded successfully from database.");
            System.out.println("  Students: " + students.size());
            System.out.println("  Companies: " + companies.size());
            System.out.println("  Placements: " + placements.size());
        } else {
            System.out.println("ERROR: Database connection failed!");
            System.out.println("Please check your MySQL server and .env configuration.");
            System.out.println("Exiting...");
            System.exit(1);
        }
    }

    public void showMainMenu() {
        while (true) {
            System.out.println("\n============================================");
            System.out.println("    PLACEMENT MANAGEMENT SYSTEM");
            System.out.println("============================================");
            System.out.println("1. Student Management");
            System.out.println("2. Company Management");
            System.out.println("3. Placement Management");
            System.out.println("4. Display All Placement Details");
            System.out.println("5. Searching & Sorting Page");
            System.out.println("6. Shortlist Students for Company");
            System.out.println("7. View Student Profile From Placement");
            System.out.println("8. Exit");
            System.out.println("============================================");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    showStudentManagementMenu();
                    break;
                case 2:
                    showCompanyManagementMenu();
                    break;
                case 3:
                    showPlacementManagementMenu();
                    break;
                case 4:
                    placementService.viewAllPlacementRecords();
                    break;
                case 5:
                    searchSortService.showSearchSortMenu(scanner);
                    break;
                case 6:
                    shortlistService.shortlistStudents(scanner);
                    break;
                case 7:
                    viewStudentProfileFromPlacement(scanner);
                    break;
                case 8:
                    System.out.println("\nThank you for using Placement Management System!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void viewStudentProfileFromPlacement(Scanner scanner) {
        System.out.println("\n========== View Student Profile From Placement ==========");
        System.out.print("Enter Register Number: ");
        String registerNumber = scanner.nextLine();

        Student student = studentService.findStudentByRegisterNumber(registerNumber);
        if (student != null) {
            studentService.displayStudentDetails(student);
        } else {
            System.out.println("Student Not Found");
        }
    }

    private void showStudentManagementMenu() {
        while (true) {
            System.out.println("\n========== Student Management ==========");
            System.out.println("1. Add Student");
            System.out.println("2. View Student");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. View All Students");
            System.out.println("6. Back");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    studentService.addStudent(scanner);
                    break;
                case 2:
                    studentService.viewStudent(scanner);
                    break;
                case 3:
                    studentService.updateStudent(scanner);
                    break;
                case 4:
                    studentService.deleteStudent(scanner);
                    break;
                case 5:
                    studentService.viewAllStudents();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private void showCompanyManagementMenu() {
        while (true) {
            System.out.println("\n========== Company Management ==========");
            System.out.println("1. Add Company");
            System.out.println("2. View Company");
            System.out.println("3. Update Company");
            System.out.println("4. Delete Company");
            System.out.println("5. View All Companies");
            System.out.println("6. Back");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    companyService.addCompany(scanner);
                    break;
                case 2:
                    companyService.viewCompany(scanner);
                    break;
                case 3:
                    companyService.updateCompany(scanner);
                    break;
                case 4:
                    companyService.deleteCompany(scanner);
                    break;
                case 5:
                    companyService.viewAllCompanies();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private void showPlacementManagementMenu() {
        while (true) {
            System.out.println("\n========== Placement Management ==========");
            System.out.println("1. Add Placement Record");
            System.out.println("2. View Placement Record");
            System.out.println("3. Update Placement Record");
            System.out.println("4. Delete Placement Record");
            System.out.println("5. Back");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    placementService.addPlacementRecord(scanner);
                    break;
                case 2:
                    placementService.viewPlacementRecord(scanner);
                    break;
                case 3:
                    placementService.updatePlacementRecord(scanner);
                    break;
                case 4:
                    placementService.deletePlacementRecord(scanner);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    public static void main(String[] args) {
        PlacementSystem system = new PlacementSystem();
        system.showMainMenu();
    }
}
