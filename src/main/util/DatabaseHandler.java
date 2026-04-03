package main.util;

import main.model.*;
import java.sql.*;
import java.util.ArrayList;

/**
 * DatabaseHandler - Handles all MySQL database operations
 */
public class DatabaseHandler {

    /**
     * Get a connection to MySQL database
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(DatabaseConfig.getJdbcDriver());
            return DriverManager.getConnection(
                    DatabaseConfig.getJdbcUrl(),
                    DatabaseConfig.getDbUser(),
                    DatabaseConfig.getDbPassword());
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found! Make sure mysql-connector jar is in lib folder.");
        }
    }

    /**
     * Test if database connection works
     */
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null) {
                conn.close();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
        return false;
    }

    /**
     * Create all required tables if they don't exist
     */
    public static void initializeDatabase() {
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();

            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS students (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY," +
                            "name VARCHAR(100) NOT NULL," +
                            "register_number VARCHAR(50) UNIQUE NOT NULL," +
                            "department VARCHAR(100) NOT NULL," +
                            "cgpa DECIMAL(4,2) NOT NULL," +
                            "year VARCHAR(10) NOT NULL," +
                            "batch VARCHAR(20) NOT NULL)");

            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS projects (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY," +
                            "student_register_number VARCHAR(50) NOT NULL," +
                            "title VARCHAR(200) NOT NULL," +
                            "description TEXT," +
                            "domain VARCHAR(100)," +
                            "technologies VARCHAR(200)," +
                            "FOREIGN KEY (student_register_number) REFERENCES students(register_number) ON DELETE CASCADE)");

            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS companies (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY," +
                            "name VARCHAR(100) UNIQUE NOT NULL," +
                            "eligible_cgpa DECIMAL(4,2) NOT NULL," +
                            "job_role VARCHAR(100) NOT NULL," +
                            "drive_date VARCHAR(20) NOT NULL)");

            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS placements (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY," +
                            "register_number VARCHAR(50) NOT NULL," +
                            "name VARCHAR(100) NOT NULL," +
                            "department VARCHAR(100) NOT NULL," +
                            "batch VARCHAR(20) NOT NULL," +
                            "company_name VARCHAR(100) NOT NULL," +
                            "salary_package DECIMAL(10,2) NOT NULL," +
                            "FOREIGN KEY (register_number) REFERENCES students(register_number) ON DELETE CASCADE)");

            stmt.close();
            conn.close();
            System.out.println("Database tables ready.");

        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }

    // STUDENT OPERATIONS

    public static ArrayList<Student> loadStudents() {
        ArrayList<Student> students = new ArrayList<>();
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM students");

            while (rs.next()) {
                Student student = new Student(
                        rs.getString("name"),
                        rs.getString("register_number"),
                        rs.getString("department"),
                        rs.getDouble("cgpa"),
                        rs.getString("year"),
                        rs.getString("batch"));
                student.setProjects(loadProjectsForStudent(rs.getString("register_number")));
                students.add(student);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error loading students: " + e.getMessage());
        }
        return students;
    }

    private static ArrayList<Project> loadProjectsForStudent(String registerNumber) {
        ArrayList<Project> projects = new ArrayList<>();
        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT * FROM projects WHERE student_register_number = ?");
            pstmt.setString(1, registerNumber);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Project project = new Project(
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("domain"),
                        rs.getString("technologies"));
                projects.add(project);
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error loading projects: " + e.getMessage());
        }
        return projects;
    }

    public static void saveStudent(Student student) {
        try {
            Connection conn = getConnection();
            PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM students WHERE register_number = ?");
            checkStmt.setString(1, student.getRegisterNumber());
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            boolean exists = rs.getInt(1) > 0;
            rs.close();
            checkStmt.close();

            if (exists) {
                PreparedStatement updateStmt = conn.prepareStatement(
                        "UPDATE students SET name=?, department=?, cgpa=?, year=?, batch=? WHERE register_number=?");
                updateStmt.setString(1, student.getName());
                updateStmt.setString(2, student.getDepartment());
                updateStmt.setDouble(3, student.getCgpa());
                updateStmt.setString(4, student.getYear());
                updateStmt.setString(5, student.getBatch());
                updateStmt.setString(6, student.getRegisterNumber());
                updateStmt.executeUpdate();
                updateStmt.close();
            } else {
                PreparedStatement insertStmt = conn.prepareStatement(
                        "INSERT INTO students (name, register_number, department, cgpa, year, batch) VALUES (?,?,?,?,?,?)");
                insertStmt.setString(1, student.getName());
                insertStmt.setString(2, student.getRegisterNumber());
                insertStmt.setString(3, student.getDepartment());
                insertStmt.setDouble(4, student.getCgpa());
                insertStmt.setString(5, student.getYear());
                insertStmt.setString(6, student.getBatch());
                insertStmt.executeUpdate();
                insertStmt.close();
            }
            saveProjectsForStudent(student.getRegisterNumber(), student.getProjects());
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error saving student: " + e.getMessage());
        }
    }

    private static void saveProjectsForStudent(String registerNumber, ArrayList<Project> projects) {
        try {
            Connection conn = getConnection();
            PreparedStatement deleteStmt = conn.prepareStatement(
                    "DELETE FROM projects WHERE student_register_number = ?");
            deleteStmt.setString(1, registerNumber);
            deleteStmt.executeUpdate();
            deleteStmt.close();

            PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO projects (student_register_number, title, description, domain, technologies) VALUES (?,?,?,?,?)");
            for (Project project : projects) {
                insertStmt.setString(1, registerNumber);
                insertStmt.setString(2, project.getTitle());
                insertStmt.setString(3, project.getDescription());
                insertStmt.setString(4, project.getDomain());
                insertStmt.setString(5, project.getTechnologies());
                insertStmt.executeUpdate();
            }
            insertStmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error saving projects: " + e.getMessage());
        }
    }

    public static void saveStudents(ArrayList<Student> students) {
        for (Student student : students) {
            saveStudent(student);
        }
    }

    public static void deleteStudent(String registerNumber) {
        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "DELETE FROM students WHERE register_number = ?");
            pstmt.setString(1, registerNumber);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error deleting student: " + e.getMessage());
        }
    }

    // COMPANY OPERATIONS

    public static ArrayList<Company> loadCompanies() {
        ArrayList<Company> companies = new ArrayList<>();
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM companies");

            while (rs.next()) {
                Company company = new Company(
                        rs.getString("name"),
                        rs.getDouble("eligible_cgpa"),
                        rs.getString("job_role"),
                        rs.getString("drive_date"));
                companies.add(company);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error loading companies: " + e.getMessage());
        }
        return companies;
    }

    public static void saveCompany(Company company) {
        try {
            Connection conn = getConnection();
            PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM companies WHERE name = ?");
            checkStmt.setString(1, company.getName());
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            boolean exists = rs.getInt(1) > 0;
            rs.close();
            checkStmt.close();

            if (exists) {
                PreparedStatement updateStmt = conn.prepareStatement(
                        "UPDATE companies SET eligible_cgpa=?, job_role=?, drive_date=? WHERE name=?");
                updateStmt.setDouble(1, company.getEligibleCGPA());
                updateStmt.setString(2, company.getJobRole());
                updateStmt.setString(3, company.getDriveDate());
                updateStmt.setString(4, company.getName());
                updateStmt.executeUpdate();
                updateStmt.close();
            } else {
                PreparedStatement insertStmt = conn.prepareStatement(
                        "INSERT INTO companies (name, eligible_cgpa, job_role, drive_date) VALUES (?,?,?,?)");
                insertStmt.setString(1, company.getName());
                insertStmt.setDouble(2, company.getEligibleCGPA());
                insertStmt.setString(3, company.getJobRole());
                insertStmt.setString(4, company.getDriveDate());
                insertStmt.executeUpdate();
                insertStmt.close();
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error saving company: " + e.getMessage());
        }
    }

    public static void saveCompanies(ArrayList<Company> companies) {
        for (Company company : companies) {
            saveCompany(company);
        }
    }

    public static void deleteCompany(String name) {
        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "DELETE FROM companies WHERE name = ?");
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error deleting company: " + e.getMessage());
        }
    }

    // PLACEMENT OPERATIONS

    public static ArrayList<PlacementDetail> loadPlacements() {
        ArrayList<PlacementDetail> placements = new ArrayList<>();
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM placements");

            while (rs.next()) {
                PlacementDetail placement = new PlacementDetail(
                        rs.getString("register_number"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getString("batch"),
                        rs.getString("company_name"),
                        rs.getDouble("salary_package"));
                placements.add(placement);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error loading placements: " + e.getMessage());
        }
        return placements;
    }

    public static void savePlacement(PlacementDetail placement) {
        try {
            Connection conn = getConnection();
            PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM placements WHERE register_number = ? AND company_name = ?");
            checkStmt.setString(1, placement.getRegisterNumber());
            checkStmt.setString(2, placement.getCompanyName());
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            boolean exists = rs.getInt(1) > 0;
            rs.close();
            checkStmt.close();

            if (exists) {
                PreparedStatement updateStmt = conn.prepareStatement(
                        "UPDATE placements SET name=?, department=?, batch=?, salary_package=? WHERE register_number=? AND company_name=?");
                updateStmt.setString(1, placement.getName());
                updateStmt.setString(2, placement.getDepartment());
                updateStmt.setString(3, placement.getBatch());
                updateStmt.setDouble(4, placement.getSalaryPackage());
                updateStmt.setString(5, placement.getRegisterNumber());
                updateStmt.setString(6, placement.getCompanyName());
                updateStmt.executeUpdate();
                updateStmt.close();
            } else {
                PreparedStatement insertStmt = conn.prepareStatement(
                        "INSERT INTO placements (register_number, name, department, batch, company_name, salary_package) VALUES (?,?,?,?,?,?)");
                insertStmt.setString(1, placement.getRegisterNumber());
                insertStmt.setString(2, placement.getName());
                insertStmt.setString(3, placement.getDepartment());
                insertStmt.setString(4, placement.getBatch());
                insertStmt.setString(5, placement.getCompanyName());
                insertStmt.setDouble(6, placement.getSalaryPackage());
                insertStmt.executeUpdate();
                insertStmt.close();
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error saving placement: " + e.getMessage());
        }
    }

    public static void savePlacements(ArrayList<PlacementDetail> placements) {
        for (PlacementDetail placement : placements) {
            savePlacement(placement);
        }
    }

    public static void deletePlacement(String registerNumber) {
        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "DELETE FROM placements WHERE register_number = ?");
            pstmt.setString(1, registerNumber);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error deleting placement: " + e.getMessage());
        }
    }
}
