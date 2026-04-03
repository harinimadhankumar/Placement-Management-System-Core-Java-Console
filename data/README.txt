================================================================================
                    PLACEMENT MANAGEMENT SYSTEM - README
================================================================================

A Java console application for managing student placements with MySQL database.

================================================================================
FEATURES:
================================================================================

1. Student Management
   - Add, View, Update, Delete students
   - Manage student projects

2. Company Management
   - Add, View, Update, Delete companies
   - Track eligible CGPA and drive dates

3. Placement Management
   - Record student placements
   - Track salary packages

4. Search & Sort
   - Sort by CGPA, Projects, Department, Batch
   - Search by various criteria

5. Shortlisting
   - Shortlist students for companies based on criteria

================================================================================
REQUIREMENTS:
================================================================================

- Java JDK 17 or higher
- MySQL Server 8.0 or higher
- MySQL JDBC Driver (included in lib/)

================================================================================
SETUP:
================================================================================

1. Create MySQL database:
   CREATE DATABASE placement_management;

2. Configure .env file with your MySQL credentials:
   DB_HOST=localhost
   DB_PORT=3306
   DB_NAME=placement_management
   DB_USER=root
   DB_PASSWORD=your_password

3. Run the application:
   - Double-click run.bat, OR
   - Run from IDE: main.PlacementSystem

================================================================================
PROJECT STRUCTURE:
================================================================================

src/main/
├── PlacementSystem.java      # Main entry point
├── model/                    # Data models
│   ├── Student.java
│   ├── Project.java
│   ├── Company.java
│   └── PlacementDetail.java
├── service/                  # Business logic
│   ├── StudentService.java
│   ├── CompanyService.java
│   ├── PlacementService.java
│   ├── SearchSortService.java
│   └── ShortlistService.java
└── util/                     # Utilities
    ├── DatabaseConfig.java
    └── DatabaseHandler.java

================================================================================
