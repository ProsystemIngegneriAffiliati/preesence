#!/bin/sh

readonly IP_ADDRESS=192.168.0.102

./asadmin start-domain
wget -P ../glassfish/domains/domain1/lib/ext/ https://jdbc.postgresql.org/download/postgresql-42.0.0.jar
./asadmin restart-domain
./asadmin create-jdbc-connection-pool \
--datasourceclassname=org.postgresql.ds.PGSimpleDataSource \
--restype=javax.sql.DataSource \
--validationmethod=auto-commit \
--allownoncomponentcallers=false \
--nontransactionalconnections=false \
--driverclassname=org.postgresql.Driver \
--property user=preesence:\
password=tGfh2hvxd9pTwX9lAEkB:\
databaseName=preesence:\
serverName=$IP_ADDRESS:\
portNumber=5432:\
url=jdbc\\:postgresql\\://$IP_ADDRESS\\:5432/preesence \
postgres_preesence_pool
./asadmin create-jdbc-resource --connectionpoolid postgres_preesence_pool jdbc/postgres_preesence
./asadmin create-auth-realm \
--classname com.sun.enterprise.security.auth.realm.jdbc.JDBCRealm \
--property \
jaas-context=jdbcRealm:\
datasource-jndi=jdbc/postgres_preesence:\
user-table=user_app:\
user-name-column=user_name:\
password-column=pass_word:\
group-table=users_groups_app:\
group-name-column=groups_group_name:\
digest-algorithm=SHA-256:\
digestrealm-password-enc-algorithm=AES:\
encoding=base64:\
charset=UTF-8 \
preesenceRealm
./asadmin stop-domain
