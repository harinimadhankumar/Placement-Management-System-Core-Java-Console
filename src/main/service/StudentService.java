package main.service;

import main.model.Project;
import main.model.Student;
import main.util.DatabaseHandler;
import java.util.ArrayList;
import java.util.Scanner;

public class StudentService {
    private ArrayList<Student> students;

    public StudentService(ArrayList<Student> students) {
        this.students = students;
    }

    public void addStudent(Scanner scanner) {
        System.out.println("\n========== Add Student ==========");

        System.out.print("Enter Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Register Number: ");
        String registerNumber = scanner.nextLine();

        for (Student s : students) {
            if (s.getRegisterNumber().equalsIgnoreCase(registerNumber)) {
                System.out.println("Error: Register Number already exists!");
                return;
            }
        }

        System.out.print("Enter Department: ");
        String department = scanner.nextLine();

        System.out.print("Enter CGPA: ");
        double cgpa = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter Year (I/II/III/IV): ");
        String year = scanner.nextLine();

        System.out.print("Enter Batch (ex: 2023-2027): ");
        String batch = scanner.nextLine();

        Student student = new Student(name, registerNumber, department, cgpa, year, batch);

        System.out.print("Enter number of projects: ");
        int numProjects = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < numProjects; i++) {
            System.out.println("\n--- Project " + (i + 1) + " ---");

            System.out.print("Enter Project Title: ");
            String title = scanner.nextLine();

            System.out.print("Enter Project Description: ");
            String description = scanner.nextLine();

            System.out.print("Enter Project Domain: ");
            String domain = scanner.nextLine();

            System.out.print("Enter Technologies: ");
            String technologies = scanner.nextLine();

            Project project = new Project(title, description, domain, technologies);
            student.addProject(project);
        }

        students.add(student);
        DatabaseHandler.saveStudent(student);
        System.out.println("\nStudent added successfully!");
    }

    public void viewStudent(Scanner scanner) {
        System.out.println("\n========== View Student ==========");
        System.out.print("Enter Register Number: ");
        String registerNumber = scanner.nextLine();

        Student student = findStudentByRegisterNumber(registerNumber);
        if (student != null) {
            displayStudentDetails(student);
        } else {
            System.out.println("Student Not Found");
        }
    }

    public void updateStudent(Scanner scanner) {
        System.out.println("\n========== Update Student ==========");
        System.out.print("Enter Register Number: ");
        String registerNumber = scanner.nextLine();

        Student student = findStudentByRegisterNumber(registerNumber);
        if (student == null) {
            System.out.println("Student Not Found");
            return;
        }

        System.out.println("\nCurrent Details:");
        displayStudentDetails(student);

        System.out.println("\n--- Update Options ---");
        System.out.println("1. Update Name");
        System.out.println("2. Update Department");
        System.out.println("3. Update CGPA");
        System.out.println("4. Update Year");
        System.out.println("5. Update Batch");
        System.out.println("6. Add Project");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.print("Enter New Name: ");
                student.setName(scanner.nextLine());
                break;
            case 2:
                System.out.print("Enter New Department: ");
                student.setDepartment(scanner.nextLine());
                break;
            case 3:
                System.out.print("Enter New CGPA: ");
                student.setCgpa(scanner.nextDouble());
                scanner.nextLine();
                break;
            case 4:
                System.out.print("Enter New Year: ");
                student.setYear(scanner.nextLine());
                break;
            case 5:
                System.out.print("Enter New Batch: ");
                student.setBatch(scanner.nextLine());
                break;
            case 6:
                System.out.print("Enter Project Title: ");
                String title = scanner.nextLine();
                System.out.print("Enter Project Description: ");
                String description = scanner.nextLine();
                System.out.print("Enter Project Domain: ");
                String domain = scanner.nextLine();
                System.out.print("Enter Technologies: ");
                String technologies = scanner.nextLine();
                student.addProject(new Project(title, description, domain, technologies));
                break;
            default:
                System.out.println("Invalid choice");
                return;
        }

        DatabaseHandler.saveStudent(student);
        System.out.println("Student updated successfully!");
    }

    public void deleteStudent(Scanner scanner) {
        System.out.println("\n========== Delete Student ==========");
        System.out.print("Enter Register Number: ");
        String registerNumber = scanner.nextLine();

        Student student = findStudentByRegisterNumber(registerNumber);
        if (student != null) {
            students.remove(student);
            DatabaseHandler.deleteStudent(registerNumber);
            System.out.println("Student deleted successfully!");
        } else {
            System.out.println("Student Not Found");
        }
    }

    public void viewAllStudents() {
        System.out.println("\n========== All Students ==========");
        if (students.isEmpty()) {
            System.out.println("No students found.");
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

    public Student findStudentByRegisterNumber(String registerNumber) {
        for (Student student : students) {
            if (student.getRegisterNumber().equalsIgnoreCase(registerNumber)) {
                return student;
            }
        }
        return null;
    }

    public void displayStudentDetails(Student student) {
        System.out.println("\n--- Student Details ---");
        System.out.println("Name: " + student.getName());
        System.out.println("Register Number: " + student.getRegisterNumber());
        System.out.println("Department: " + student.getDepartment());
        System.out.println("CGPA: " + student.getCgpa());
        System.out.println("Year: " + student.getYear());
        System.out.println("Batch: " + student.getBatch());
        System.out.println("Number of Projects: " + student.getProjects().size());

        if (!student.getProjects().isEmpty()) {
            System.out.println("\n--- Projects ---");
            int count = 1;
            for (Project project : student.getProjects()) {
                System.out.println("\nProject " + count++ + ":");
                System.out.println("  Title: " + project.getTitle());
                System.out.println("  Description: " + project.getDescription());
                System.out.println("  Domain: " + project.getDomain());
                System.out.println("  Technologies: " + project.getTechnologies());
            }
        }
    }

    public ArrayList<Student> getStudents() {
        return students;
    }
}
