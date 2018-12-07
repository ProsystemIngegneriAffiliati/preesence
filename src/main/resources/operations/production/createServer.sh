#!/usr/bin/env bash

source config.shlib; # load the config library functions

#Apache proxy pass and reverse proxy configuration
ssh root@"$(config_get IP_ADDRESS)" -p "$(config_get EXTERNAL_SSH_PORT)" '\
a2enmod proxy_http; \
service apache2 stop; \
service apache2 start; \
service apache2 restart; \
cp /etc/apache2/sites-available/000-default.conf /etc/apache2/sites-available/payaraSite.conf; \
sed -i /etc/apache2/sites-available/payaraSite.conf -e "s/<VirtualHost \*:80>/<VirtualHost *:80>\n\tProxyPass \/ http:\/\/localhost:8080\/\n\tProxyPassReverse \/ http:\/\/localhost:8080\/\n\tProxyPreserveHost on/"; \
a2dissite 000-default; \
a2ensite payaraSite; \
service apache2 restart'

#Database creation
scp -P "$(config_get EXTERNAL_SSH_PORT)" "$(config_get DB_INITIAL_FILENAME_COMPRESSED)" root@"$(config_get IP_ADDRESS)":/root/
ssh root@"$(config_get IP_ADDRESS)" -p "$(config_get EXTERNAL_SSH_PORT)" '\
useradd -M -g postgres -G users '"$(config_get DB_USER_NAME)"'; \
passwd '"$(config_get DB_USER_NAME)"'; \
createuser --createdb --pwprompt --echo '"$(config_get DB_USER_NAME)"'; \
createdb --owner='"$(config_get DB_USER_NAME)"' --encoding=UTF8 '"$(config_get DB_NAME)"'; \
gzip --uncompress '"$(config_get DB_INITIAL_FILENAME_COMPRESSED)"'; \
pg_restore -d '"$(config_get DB_NAME)"' '"$(config_get DB_INITIAL_FILENAME)"'; \
rm '"$(config_get DB_INITIAL_FILENAME)"''

#Useful for database backup
#scp -P "$(config_get EXTERNAL_SSH_PORT)" backupDb.sh root@"$(config_get IP_ADDRESS)":/root/
#ssh root@"$(config_get IP_ADDRESS)" -p "$(config_get EXTERNAL_SSH_PORT)" '\
#apt-get -qy update; \
#apt-get -qy install libsasl2-modules mutt; \
#chmod +x backupDb.sh; \
#(crontab -l && echo "15 1 * * 0 /root/backupDb.sh >/dev/null 2>&1") | crontab -'

#Application server (Payara) installation and configuration
scp -P "$(config_get EXTERNAL_SSH_PORT)" *Password root@"$(config_get IP_ADDRESS)":/root/
ssh root@"$(config_get IP_ADDRESS)" -p "$(config_get EXTERNAL_SSH_PORT)" '\
apt-get install dirmngr; \
echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu xenial main" | tee /etc/apt/sources.list.d/webupd8team-java.list; \
echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu xenial main" | tee -a /etc/apt/sources.list.d/webupd8team-java.list; \
apt-key adv --no-tty --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys EEA14886; \
apt-get update; \
echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections; \
apt-get -y install oracle-java8-installer; \
\
cd '"$(config_get AS_INSTALL_DIR)"'; \
wget https://repo1.maven.org/maven2/fish/payara/distributions/payara/'"$(config_get AS_VERSION)"'/payara-'"$(config_get AS_VERSION)"'.zip; \
unzip payara-'"$(config_get AS_VERSION)"'.zip; \
rm payara-'"$(config_get AS_VERSION)"'.zip; \
\
wget -P '"$(config_get AS_ROOT_DIR)"'/glassfish/domains/'"$(config_get AS_DOMAIN_NAME)"'/lib/ext/ https://jdbc.postgresql.org/download/'"$(config_get POSTGRESQL_JDBC_DRIVER)"'; \
\
sh '"$(config_get AS_BIN)"'/asadmin start-domain '"$(config_get AS_DOMAIN_NAME)"'; \
\
sh '"$(config_get AS_BIN)"'/asadmin --passwordfile ${HOME}/dbUserPassword create-password-alias '"$(config_get AS_PASSWORD_ALIAS_NAME)"'; \
rm ${HOME}/dbUserPassword; \
sh '"$(config_get AS_BIN)"'/asadmin --passwordfile ${HOME}/mailPassword create-password-alias '"$(config_get AS_MAILPASSWORD_ALIAS_NAME)"'; \
rm ${HOME}/mailPassword; \
\
sh '"$(config_get AS_BIN)"'/asadmin create-jdbc-connection-pool \
--datasourceclassname=org.postgresql.ds.PGSimpleDataSource \
--restype=javax.sql.DataSource \
--validationmethod=auto-commit \
--allownoncomponentcallers=false \
--nontransactionalconnections=false \
--driverclassname=org.postgresql.Driver \
--property user='"$(config_get DB_USER_NAME)"':\
password=\$\{ALIAS='"$(config_get AS_PASSWORD_ALIAS_NAME)"'\}:\
databaseName='"$(config_get DB_NAME)"':\
serverName=localhost:\
portNumber=5432:\
url=jdbc\\:postgresql\\://localhost\\:5432/'"$(config_get DB_NAME)"' \
postgres_'"$(config_get APP_NAME)"'_pool; \
\
sh '"$(config_get AS_BIN)"'/asadmin create-jdbc-resource --connectionpoolid postgres_'"$(config_get APP_NAME)"'_pool jdbc/postgres_'"$(config_get APP_NAME)"'; \
\
sh '"$(config_get AS_BIN)"'/asadmin create-javamail-resource \
--mailhost smtp.office365.com \
--mailuser EnviApp@acs-polito.it \
--fromaddress EnviApp@acs-polito.it \
--password \$\{ALIAS='"$(config_get AS_MAILPASSWORD_ALIAS_NAME)"'\} \
--auth true \
--property mail.smtp.ssl.enable=true:\
mail.smtp.ssl.trust=smtp.office365.com:\
mail.smtp.host=smtp.office365.com \
mail/'"$(config_get APP_NAME)"'Mail; \
\
sh '"$(config_get AS_BIN)"'/asadmin restart-domain '"$(config_get AS_DOMAIN_NAME)"''
