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
import com.prosystemingegneri.preesence.presentation.ExceptionUtility;
import java.io.Serializable;
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
public class UserPresenter implements Serializable {
    @Inject
    UserService userService;
    
    private UserApp user;
    private String id;
    
    private boolean isCreatingWorker;
    
    @PostConstruct
    private void init() {
        isCreatingWorker = true;
    }
    
    
    public void readUserApp() {
        if (id != null && !id.isEmpty())
            user = userService.readUserApp(id);
    }
    
    public String saveUserApp() {
        try {
            user = userService.saveUserApp(user, isCreatingWorker);
        } catch (EJBException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", ExceptionUtility.unwrap(e.getCausedByException()).getLocalizedMessage()));
            return null;
        }
        
        return "/secured/manageUser/users?faces-redirect=true";
    }

    public UserApp getUserApp() {
        return user;
    }

    public void setUserApp(UserApp userApp) {
        this.user = userApp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isIsCreatingWorker() {
        return isCreatingWorker;
    }

    public void setIsCreatingWorker(boolean isCreatingWorker) {
        this.isCreatingWorker = isCreatingWorker;
    }
    
}
