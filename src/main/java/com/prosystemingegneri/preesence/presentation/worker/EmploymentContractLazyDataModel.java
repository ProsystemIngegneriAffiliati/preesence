/*
 * Copyright (C) 2019-2020 Prosystem Ingegneri Affiliati
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
import static com.prosystemingegneri.preesence.presentation.LazyUtils.getAscending;
import static com.prosystemingegneri.preesence.presentation.LazyUtils.getStringFromFilter;
import java.util.List;
import java.util.Map;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Mainardi Davide <davide at mainardisoluzioni.com>
 */
public class EmploymentContractLazyDataModel extends LazyDataModel<EmploymentContract>{
    private final EmploymentContractService service;

    public EmploymentContractLazyDataModel(EmploymentContractService service) {
        this.service = service;
    }
    
    @Override
    public Object getRowKey(EmploymentContract object) {
        return object.getId();
    }
    
    @Override
    public List<EmploymentContract> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, FilterMeta> filterBy) {
        String name = getStringFromFilter(filterBy, "name");
        
        List<EmploymentContract> result = service.list(first, pageSize, sortField, getAscending(sortOrder), name);
        this.setRowCount(service.getCount(name).intValue());
        
        return result;
    }

    @Override
    public EmploymentContract getRowData(String rowKey) {
        try {
            return service.find(Long.parseLong(rowKey));
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
}
