#!/bin/sh

./asadmin delete-auth-realm preesenceRealm
./asadmin delete-jdbc-resource jdbc/postgres_preesence
./asadmin delete-jdbc-connection-pool postgres_preesence_pool
rm ../glassfish/domains/domain1/lib/ext/postgresql-42.0.0.jar
