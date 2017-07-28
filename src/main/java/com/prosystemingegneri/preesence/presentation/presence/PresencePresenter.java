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
import com.prosystemingegneri.preesence.business.user.entity.UserApp;
import com.prosystemingegneri.preesence.presentation.ExceptionUtility;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import org.omnifaces.cdi.ViewScoped;

/**
 *
 * @author Davide Mainardi <ingmainardi@live.com>
 */
@Named
@ViewScoped
public class PresencePresenter implements Serializable{
    @Inject
    PresenceService service;
    
    private Presence presence;
    private Long id;
    
    @Resource
    Validator validator;
    
    public String savePresence() {
        boolean isValidated = true;
        for (ConstraintViolation<Presence> constraintViolation : validator.validate(presence, Default.class)) {
            isValidated = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", constraintViolation.getMessage()));
        }
        if (!isValidated)
            return null;
        
        try {
            service.savePresence(presence);
        } catch (EJBException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", ExceptionUtility.unwrap(e.getCausedByException()).getLocalizedMessage()));
            return null;
        }
        
        return "/secured/presence/presences?faces-redirect=true";
    }
    
    public void detailPresence() {
        if (id != null) {
            if (id == 0)
                presence = new Presence();
            else
                presence = service.readPresence(id);
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