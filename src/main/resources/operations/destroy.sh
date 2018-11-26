#!/bin/sh

readonly APP_NAME=preesence
readonly POSTGRESQL_JDBC_DRIVER=postgresql-42.2.5.jar

rm -r $HOME/"${APP_NAME}"/
./asadmin delete-jdbc-resource jdbc/postgres_"${APP_NAME}"
./asadmin delete-jdbc-connection-pool postgres_"${APP_NAME}"_pool
rm ../glassfish/domains/domain1/lib/ext/"${POSTGRESQL_JDBC_DRIVER}"