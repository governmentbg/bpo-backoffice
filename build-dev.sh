#!/bin/bash
catchError() {
	tput setaf 1; echo "-------------------- UNSUCCESSFUL BUILD --------------------"; tput sgr0
  	exit 1
}

tput setaf 3; echo "-------------------- START BUILDING IPAS PROJECT --------------------"; tput sgr0

mvn -f ipas-domains/pom.xml clean install -DskipTests || catchError
mvn -f ipas-rest-client/pom.xml clean install -DskipTests || catchError
mvn -f ipas-persistence/pom.xml clean install -DskipTests || catchError
mvn -f ipas-ui/pom.xml clean install -s ipas-ui/settings.xml -Pdev -DskipTests || catchError

tput setaf 2; echo "-------------------- SUCCESSFUL BUILD --------------------"; tput sgr0
