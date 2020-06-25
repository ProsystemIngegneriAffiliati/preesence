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
package com.prosystemingegneri.preesence.presentation.worker;

import com.prosystemingegneri.preesence.business.worker.boundary.LunchBreakTicketService;
import com.prosystemingegneri.preesence.business.worker.entity.LunchBreakTicket;
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
 * @author Mainardi Davide <davide@mainardisoluzioni.com>
 */
@Named
@ViewScoped
public class LunchBreakTicketListPresenter implements Serializable{
    @Inject
    private LunchBreakTicketService service;
    
    private LunchBreakTicketLazyDataModel lazyLunchBreakTickets;
    private List<LunchBreakTicket> selectedLunchBreakTickets;
    private List<LunchBreakTicket> lunchBreakTickets;
    
    @PostConstruct
    public void init() {
        lazyLunchBreakTickets = new LunchBreakTicketLazyDataModel(service);
        lunchBreakTickets = new ArrayList<>();
    }
    
    public void delete() {
        if (selectedLunchBreakTickets != null && !selectedLunchBreakTickets.isEmpty())
            for (LunchBreakTicket lunchBreakTicket : selectedLunchBreakTickets)
                service.delete(lunchBreakTicket.getId());
        else
            Messages.create("missingSelection").warn().detail("missingSelection.tip").add();
    }
    
    public List<LunchBreakTicket> complete(String name) {
        lunchBreakTickets = service.list(0, 10, null, null, name);
        return lunchBreakTickets;
    }
    
    /**
     * Useful only for 'omnifaces.ListConverter' used in 'p:autoComplete'
     * 
     * @param defaultLunchBreakTicket Needed when jsf page read not null autocomplete (when, for example, open an already saved entity)
     * @return 
     */
    public List<LunchBreakTicket> getLunchBreakTickets(LunchBreakTicket defaultLunchBreakTicket) {
        if (lunchBreakTickets.isEmpty())
            lunchBreakTickets.add(defaultLunchBreakTicket);
        return lunchBreakTickets;
    }

    public LunchBreakTicketLazyDataModel getLazyLunchBreakTickets() {
        return lazyLunchBreakTickets;
    }

    public void setLazyLunchBreakTickets(LunchBreakTicketLazyDataModel lazyLunchBreakTickets) {
        this.lazyLunchBreakTickets = lazyLunchBreakTickets;
    }

    public List<LunchBreakTicket> getSelectedLunchBreakTickets() {
        return selectedLunchBreakTickets;
    }

    public void setSelectedLunchBreakTickets(List<LunchBreakTicket> selectedLunchBreakTicket) {
        this.selectedLunchBreakTickets = selectedLunchBreakTicket;
    }
    
}
