@echo off
setlocal EnableExtensions EnableDelayedExpansion
chcp 65001 >nul

for %%I in ("%~dp0..") do set "PROJECT_DIR=%%~fI"
set "OUTPUT_DIR=%PROJECT_DIR%\out\verify"
set "MAIN_OUTPUT=%OUTPUT_DIR%\main"
set "TEST_OUTPUT=%OUTPUT_DIR%\test"
set "JUNIT_JAR=%PROJECT_DIR%\lib\junit-4.13.2.jar"
set "HAMCREST_JAR=%PROJECT_DIR%\lib\hamcrest-core-1.3.jar"

if not exist "%JUNIT_JAR%" (
    echo JUnit 4 실행 라이브러리를 찾을 수 없습니다: %JUNIT_JAR%
    exit /b 1
)
if not exist "%HAMCREST_JAR%" (
    echo JUnit 4 실행 라이브러리를 찾을 수 없습니다: %HAMCREST_JAR%
    exit /b 1
)

if exist "%OUTPUT_DIR%" rmdir /s /q "%OUTPUT_DIR%"
mkdir "%MAIN_OUTPUT%" || exit /b 1
mkdir "%TEST_OUTPUT%" || exit /b 1

set "MAIN_FILES="
for /r "%PROJECT_DIR%\src\main\java" %%F in (*.java) do (
    set "MAIN_FILES=!MAIN_FILES! "%%F""
)
if not defined MAIN_FILES (
    echo 컴파일할 main Java 소스를 찾을 수 없습니다.
    exit /b 1
)

javac --release 17 -encoding UTF-8 -Xlint:all -Werror ^
    -d "%MAIN_OUTPUT%" ^
    !MAIN_FILES!
if errorlevel 1 exit /b %ERRORLEVEL%

if exist "%PROJECT_DIR%\src\main\resources" (
    xcopy "%PROJECT_DIR%\src\main\resources\*" "%MAIN_OUTPUT%\" /E /I /Y >nul
    if errorlevel 1 exit /b !ERRORLEVEL!
)

set "TEST_FILES="
set "TEST_CLASSES="
set "TEST_SOURCE_PREFIX=%PROJECT_DIR%\src\test\java\"
for /r "%PROJECT_DIR%\src\test\java" %%F in (*Test.java) do (
    set "TEST_FILES=!TEST_FILES! "%%F""
    set "CLASS_NAME=%%F"
    set "CLASS_NAME=!CLASS_NAME:%TEST_SOURCE_PREFIX%=!"
    set "CLASS_NAME=!CLASS_NAME:\=.!"
    set "CLASS_NAME=!CLASS_NAME:.java=!"
    set "TEST_CLASSES=!TEST_CLASSES! !CLASS_NAME!"
)
if not defined TEST_FILES (
    echo 컴파일할 JUnit 4 테스트를 찾을 수 없습니다.
    exit /b 1
)

set "TEST_CLASSPATH=%MAIN_OUTPUT%;%JUNIT_JAR%;%HAMCREST_JAR%"
javac --release 17 -encoding UTF-8 -Xlint:all -Werror ^
    -cp "%TEST_CLASSPATH%" ^
    -d "%TEST_OUTPUT%" ^
    !TEST_FILES!
if errorlevel 1 exit /b %ERRORLEVEL%

if exist "%PROJECT_DIR%\src\test\resources" (
    xcopy "%PROJECT_DIR%\src\test\resources\*" "%TEST_OUTPUT%\" /E /I /Y >nul
    if errorlevel 1 exit /b !ERRORLEVEL!
)

if not defined TEST_CLASSES (
    echo 실행할 JUnit 4 테스트를 찾을 수 없습니다.
    exit /b 1
)

set "RUNTIME_CLASSPATH=%MAIN_OUTPUT%;%TEST_OUTPUT%;%JUNIT_JAR%;%HAMCREST_JAR%"
java -cp "%RUNTIME_CLASSPATH%" org.junit.runner.JUnitCore !TEST_CLASSES!
exit /b %ERRORLEVEL%
