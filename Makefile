all: build start-service
.PHONY: build

build:
	@echo "##### Build inventory api jar file #####"
	mvn clean package
	@echo "##### DONE #####"

start-service:
	@echo "##### Start inventory api service #####"
	@echo "########################### IMPORTANT ############################################################################################"
	@echo "##### Before you start service you must properly configure connection to database in ./tools/congif_local.json!              #####"
	@echo "##### Now service should up and running pls check: http://localhost:9080/swagger.json or http://localhost:9080/servicehealth #####"
	@echo "##################################################################################################################################"

	java -DdevMode=true --add-opens java.base/java.lang=ALL-UNNAMED -jar ./target/inventory-api-3.5.0-SNAPSHOT.jar server ./tools/config_local.json

	@echo "##### DONE #####"
