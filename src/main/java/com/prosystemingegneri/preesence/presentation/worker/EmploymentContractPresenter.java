/*
 * Copyright (C) 2019 Prosystem Ingegneri Affiliati
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

import com.prosystemingegneri.preesence.business.worker.boundary.EmploymentContractService;
import com.prosystemingegneri.preesence.business.worker.entity.EmploymentContract;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

/**
 *
 * @author Mainardi Davide <davide at mainardisoluzioni.com>
 */
@Named
@ViewScoped
public class EmploymentContractPresenter implements Serializable{
    @Inject
    private EmploymentContractService service;
    
    @Inject
    private FacesContext facesContext;
    
    private EmploymentContract employmentContract;
    private Long id;
    
    public String save() {
        employmentContract = service.save(employmentContract);
        Messages.create("success").detail("saved").flash().add();
        if (id == 0L)
            id = employmentContract.getId();
        
        return facesContext.getViewRoot().getViewId() + "?faces-redirect=true&includeViewParams=true";
    }
    
    public void detail() {
        if (id != null) {
            if (id == 0)
                employmentContract = service.create();
            else
                employmentContract = service.find(id);
        }
    }

    public EmploymentContract getEmploymentContract() {
        return employmentContract;
    }

    public void setEmploymentContract(EmploymentContract employmentContract) {
        this.employmentContract = employmentContract;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
}