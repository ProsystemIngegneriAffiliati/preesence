/*
 * Copyright (C) 2017-2020 Prosystem Ingegneri Affiliati
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

import com.prosystemingegneri.preesence.business.worker.boundary.WorkerService;
import com.prosystemingegneri.preesence.business.worker.entity.Worker;
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
 * @author Davide Mainardi <ingmainardi@live.com>
 */
@Named
@ViewScoped
public class WorkerListPresenter implements Serializable{
    @Inject
    private WorkerService service;
    
    private WorkerLazyDataModel lazyWorkers;
    private List<Worker> selectedWorkers;
    private List<Worker> workers;
    
    @PostConstruct
    public void init() {
        lazyWorkers = new WorkerLazyDataModel(service);
        workers = new ArrayList<>();
    }
    
    public void delete() {
        if (selectedWorkers != null && !selectedWorkers.isEmpty())
            for (Worker worker : selectedWorkers)
                service.delete(worker.getId());
        else
            Messages.create("missingSelection").warn().detail("missingSelection.tip").add();
    }
    
    public List<Worker> complete(String name) {
        workers = service.list(0, 10, null, null, name, Boolean.FALSE);
        return workers;
    }
    
    /**
     * Useful only for 'omnifaces.ListConverter' used in 'p:autoComplete'
     * 
     * @param defaultWorker Needed when jsf page read not null autocomplete (when, for example, open an already saved entity)
     * @return 
     */
    public List<Worker> getWorkers(Worker defaultWorker) {
        if (workers.isEmpty())
            workers.add(defaultWorker);
        return workers;
    }

    public WorkerLazyDataModel getLazyWorkers() {
        return lazyWorkers;
    }

    public void setLazyWorkers(WorkerLazyDataModel lazyWorkers) {
        this.lazyWorkers = lazyWorkers;
    }

    public List<Worker> getSelectedWorkers() {
        return selectedWorkers;
    }

    public void setSelectedWorkers(List<Worker> selectedWorker) {
        this.selectedWorkers = selectedWorker;
    }
    
}