@echo off
powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%~dp0..\..\scripts\run-gradle.ps1" -ProjectDirectory "%~dp0." -JavaVersion 21 %*
exit /b %ERRORLEVEL%
