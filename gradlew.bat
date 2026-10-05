@echo off
powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%~dp0scripts\run-gradle.ps1" -ProjectDirectory "%~dp0." -JavaVersion 21 %*
exit /b %ERRORLEVEL%
