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
package com.prosystemingegneri.preesence.presentation.user;

import com.prosystemingegneri.preesence.business.auth.boundary.UserAppService;
import static com.prosystemingegneri.preesence.business.auth.control.ChangingPasswordResult.SUCCESS;
import static com.prosystemingegneri.preesence.business.auth.control.ChangingPasswordResult.USER_NOT_FOUND;
import static com.prosystemingegneri.preesence.business.auth.control.ChangingPasswordResult.WRONG_PASSWORD;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.credential.Password;
import javax.validation.constraints.NotEmpty;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

/**
 *
 * @author Davide Mainardi <ingmainardi at live.com>
 */
@Named
@RequestScoped
public class ChangePasswordPresenter {
    @Inject
    private UserAppService service;
    
    @Inject
    private ExternalContext externalContext;
    
    private String username;
    
    private @NotEmpty String currentPassword;
    private @NotEmpty String newPassword;
    
    @PostConstruct
    public void init() {
        if (externalContext.getUserPrincipal() != null) {
            if (service.isUsernamePresent(externalContext.getUserPrincipal().getName()))
                username = externalContext.getUserPrincipal().getName();
            else
                Messages.create("error").error().detail("user.changePassword.error.noUserInDatabase").add();
        }
        else
            Messages.create("error").error().detail("user.changePassword.error.noLoggedUser").add();
    }
    
    public void submit() {
        switch (service.changePassword(username, new Password(currentPassword), new Password(newPassword))) {
            case SUCCESS:
                Messages.create("success").detail("user.changePassword.done").flash().add();
                Faces.redirect("index");
                break;
            case USER_NOT_FOUND:
                Messages.create("error").error().detail("user.changePassword.error.noUserInDatabase").add();
                break;
            case WRONG_PASSWORD:
                Messages.create("warning").error().detail("user.changePassword.error.wrongPassword").add("signUpForm:currentPassword");
                break;
            default:
                throw new AssertionError();
        }
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getUsername() {
        return username;
    }
    
}
