/*
 * Copyright (C) 2018-2019 Prosystem Ingegneri Affiliati
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.prosystemingegneri.preesence.presentation;

import java.io.Serializable;
import static java.lang.String.format;
import java.util.ResourceBundle;
import static java.util.ResourceBundle.getBundle;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.annotation.FacesConfig;
import javax.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.LoginToContinue;
import javax.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import org.omnifaces.cdi.Startup;
import static org.omnifaces.util.Faces.getLocale;
import org.omnifaces.util.Messages;
import static org.omnifaces.util.Utils.isEmpty;

/**
 *
 * @author Davide Mainardi <ingmainardi at live.com>
 */
@Startup
@CustomFormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(
                loginPage = "/login.xhtml",
                useForwardToLogin = false,
                errorPage = ""
        )
)
@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "jdbc/postgres_preesence",
        callerQuery = "select password from userapp where username = ?",
        groupsQuery = "select roles from groupapp_roles join userapp on userapp.groupapp_name = groupapp_roles.groupapp_name where userapp.username = ?",
        hashAlgorithm = Pbkdf2PasswordHash.class,
        hashAlgorithmParameters = {
            "Pbkdf2PasswordHash.Iterations=3072",
            "Pbkdf2PasswordHash.Algorithm=PBKDF2WithHmacSHA512",
            "Pbkdf2PasswordHash.SaltSizeBytes=64"
        }
)
@ApplicationScoped
@FacesConfig(version = FacesConfig.Version.JSF_2_3)
public class ApplicationConfig implements Serializable {

    @PostConstruct
    public void init() {
        configureMessageResolver();
    }

    private void configureMessageResolver() {
        Messages.setResolver(new Messages.Resolver() {
            private static final String BASE_NAME = "i18n.messages";

            @Override
            public String getMessage(String message, Object... params) {
                ResourceBundle bundle = getBundle(BASE_NAME, getLocale());
                if (bundle.containsKey(message)) {
                    message = bundle.getString(message);
                }
                return isEmpty(params) ? message : format(message, params);
            }
        });
    }
}
