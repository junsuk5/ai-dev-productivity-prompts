@echo off
setlocal EnableExtensions EnableDelayedExpansion
chcp 65001 >nul

for %%I in ("%~dp0..") do set "PROJECT_DIR=%%~fI"
set "OUTPUT_DIR=%PROJECT_DIR%\out\run"

if exist "%OUTPUT_DIR%" rmdir /s /q "%OUTPUT_DIR%"
mkdir "%OUTPUT_DIR%" || exit /b 1

set "MAIN_FILES="
for /r "%PROJECT_DIR%\src\main\java" %%F in (*.java) do (
    set "MAIN_FILES=!MAIN_FILES! "%%F""
)
if not defined MAIN_FILES (
    echo 컴파일할 main Java 소스를 찾을 수 없습니다.
    exit /b 1
)

javac --release 17 -encoding UTF-8 -Xlint:all -Werror ^
    -d "%OUTPUT_DIR%" ^
    !MAIN_FILES!
if errorlevel 1 exit /b %ERRORLEVEL%

if exist "%PROJECT_DIR%\src\main\resources" (
    xcopy "%PROJECT_DIR%\src\main\resources\*" "%OUTPUT_DIR%\" /E /I /Y >nul
    if errorlevel 1 exit /b !ERRORLEVEL!
)

java -cp "%OUTPUT_DIR%" kr.or.publicdata.portal.HospitalPortalApplication
exit /b %ERRORLEVEL%
