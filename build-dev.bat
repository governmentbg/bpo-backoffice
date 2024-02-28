@echo off
echo.
echo [93m -------------------- START BUILDING IPAS PROJECT -------------------- [0m
echo.

call mvn -f  "%cd%\ipas-domains\pom.xml" clean install -DskipTests || goto :error
call mvn -f  "%cd%\ipas-rest-client\pom.xml" clean install -DskipTests || goto :error
call mvn -f  "%cd%\ipas-persistence\pom.xml" clean install -DskipTests || goto :error
call mvn -f  "%cd%\ipas-ui\pom.xml" clean install -s "%cd%\ipas-ui\settings.xml" -P dev -DskipTests || goto :error

echo.
echo [92m -------------------- SUCCESSFUL BUILD -------------------- [0m
echo.

:error
if %errorlevel% neq 0 (
	echo.
	echo [91m -------------------- UNSUCCESSFUL BUILD -------------------- [0m
	echo.
)
exit /b %errorlevel%

