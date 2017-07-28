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
import com.prosystemingegneri.preesence.business.worker.entity.Worker;
import com.prosystemingegneri.preesence.presentation.ExceptionUtility;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
public class PresenceListPresenter implements Serializable{
    @Inject
    PresenceService service;
    
    private List<Presence> presences;
    
    private Date start;
    private Date end;
    private Worker worker;
    private Boolean isPresenceNotEnded;
    
    @PostConstruct
    public void init() {        
        Calendar startCalendar = new GregorianCalendar();
        startCalendar.set(Calendar.DAY_OF_MONTH, 1);
        startCalendar.set(Calendar.MONTH, 0);
        start = startCalendar.getTime();
        
        Calendar endCalendar = new GregorianCalendar();
        endCalendar.add(Calendar.DAY_OF_YEAR, 1);
        end = endCalendar.getTime();
        
        updatePresences();
    }
    
    public void updatePresences() {
        presences = service.listPresences(start, end, worker, isPresenceNotEnded);
    }
    
    
    public void deletePresence(Long id) {
        if (id != null) {
            try {
                service.deletePresence(id);
            } catch (EJBException e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", ExceptionUtility.unwrap(e.getCausedByException()).getLocalizedMessage()));
            }
            updatePresences();
        }
        else
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Missing selection", "Select a row before deleting"));
    }

    public List<Presence> getPresences() {
        return presences;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public Boolean getIsPresenceNotEnded() {
        return isPresenceNotEnded;
    }

    public void setIsPresenceNotEnded(Boolean isPresenceNotEnded) {
        this.isPresenceNotEnded = isPresenceNotEnded;
    }
    
}