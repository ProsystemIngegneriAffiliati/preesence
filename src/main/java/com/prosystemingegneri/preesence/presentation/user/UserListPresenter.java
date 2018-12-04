/*
 * Copyright (C) 2018 Prosystem Ingegneri Affiliati
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
import com.prosystemingegneri.preesence.business.auth.entity.UserApp;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

/**
 *
 * @author Davide Mainardi <ingmainardi at live.com>
 */
@Named
@ViewScoped
public class UserListPresenter implements Serializable {
    @Inject
    private UserAppService service;
    
    @Inject
    private ExternalContext externalContext;
    
    private UserLazyDataModel lazyUserApps;
    private List<UserApp> selectedUserApps;
    
    private List<UserApp> unassociatedUserApps;
    
    @PostConstruct
    public void init() {
        lazyUserApps = new UserLazyDataModel(service);
    }
    
    public void deleteUserApp() {
        if (selectedUserApps != null && !selectedUserApps.isEmpty()) {
            for (UserApp userApp : selectedUserApps) {
                if (!externalContext.getUserPrincipal().getName().equals(userApp.getUsername()))
                    service.delete(userApp.getUsername());
            }
        }
        else
            Messages.create("missingSelection").warn().detail("missingSelection.tip").add();
    }

    public UserLazyDataModel getLazyUserApps() {
        return lazyUserApps;
    }

    public void setLazyUserApps(UserLazyDataModel lazyUserApps) {
        this.lazyUserApps = lazyUserApps;
    }

    public List<UserApp> getSelectedUserApps() {
        return selectedUserApps;
    }

    public void setSelectedUserApps(List<UserApp> selectedUserApp) {
        this.selectedUserApps = selectedUserApp;
    }
    
    public List<UserApp> completeUnassociatedUserApps(String nome) {
        return service.listUserApps(0, 25, null, null, nome, null, Boolean.FALSE);
    }
    
    //Useful only for 'omnifaces.ListConverter' used in 'p:autoComplete'
    public List<UserApp> getUnassociatedUserApps() {
        if (unassociatedUserApps == null || unassociatedUserApps.isEmpty())
            unassociatedUserApps = service.listUserApps(0, 0, null, null, null, null, Boolean.FALSE);
        
        return unassociatedUserApps;
    }
    
}
