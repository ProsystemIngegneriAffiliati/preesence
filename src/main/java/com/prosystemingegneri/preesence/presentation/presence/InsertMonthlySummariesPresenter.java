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
package com.prosystemingegneri.preesence.presentation.presence;

import com.prosystemingegneri.preesence.business.presence.boundary.MonthlySummaryService;
import com.prosystemingegneri.preesence.business.presence.controller.PresenceEvent;
import com.prosystemingegneri.preesence.business.presence.entity.MonthlySummary;
import com.prosystemingegneri.preesence.business.worker.boundary.LunchBreakTicketService;
import com.prosystemingegneri.preesence.business.worker.boundary.WorkerService;
import com.prosystemingegneri.preesence.business.worker.entity.LunchBreakTicket;
import com.prosystemingegneri.preesence.business.worker.entity.Worker;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotEmpty;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

/**
 *
 * @author Mainardi Davide <davide at mainardisoluzioni.com>
 */
@Named
@ViewScoped
public class InsertMonthlySummariesPresenter implements Serializable {
    @Inject
    private FacesContext facesContext;
    
    private String monthStr;
    
    @Inject
    private LunchBreakTicketService lunchBreakTicketService;
    private List<LunchBreakTicket> lunchBreakTickets = new ArrayList<>();
    
    private List<PresenceEvent> events = new ArrayList<>();
    
    @Inject
    private WorkerService workerService;
    private List<Worker> workers = new ArrayList<>();
    private @NotEmpty List<Worker> selectedWorkers = new ArrayList<>();
    
    private @NotEmpty List<MonthlySummary> summaries = new ArrayList<>();
    private MonthlySummary footerSummary = new MonthlySummary();
    private List<MonthlySummary> summariesToBeDeleted = new ArrayList<>();
    
    @Inject
    private MonthlySummaryService service;
    
    @PostConstruct
    public void init() {
        workers = workerService.list(0, 0, null, null, null, null);
        selectedWorkers.clear();
        for (Worker worker : workers)
            if (worker.getDismission() == null)
                selectedWorkers.add(worker);
        monthStr = YearMonth.now().minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }
    
    public String reload() {
        return facesContext.getViewRoot().getViewId() + "?faces-redirect=true";
    }
    
    public String save() {
        for (MonthlySummary toBeDeleted : summariesToBeDeleted)
            service.delete(toBeDeleted.getId());
        for (MonthlySummary summary : summaries) {
            if (!summary.isEmpty())
                service.save(summary);
        }
        
        Messages.create("success").detail("saved").flash().add();
        
        return reload();
    }
    
    public void populateSummaries() {
        summaries = service.populateSummaries(selectedWorkers, YearMonth.parse(monthStr));
        events.clear();
        lunchBreakTickets.clear();
        List<Long> lunchBreakTicketIds = new ArrayList<>();
        for (MonthlySummary summary : summaries) {
            for (PresenceEvent event : summary.getPresenceEventSummaries().keySet())
                if (!events.contains(event))
                    events.add(event);
            for (Long idLunchBreakTicket : summary.getTicketSummaries().keySet())
                if (!lunchBreakTicketIds.contains(idLunchBreakTicket))
                    lunchBreakTicketIds.add(idLunchBreakTicket);
        }
        for (Long lunchBreakTicketId : lunchBreakTicketIds)
            lunchBreakTickets.add(lunchBreakTicketService.find(lunchBreakTicketId));
        updateFooterSummary();
    }
    
    public void reset(MonthlySummary summary) {
        if (summary.getId() != null)
            summariesToBeDeleted.add(summary);
        int index = summaries.indexOf(summary);
        summaries.remove(index);
        summaries.add(index, service.populateWorkerSummary(summary.getWorker(), summary.getMonth()));
        updateFooterSummary();
    }
    
    public void updateFooterSummary() {
        footerSummary = new MonthlySummary();
        Integer distanceTraveled = 0;
        BigDecimal dressingAllowance = BigDecimal.ZERO;
        BigDecimal hours = BigDecimal.ZERO;
        BigDecimal overtime30 = BigDecimal.ZERO;
        BigDecimal overtime50 = BigDecimal.ZERO;
        Integer presenceNumber = 0;
        BigDecimal totalReimburseForDistanceTraveled = BigDecimal.ZERO;
        
        for (MonthlySummary summary : summaries) {
            distanceTraveled += summary.getDistanceTraveled();
            dressingAllowance = dressingAllowance.add(summary.getDressingAllowance());
            hours = hours.add(summary.getHours());
            overtime30 = overtime30.add(summary.getOvertime30());
            overtime50 = overtime50.add(summary.getOvertime50());
            presenceNumber += summary.getPresenceNumber();
            totalReimburseForDistanceTraveled = totalReimburseForDistanceTraveled.add(summary.getTotalReimburseForDistanceTraveled());
            for (Map.Entry<Long, Integer> entry : summary.getTicketSummaries().entrySet())
                footerSummary.getTicketSummaries().merge(entry.getKey(), entry.getValue(), Integer::sum);
            for (Map.Entry<PresenceEvent, BigDecimal> entry : summary.getPresenceEventSummaries().entrySet())
                footerSummary.getPresenceEventSummaries().merge(entry.getKey(), entry.getValue(), BigDecimal::add);
        }
        
        footerSummary.setDistanceTraveled(distanceTraveled);
        footerSummary.setDressingAllowance(dressingAllowance);
        footerSummary.setHours(hours);
        footerSummary.setOvertime30(overtime30);
        footerSummary.setOvertime50(overtime50);
        footerSummary.setPresenceNumber(presenceNumber);
        footerSummary.setTotalReimburseForDistanceTraveled(totalReimburseForDistanceTraveled);
    }

    public List<Worker> getSelectedWorkers() {
        return selectedWorkers;
    }

    public void setSelectedWorkers(List<Worker> selectedWorkers) {
        this.selectedWorkers = selectedWorkers;
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    }

    public List<MonthlySummary> getSummaries() {
        return summaries;
    }

    public void setSummaries(List<MonthlySummary> summaries) {
        this.summaries = summaries;
    }

    public String getMonthStr() {
        return monthStr;
    }

    public void setMonthStr(String monthStr) {
        this.monthStr = monthStr;
    }

    public List<LunchBreakTicket> getLunchBreakTickets() {
        return lunchBreakTickets;
    }

    public void setLunchBreakTickets(List<LunchBreakTicket> lunchBreakTickets) {
        this.lunchBreakTickets = lunchBreakTickets;
    }

    public List<PresenceEvent> getEvents() {
        return events;
    }

    public void setEvents(List<PresenceEvent> events) {
        this.events = events;
    }

    public MonthlySummary getFooterSummary() {
        return footerSummary;
    }

    public void setFooterSummary(MonthlySummary footerSummary) {
        this.footerSummary = footerSummary;
    }
    
}
