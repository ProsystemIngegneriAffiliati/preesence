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
package com.prosystemingegneri.preesence.presentation;

import java.util.Map;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortOrder;
import static org.primefaces.model.SortOrder.ASCENDING;
import static org.primefaces.model.SortOrder.DESCENDING;

/**
 *
 * @author Mainardi Davide <davide@mainardisoluzioni.com>
 */
public class LazyUtils {
    public static String getStringFromFilter(Map<String, FilterMeta> filterBy, String key) {
        String result = null;
        if (filterBy.get(key) != null && filterBy.get(key).getFilterValue() != null)
            result = filterBy.get(key).getFilterValue().toString();
        
        return result;
    }
    
    public static Boolean getBooleanFromFilter(Map<String, FilterMeta> filterBy, String key) {
        Boolean result = null;
        if (filterBy.get(key) != null && filterBy.get(key).getFilterValue() != null)
            result = (Boolean) filterBy.get(key).getFilterValue();
        
        return result;
    }
    
    public static Integer getIntegerFromFilter(Map<String, FilterMeta> filterBy, String key) {
        Integer result = null;
        if (filterBy.get(key) != null && filterBy.get(key).getFilterValue() != null)
            result = (Integer) filterBy.get(key).getFilterValue();
        
        return result;
    }
    
    public static Boolean getAscending(SortOrder sortOrder) {
        switch (sortOrder) {
            case ASCENDING:
                return Boolean.TRUE;
            case DESCENDING:
                return Boolean.FALSE;
            default:
        }
        
        return null;
    }
}
