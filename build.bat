@echo off
rem Needs JDK 21. Result: build\libs\1.0.0\prism-ui-1.0.0+mc1.21.11.jar
call gradlew.bat buildActive
pause
