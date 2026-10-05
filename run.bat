@echo off
setlocal
mvn clean compile
if errorlevel 1 exit /b 1
mvn exec:java
endlocal
