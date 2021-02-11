all: build start-service
.PHONY: build

build:
	@echo "##### Build inventory api jar file #####"
	mvn clean package
	@echo "##### DONE #####"

start-service:
	@echo "##### Start inventory api service #####"
	@echo "########################### IMPORTANT ############################################################################################"
	@echo "##### Before you start service you must properly configure connection to database in ./tools/congif_local.json!:             #####"
	@echo "##### Check password to inventory api on your lab: k  get secret dev-dcae-inventory-api-pg-user-creds -o yaml, decode it 	#####"
	@echo "##### from base64. 																											#####"
	@echo "##### Change Worker_IP adress in url, expose port from svc dcae-inv-pg-primary, and also change it in url                    #####"
	@echo "##### Now service should up and running pls check: http://localhost:9080/swagger.json or http://localhost:9080/servicehealth #####"
	@echo "##################################################################################################################################"

	java -DdevMode=true --add-opens java.base/java.lang=ALL-UNNAMED -jar ./target/inventory-api-3.5.1-SNAPSHOT.jar server /home/edyta/Repository/Onap/inventory-api/tools/config_local.json

	@echo "##### DONE #####"
