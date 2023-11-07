#clean and package all services.
#-pl select a specified project, -am If project list is specified, also build projects required by the list

sh mvnw clean
sh mvnw package -pl api-gateway -am
sh mvnw package -pl discovery-server -am
sh mvnw package -pl inventory-service -am
sh mvnw package -pl notification-service -am
sh mvnw package -pl order-service -am
sh mvnw package -pl product-service -am