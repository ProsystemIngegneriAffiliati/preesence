/*
 * Copyright (C) 2020 Prosystem Ingegneri Affiliati
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
package com.prosystemingegneri.preesence.presentation.worker;

import com.prosystemingegneri.preesence.business.worker.boundary.LunchBreakTicketService;
import com.prosystemingegneri.preesence.business.worker.entity.LunchBreakTicket;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

/**
 *
 * @author Mainardi Davide <davide@mainardisoluzioni.com>
 */
@Named
@ViewScoped
public class LunchBreakTicketPresenter implements Serializable{
    @Inject
    private LunchBreakTicketService service;
    
    @Inject
    private FacesContext facesContext;
    
    private LunchBreakTicket lunchBreakTicket;
    private Long id;
    
    public String save() {
        lunchBreakTicket = service.save(lunchBreakTicket);
        Messages.create("success").detail("saved").flash().add();
        if (id == 0L)
            id = lunchBreakTicket.getId();
        
        return facesContext.getViewRoot().getViewId() + "?faces-redirect=true&includeViewParams=true";
    }
    
    public void detail() {
        if (id != null) {
            if (id == 0)
                lunchBreakTicket = service.create();
            else
                lunchBreakTicket = service.find(id);
        }
    }

    public LunchBreakTicket getLunchBreakTicket() {
        return lunchBreakTicket;
    }

    public void setLunchBreakTicket(LunchBreakTicket lunchBreakTicket) {
        this.lunchBreakTicket = lunchBreakTicket;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
}
