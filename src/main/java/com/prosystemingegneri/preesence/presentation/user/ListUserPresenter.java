/*
 * Copyright (C) 2017 Prosystem Ingegneri Affiliati
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

import com.prosystemingegneri.preesence.business.user.boundary.UserService;
import com.prosystemingegneri.preesence.business.user.entity.UserApp;
import com.prosystemingegneri.preesence.presentation.Authenticator;
import com.prosystemingegneri.preesence.presentation.ExceptionUtility;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;

/**
 *
 * @author Davide Mainardi <ingmainardi@live.com>
 */
@Named
@ViewScoped
public class ListUserPresenter implements Serializable{
    private List<UserApp> users;
    
    @Inject
    UserService userService;
    
    @Inject
    Authenticator authenticator;
    
    @PostConstruct
    public void init() {
        users = userService.listUserApps();
    }
    
    public void deleteUser(UserApp userApp) {
        if (userApp.getUserName().equals(authenticator.getLoggedUser().getUserName()))
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in deleting user", "You cannot delete yourself"));
        else {
            try {
                userService.deleteUserApp(userApp);
            } catch (EJBException e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", ExceptionUtility.unwrap(e.getCausedByException()).getLocalizedMessage()));
            }
        }
        users = userService.listUserApps();
    }

    public List<UserApp> getUsers() {
        return users;
    }
}
