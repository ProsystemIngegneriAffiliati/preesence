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
package com.prosystemingegneri.preesence.presentation.user;

import com.prosystemingegneri.preesence.business.auth.boundary.UserAppService;
import com.prosystemingegneri.preesence.business.auth.entity.UserApp;
import static com.prosystemingegneri.preesence.presentation.LazyUtils.getAscending;
import static com.prosystemingegneri.preesence.presentation.LazyUtils.getStringFromFilter;
import java.util.List;
import java.util.Map;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Davide Mainardi <ingmainardi at live.com>
 */
public class UserLazyDataModel extends LazyDataModel<UserApp>{
    private final UserAppService service;

    public UserLazyDataModel(UserAppService service) {
        this.service = service;
    }
    
    @Override
    public Object getRowKey(UserApp object) {
        return object.getUsername();
    }
    
    @Override
    public List<UserApp> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, FilterMeta> filterBy) {
        String username = getStringFromFilter(filterBy, "username");
        String groupAppName = getStringFromFilter(filterBy, "groupAppName");
        
        List<UserApp> result = service.list(first, pageSize, sortField, getAscending(sortOrder), username, groupAppName, null);
        this.setRowCount(service.getCount(username, groupAppName, null).intValue());
        
        return result;
    }

    @Override
    public UserApp getRowData(String rowKey) {
        return service.find(rowKey);
    }
    
}
