@echo off
echo ============================================
echo    Placement Management System - Compile
echo ============================================
echo.

REM Check if lib folder exists with MySQL connector
if not exist "lib\mysql-connector-j-8.0.33.jar" (
    echo WARNING: MySQL JDBC driver not found in lib folder!
    echo Please download mysql-connector-j-8.x.x.jar and place it in the lib folder.
    echo Download from: https://dev.mysql.com/downloads/connector/j/
    echo.
)

REM Create bin directory if it doesn't exist
if not exist "bin" mkdir bin

echo Compiling Java files...

REM Compile with MySQL JDBC driver in classpath
if exist "lib\mysql-connector-j-8.0.33.jar" (
    javac -d bin -sourcepath src -cp "lib\mysql-connector-j-8.0.33.jar" src\main\PlacementSystem.java
) else (
    REM Try with any mysql connector jar in lib folder
    for %%f in (lib\mysql-connector*.jar) do (
        javac -d bin -sourcepath src -cp "%%f" src\main\PlacementSystem.java
        goto :compiled
    )
    REM Fallback: compile without MySQL driver (will fail at runtime for DB operations)
    javac -d bin -sourcepath src src\main\PlacementSystem.java
)
:compiled

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ============================================
    echo    Compilation Successful!
    echo ============================================
    echo Run the application using: run.bat
) else (
    echo.
    echo ============================================
    echo    Compilation Failed!
    echo ============================================
    echo Check the error messages above.
)

echo.
pause
