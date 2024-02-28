@echo off
echo.
echo [93m -------------------- START IPAS REST DOMAIN GENERATOR -------------------- [0m
echo.

call mvn -f  "%cd%\pom.xml" clean install -DskipTests -q || goto :error
call mvn -f  "%cd%\pom.xml" exec:java -Dexec.mainClass="bg.duosoft.ipas.ExecuteGenerator" -Dexec.args="-classes CPatent,CMark,CUserdoc,CAttachment,CProcess,CIpcClass,CommonTerm,CReception,CReceptionResponse,CProcessInsertActionRequest,COffidocAbdocsDocument,CUserdocHierarchyNode,CSearchParam,SearchActionParam,SearchPage,Pageable,Sortable,CSearchResult -target ../ipas-domain-rest/src/main/java/bg/duosoft/ipas/rest/model" -q || goto :error

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

