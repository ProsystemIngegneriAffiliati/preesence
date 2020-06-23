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
import javax.enterprise.context.RequestScoped;
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
public class SignUpPresenter {
    @Inject
    private UserAppService userAppService;
    
    private @NotEmpty String username;
    private @NotEmpty String password;
    
    public void createUser() {
        if (!userAppService.isUsernamePresent(username)) {
            if (userAppService.create(username, new Password(password)) != null)
                Messages.create("success").detail("signUp.createUser.done").flash().add();
            else
                Messages.create("error").error().detail("signUp.error.userNotCreated").flash().add();
            Faces.redirect("index");
        }
        else
            Messages.create("warning").error().detail("signUp.error.usernamePresent").add();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
