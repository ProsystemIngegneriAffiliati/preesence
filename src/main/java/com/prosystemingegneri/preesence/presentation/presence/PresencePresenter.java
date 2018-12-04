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
package com.prosystemingegneri.preesence.presentation.presence;

import com.prosystemingegneri.preesence.business.presence.boundary.PresenceService;
import com.prosystemingegneri.preesence.business.presence.entity.Presence;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;


/**
 *
 * @author Davide Mainardi <ingmainardi@live.com>
 */
@Named
@ViewScoped
public class PresencePresenter implements Serializable{
    @Inject
    private PresenceService service;
    
    @Inject
    private FacesContext facesContext;
    
    private Presence presence;
    private Long id;
    
    public String save() {
        presence = service.save(presence);
        Messages.create("success").detail("saved").flash().add();
        if (id == 0L)
            id = presence.getId();
        
        return facesContext.getViewRoot().getViewId() + "?faces-redirect=true&includeViewParams=true";
    }
    
    public void detail() {
        if (id != null) {
            if (id == 0)
                presence = service.create();
            else
                presence = service.find(id);
        }
    }

    public Presence getPresence() {
        return presence;
    }

    public void setPresence(Presence presence) {
        this.presence = presence;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
}