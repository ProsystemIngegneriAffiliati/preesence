/*
 * Copyright (C) 2018-2020 Prosystem Ingegneri Affiliati
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
import static com.prosystemingegneri.preesence.presentation.LazyUtils.getAscending;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Davide Mainardi <ingmainardi at live.com>
 */
public class PresenceLazyDataModel extends LazyDataModel<Presence>{
    private final PresenceService service;
    
    private Worker worker;
    private LocalDate start;
    private LocalDate end;

    public PresenceLazyDataModel(PresenceService service) {
        this.service = service;
    }
    
    @Override
    public Object getRowKey(Presence object) {
        return object.getId();
    }
    
    @Override
    public List<Presence> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, FilterMeta> filterBy) {
        List<Presence> result = service.list(first, pageSize, sortField, getAscending(sortOrder), start, end, worker);
        this.setRowCount(service.getCount(start, end, worker).intValue());
        
        return result;
    }

    @Override
    public Presence getRowData(String rowKey) {
        try {
            return service.find(Long.parseLong(rowKey));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }
    
}
