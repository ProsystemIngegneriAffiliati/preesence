#!/bin/sh
readonly IDE_WORKSPACE=$HOME/NetBeansProjects
readonly AS_LIBFOLDER=$HOME/payara5/glassfish/domains/domain1/lib
readonly APP_NAME=preesence
readonly IP_ADDRESS=192.168.1.10
readonly POSTGRESQL_JDBC_DRIVER_VERSION=42.2.8
readonly DB_NAME="${APP_NAME}"
readonly DB_USER_NAME="${APP_NAME}"
readonly DB_USER_PASSWORD=aEsSV7ToimzThX6BbP6n

mkdir $HOME/"${APP_NAME}"
mkdir $HOME/"${APP_NAME}"/documents
\
mvn -f "${IDE_WORKSPACE}"/"${APP_NAME}"/pom.xml -DincludeScope=provided -DexcludeArtifactIds=javax.mail,javaee-api,activation,microprofile -DexcludeTransitive=true -DoutputDirectory=$HOME/"${APP_NAME}"/ dependency:copy-dependencies
mvn dependency:copy -Dartifact=org.postgresql:postgresql:"${POSTGRESQL_JDBC_DRIVER_VERSION}" -DoutputDirectory=$HOME/"${APP_NAME}"/
\
./asadmin start-domain
./asadmin add-library $HOME/"${APP_NAME}"/*.jar
./asadmin create-jdbc-connection-pool \
--datasourceclassname=org.postgresql.ds.PGSimpleDataSource \
--restype=javax.sql.DataSource \
--validationmethod=auto-commit \
--allownoncomponentcallers=false \
--nontransactionalconnections=false \
--driverclassname=org.postgresql.Driver \
--property user="${DB_USER_NAME}":\
password="${DB_USER_PASSWORD}":\
databaseName="${DB_NAME}":\
serverName="${IP_ADDRESS}":\
portNumber=5432:\
url=jdbc\\:postgresql\\://"${IP_ADDRESS}"\\:5432/"${DB_NAME}" \
postgres_"${APP_NAME}"_pool
./asadmin create-jdbc-resource --connectionpoolid postgres_"${APP_NAME}"_pool jdbc/postgres_"${APP_NAME}"
./asadmin stop-domain
