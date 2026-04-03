@echo off
cd /d "%~dp0"
java -cp "bin;lib\mysql-connector-j-8.0.33.jar" main.PlacementSystem
pause
