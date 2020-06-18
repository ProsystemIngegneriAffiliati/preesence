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
import java.io.Serializable;
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
public class HolidayListPresenter implements Serializable{
    @Inject
    private HolidayService service;
    
    private HolidayLazyDataModel lazyBankHolidays;
    private List<BankHoliday> selectedBankHolidays;
    
    @PostConstruct
    public void init() {
        lazyBankHolidays = new HolidayLazyDataModel(service);
    }
    
    public void delete() {
        if (selectedBankHolidays != null && !selectedBankHolidays.isEmpty())
            for (BankHoliday bankHoliday : selectedBankHolidays)
                service.delete(bankHoliday.getDaytime());
        else
            Messages.create("missingSelection").warn().detail("missingSelection.tip").add();
    }

    public HolidayLazyDataModel getLazyBankHolidays() {
        return lazyBankHolidays;
    }

    public void setLazyBankHolidays(HolidayLazyDataModel lazyBankHolidays) {
        this.lazyBankHolidays = lazyBankHolidays;
    }

    public List<BankHoliday> getSelectedBankHolidays() {
        return selectedBankHolidays;
    }

    public void setSelectedBankHolidays(List<BankHoliday> selectedBankHolidays) {
        this.selectedBankHolidays = selectedBankHolidays;
    }
    
}