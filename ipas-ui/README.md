## IPAS-UI

requireds:
	
	node.js and npm
	java 8
	

startup: 

    npm install
	npm install -g grunt-cli
    
    This commands install all necassery js and java dependacies. Also, gradlew build is compilied materialazi framewotk

build:

    mvn build or mvnw.cmd build

run:

    mvn spring-boot:run or mvnw.cmd spring-boot:run 
    after build: java -jar target\IPAS-UI-x.x.x.jar



