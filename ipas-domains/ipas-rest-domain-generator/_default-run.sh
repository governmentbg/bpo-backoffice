#!/bin/bash
catchError() {
	tput setaf 1; echo "-------------------- ERROR --------------------"; tput sgr0
  	exit 1
}

tput setaf 3; echo "-------------------- START IPAS REST DOMAIN GENERATOR --------------------"; tput sgr0

mvn clean install -DskipTests || catchError
mvn exec:java -Dexec.mainClass="bg.duosoft.ipas.ExecuteGenerator" -Dexec.args="-classes CPatent,CMark,CUserdoc,CAttachment,CProcess,CIpcClass,CommonTerm,CReception,CReceptionResponse,CProcessInsertActionRequest,COffidocAbdocsDocument ,CUserdocHierarchyNode,CSearchParam,SearchActionParam,SearchPage,Pageable,Sortable,CSearchResult -target ../ipas-domain-rest/src/main/java/bg/duosoft/ipas/rest/model" || catchError


tput setaf 2; echo "-------------------- GENERATION OF JAVA FILES END SUCCESSFULL --------------------"; tput sgr0
