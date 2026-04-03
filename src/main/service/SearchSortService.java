package main.service;

import main.model.Project;
import main.model.Student;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class SearchSortService {
    private StudentService studentService;

    public SearchSortService(StudentService studentService) {
        this.studentService = studentService;
    }

    public void showSearchSortMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n========== Searching & Sorting Page ==========");
            System.out.println("--- Sorting Options ---");
            System.out.println("1. Sort by CGPA");
            System.out.println("2. Sort by Number of Projects");
            System.out.println("3. Sort by Project Domain");
            System.out.println("4. Sort by Technologies");
            System.out.println("5. Sort by Department");
            System.out.println("6. Sort by Batch");
            System.out.println("7. Sort by Name");
            System.out.println("\n--- Searching Options ---");
            System.out.println("8. Search by Register Number");
            System.out.println("9. Search by Name");
            System.out.println("10. Search by CGPA >= value");
            System.out.println("11. Search by Project Domain");
            System.out.println("12. Search by Technologies");
            System.out.println("13. Search by Batch");
            System.out.println("14. Search by Department");
            System.out.println("15. Back to Main Menu");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    sortByCGPA();
                    break;
                case 2:
                    sortByNumberOfProjects();
                    break;
                case 3:
                    sortByProjectDomain(scanner);
                    break;
                case 4:
                    sortByTechnologies(scanner);
                    break;
                case 5:
                    sortByDepartment();
                    break;
                case 6:
                    sortByBatch();
                    break;
                case 7:
                    sortByName();
                    break;
                case 8:
                    searchByRegisterNumber(scanner);
                    break;
                case 9:
                    searchByName(scanner);
                    break;
                case 10:
                    searchByCGPA(scanner);
                    break;
                case 11:
                    searchByProjectDomain(scanner);
                    break;
                case 12:
                    searchByTechnologies(scanner);
                    break;
                case 13:
                    searchByBatch(scanner);
                    break;
                case 14:
                    searchByDepartment(scanner);
                    break;
                case 15:
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private void sortByCGPA() {
        ArrayList<Student> students = new ArrayList<>(studentService.getStudents());
        Collections.sort(students, (s1, s2) -> Double.compare(s2.getCgpa(), s1.getCgpa()));
        displayStudentList(students, "Sorted by CGPA (Descending)");
    }

    private void sortByNumberOfProjects() {
        ArrayList<Student> students = new ArrayList<>(studentService.getStudents());
        Collections.sort(students, (s1, s2) -> Integer.compare(s2.getProjects().size(), s1.getProjects().size()));
        displayStudentList(students, "Sorted by Number of Projects (Descending)");
    }

    private void sortByProjectDomain(Scanner scanner) {
        System.out.print("Enter Domain: ");
        String domain = scanner.nextLine();

        ArrayList<Student> filtered = new ArrayList<>();
        for (Student student : studentService.getStudents()) {
            for (Project project : student.getProjects()) {
                if (project.getDomain().toLowerCase().contains(domain.toLowerCase())) {
                    filtered.add(student);
                    break;
                }
            }
        }
        displayStudentList(filtered, "Students with Domain: " + domain);
    }

    private void sortByTechnologies(Scanner scanner) {
        System.out.print("Enter Technology: ");
        String technology = scanner.nextLine();

        ArrayList<Student> filtered = new ArrayList<>();
        for (Student student : studentService.getStudents()) {
            for (Project project : student.getProjects()) {
                if (project.getTechnologies().toLowerCase().contains(technology.toLowerCase())) {
                    filtered.add(student);
                    break;
                }
            }
        }
        displayStudentList(filtered, "Students using Technology: " + technology);
    }

    private void sortByDepartment() {
        ArrayList<Student> students = new ArrayList<>(studentService.getStudents());
        Collections.sort(students, Comparator.comparing(Student::getDepartment));
        displayStudentList(students, "Sorted by Department");
    }

    private void sortByBatch() {
        ArrayList<Student> students = new ArrayList<>(studentService.getStudents());
        Collections.sort(students, Comparator.comparing(Student::getBatch));
        displayStudentList(students, "Sorted by Batch");
    }

    private void sortByName() {
        ArrayList<Student> students = new ArrayList<>(studentService.getStudents());
        Collections.sort(students, Comparator.comparing(Student::getName));
        displayStudentList(students, "Sorted by Name");
    }

    private void searchByRegisterNumber(Scanner scanner) {
        System.out.print("Enter Register Number: ");
        String registerNumber = scanner.nextLine();

        Student student = studentService.findStudentByRegisterNumber(registerNumber);
        if (student != null) {
            ArrayList<Student> result = new ArrayList<>();
            result.add(student);
            displayStudentList(result, "Search Result");
        } else {
            System.out.println("Student Not Found");
        }
    }

    private void searchByName(Scanner scanner) {
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();

        ArrayList<Student> results = new ArrayList<>();
        for (Student student : studentService.getStudents()) {
            if (student.getName().toLowerCase().contains(name.toLowerCase())) {
                results.add(student);
            }
        }

        if (results.isEmpty()) {
            System.out.println("No students found");
        } else {
            displayStudentList(results, "Search Results for Name: " + name);
        }
    }

    private void searchByCGPA(Scanner scanner) {
        System.out.print("Enter Minimum CGPA: ");
        double minCGPA = scanner.nextDouble();
        scanner.nextLine();

        ArrayList<Student> results = new ArrayList<>();
        for (Student student : studentService.getStudents()) {
            if (student.getCgpa() >= minCGPA) {
                results.add(student);
            }
        }

        if (results.isEmpty()) {
            System.out.println("No students found with CGPA >= " + minCGPA);
        } else {
            displayStudentList(results, "Students with CGPA >= " + minCGPA);
        }
    }

    private void searchByProjectDomain(Scanner scanner) {
        System.out.print("Enter Project Domain: ");
        String domain = scanner.nextLine();

        ArrayList<Student> results = new ArrayList<>();
        for (Student student : studentService.getStudents()) {
            for (Project project : student.getProjects()) {
                if (project.getDomain().toLowerCase().contains(domain.toLowerCase())) {
                    results.add(student);
                    break;
                }
            }
        }

        if (results.isEmpty()) {
            System.out.println("No students found with domain: " + domain);
        } else {
            displayStudentList(results, "Students with Domain: " + domain);
        }
    }

    private void searchByTechnologies(Scanner scanner) {
        System.out.print("Enter Technology: ");
        String technology = scanner.nextLine();

        ArrayList<Student> results = new ArrayList<>();
        for (Student student : studentService.getStudents()) {
            for (Project project : student.getProjects()) {
                if (project.getTechnologies().toLowerCase().contains(technology.toLowerCase())) {
                    results.add(student);
                    break;
                }
            }
        }

        if (results.isEmpty()) {
            System.out.println("No students found using technology: " + technology);
        } else {
            displayStudentList(results, "Students using Technology: " + technology);
        }
    }

    private void searchByBatch(Scanner scanner) {
        System.out.print("Enter Batch: ");
        String batch = scanner.nextLine();

        ArrayList<Student> results = new ArrayList<>();
        for (Student student : studentService.getStudents()) {
            if (student.getBatch().equalsIgnoreCase(batch)) {
                results.add(student);
            }
        }

        if (results.isEmpty()) {
            System.out.println("No students found in batch: " + batch);
        } else {
            displayStudentList(results, "Students in Batch: " + batch);
        }
    }

    private void searchByDepartment(Scanner scanner) {
        System.out.print("Enter Department: ");
        String department = scanner.nextLine();

        ArrayList<Student> results = new ArrayList<>();
        for (Student student : studentService.getStudents()) {
            if (student.getDepartment().equalsIgnoreCase(department)) {
                results.add(student);
            }
        }

        if (results.isEmpty()) {
            System.out.println("No students found in department: " + department);
        } else {
            displayStudentList(results, "Students in Department: " + department);
        }
    }

    private void displayStudentList(ArrayList<Student> students, String title) {
        System.out.println("\n========== " + title + " ==========");
        if (students.isEmpty()) {
            System.out.println("No students to display.");
            return;
        }

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
