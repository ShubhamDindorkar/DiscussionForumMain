@echo off
REM Set Java 21 as the active Java version
set JAVA_HOME=C:\Program Files\Java\jdk-21
set PATH=%JAVA_HOME%\bin;%PATH%

echo Using Java 21...
java -version
echo.
echo Building and running JavaFX application...
echo.

cd /d "%~dp0"
gradlew.bat clean run

pause
