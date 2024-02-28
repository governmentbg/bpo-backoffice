#!/bin/bash
catchError() {
	tput setaf 1; echo "-------------------- ERROR --------------------"; tput sgr0
  	exit 1
}

tput setaf 3; echo "-------------------- START IPAS REST DOMAIN GENERATOR --------------------"; tput sgr0

mvn clean install -DskipTests || catchError
mvn exec:java -Dexec.mainClass="bg.duosoft.ipas.ExecuteGenerator" || catchError


tput setaf 2; echo "-------------------- GENERATION OF JAVA FILES END SUCCESSFULL --------------------"; tput sgr0
