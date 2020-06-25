/*
 * Copyright (C) 2018-2020 Prosystem Ingegneri Affiliati
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
package com.prosystemingegneri.preesence.presentation.auth;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import static javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters.withParams;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.Password;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import org.omnifaces.util.Messages;

/**
 *
 * @author Davide Mainardi <ingmainardi at live.com>
 */
@Named
@RequestScoped
public class LoginPresenter {

    @Inject
    private ExternalContext externalContext;
    @Inject
    private FacesContext facesContext;
    @Inject
    private SecurityContext securityContext;

    private @NotBlank String password;
    private @NotBlank String username;

    public void login() {
        Credential credential = new UsernamePasswordCredential(username, new Password(password));
        AuthenticationStatus status = securityContext.authenticate(
                (HttpServletRequest) externalContext.getRequest(),
                (HttpServletResponse) externalContext.getResponse(),
                withParams().credential(credential));
        switch (status) {
            case NOT_DONE:
                break;
            case SEND_CONTINUE:
                facesContext.responseComplete();
                break;
            case SEND_FAILURE:
                Messages.addGlobalError("login.failed");
                break;
            case SUCCESS:
                //facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Authentication successfull", null));
                break;
            default:
                throw new AssertionError();
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
}
