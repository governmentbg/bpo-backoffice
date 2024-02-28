@echo off
echo.
echo [93m -------------------- START IPAS REST DOMAIN GENERATOR -------------------- [0m
echo.

call mvn -f  "%cd%\pom.xml" clean install -DskipTests -q || goto :error
call mvn -f  "%cd%\pom.xml" exec:java -Dexec.mainClass="bg.duosoft.ipas.ExecuteGenerator" -q || goto :error

echo.
echo [92m -------------------- GENERATION OF JAVA FILES END SUCCESSFULL ! -------------------- [0m
echo.

:error
if %errorlevel% neq 0 (
	echo.
	echo [91m -------------------- ERROR -------------------- [0m
	echo.
)
exit /b %errorlevel%

