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
package com.prosystemingegneri.preesence.presentation.holiday;

import com.prosystemingegneri.preesence.business.holiday.boundary.HolidayService;
import com.prosystemingegneri.preesence.business.holiday.entity.BankHoliday;
import static com.prosystemingegneri.preesence.presentation.LazyUtils.getAscending;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Mainardi Davide <davide at mainardisoluzioni.com>
 */
public class HolidayLazyDataModel extends LazyDataModel<BankHoliday>{
    private final HolidayService service;

    public HolidayLazyDataModel(HolidayService service) {
        this.service = service;
    }
    
    @Override
    public Object getRowKey(BankHoliday object) {
        return object.getDaytime();
    }
    
    @Override
    public List<BankHoliday> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, FilterMeta> filterBy) {
        List<BankHoliday> result = service.list(first, pageSize, sortField, getAscending(sortOrder));
        this.setRowCount(service.getCount().intValue());
        
        return result;
    }

    @Override
    public BankHoliday getRowData(String rowKey) {
        try {
            return service.find(LocalDate.parse(rowKey));
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
}
