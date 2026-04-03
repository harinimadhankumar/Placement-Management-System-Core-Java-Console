package main.service;

import main.model.PlacementDetail;
import main.model.Student;
import main.util.DatabaseHandler;
import java.util.ArrayList;
import java.util.Scanner;

public class PlacementService {
    private ArrayList<PlacementDetail> placements;
    private StudentService studentService;

    public PlacementService(ArrayList<PlacementDetail> placements, StudentService studentService) {
        this.placements = placements;
        this.studentService = studentService;
    }

    public void addPlacementRecord(Scanner scanner) {
        System.out.println("\n========== Add Placement Record ==========");

        System.out.print("Enter Register Number: ");
        String registerNumber = scanner.nextLine();

        Student student = studentService.findStudentByRegisterNumber(registerNumber);
        if (student == null) {
            System.out.println("Student Not Found. Cannot add placement record.");
            return;
        }

        System.out.println("\n--- Auto-fetched Student Details ---");
        System.out.println("Name: " + student.getName());
        System.out.println("Department: " + student.getDepartment());
        System.out.println("Batch: " + student.getBatch());

        System.out.print("\nEnter Company Name: ");
        String companyName = scanner.nextLine();

        System.out.print("Enter Salary Package (LPA): ");
        double salaryPackage = scanner.nextDouble();
        scanner.nextLine();

        PlacementDetail placement = new PlacementDetail(
                student.getRegisterNumber(),
                student.getName(),
                student.getDepartment(),
                student.getBatch(),
                companyName,
                salaryPackage);

        placements.add(placement);
        DatabaseHandler.savePlacement(placement);
        System.out.println("Placement record added successfully!");
    }

    public void viewPlacementRecord(Scanner scanner) {
        System.out.println("\n========== View Placement Record ==========");
        System.out.print("Enter Register Number: ");
        String registerNumber = scanner.nextLine();

        PlacementDetail placement = findPlacementByRegisterNumber(registerNumber);
        if (placement != null) {
            System.out.println("\n--- Placement Details ---");
            System.out.println(placement);
        } else {
            System.out.println("Placement Record Not Found");
        }
    }

    public void updatePlacementRecord(Scanner scanner) {
        System.out.println("\n========== Update Placement Record ==========");
        System.out.print("Enter Register Number: ");
        String registerNumber = scanner.nextLine();

        PlacementDetail placement = findPlacementByRegisterNumber(registerNumber);
        if (placement == null) {
            System.out.println("Placement Record Not Found");
            return;
        }

        System.out.println("\nCurrent Details:");
        System.out.println(placement);

        System.out.println("\n--- Update Options ---");
        System.out.println("1. Update Company Name");
        System.out.println("2. Update Salary Package");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.print("Enter New Company Name: ");
                placement.setCompanyName(scanner.nextLine());
                break;
            case 2:
                System.out.print("Enter New Salary Package: ");
                placement.setSalaryPackage(scanner.nextDouble());
                scanner.nextLine();
                break;
            default:
                System.out.println("Invalid choice");
                return;
        }

        DatabaseHandler.savePlacement(placement);
        System.out.println("Placement record updated successfully!");
    }

    public void deletePlacementRecord(Scanner scanner) {
        System.out.println("\n========== Delete Placement Record ==========");
        System.out.print("Enter Register Number: ");
        String registerNumber = scanner.nextLine();

        PlacementDetail placement = findPlacementByRegisterNumber(registerNumber);
        if (placement != null) {
            placements.remove(placement);
            DatabaseHandler.deletePlacement(registerNumber);
            System.out.println("Placement record deleted successfully!");
        } else {
            System.out.println("Placement Record Not Found");
        }
    }

    public void viewAllPlacementRecords() {
        System.out.println("\n========== Display All Placement Details ==========");
        if (placements.isEmpty()) {
            System.out.println("No placement records found.");
            return;
        }

        System.out.println(String.format("%-15s | %-20s | %-15s | %-12s | %-25s | %-12s",
                "Register No", "Name", "Department", "Batch", "Company Name", "Salary (LPA)"));
        System.out.println("=".repeat(120));

        for (PlacementDetail placement : placements) {
            System.out.println(placement);
        }
    }

    public PlacementDetail findPlacementByRegisterNumber(String registerNumber) {
        for (PlacementDetail placement : placements) {
            if (placement.getRegisterNumber().equalsIgnoreCase(registerNumber)) {
                return placement;
            }
        }
        return null;
    }

    public ArrayList<PlacementDetail> getPlacements() {
        return placements;
    }
}
