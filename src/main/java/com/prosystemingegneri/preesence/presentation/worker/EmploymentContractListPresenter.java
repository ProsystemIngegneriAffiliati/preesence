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
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
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
public class EmploymentContractListPresenter implements Serializable{
    @Inject
    private EmploymentContractService service;
    
    private EmploymentContractLazyDataModel lazyEmploymentContracts;
    private List<EmploymentContract> selectedEmploymentContracts;
    private List<EmploymentContract> employmentContracts;
    
    @PostConstruct
    public void init() {
        lazyEmploymentContracts = new EmploymentContractLazyDataModel(service);
        employmentContracts = new ArrayList<>();
    }
    
    public void delete() {
        if (selectedEmploymentContracts != null && !selectedEmploymentContracts.isEmpty())
            for (EmploymentContract employmentContract : selectedEmploymentContracts)
                service.delete(employmentContract.getId());
        else
            Messages.create("missingSelection").warn().detail("missingSelection.tip").add();
    }
    
    public List<EmploymentContract> complete(String name) {
        employmentContracts = service.list(0, 10, null, null, name);
        return employmentContracts;
    }
    
    /**
     * Useful only for 'omnifaces.ListConverter' used in 'p:autoComplete'
     * 
     * @param defaultEmploymentContract Needed when jsf page read not null autocomplete (when, for example, open an already saved entity)
     * @return 
     */
    public List<EmploymentContract> getEmploymentContracts(EmploymentContract defaultEmploymentContract) {
        if (employmentContracts.isEmpty())
            employmentContracts.add(defaultEmploymentContract);
        return employmentContracts;
    }

    public EmploymentContractLazyDataModel getLazyEmploymentContracts() {
        return lazyEmploymentContracts;
    }

    public void setLazyEmploymentContracts(EmploymentContractLazyDataModel lazyEmploymentContracts) {
        this.lazyEmploymentContracts = lazyEmploymentContracts;
    }

    public List<EmploymentContract> getSelectedEmploymentContracts() {
        return selectedEmploymentContracts;
    }

    public void setSelectedEmploymentContracts(List<EmploymentContract> selectedEmploymentContract) {
        this.selectedEmploymentContracts = selectedEmploymentContract;
    }
    
}