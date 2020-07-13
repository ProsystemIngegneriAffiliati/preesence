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
package com.prosystemingegneri.preesence.business.presence.entity;

import com.prosystemingegneri.preesence.business.entity.BaseEntity;
import com.prosystemingegneri.preesence.business.worker.entity.LunchBreakTicketSummary;
import com.prosystemingegneri.preesence.business.worker.entity.Worker;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Mainardi Davide <davide at mainardisoluzioni.com>
 */
@Entity
public class MonthlySummary extends BaseEntity {
    
    @ManyToOne(optional = false)
    private @NotNull Worker worker;
    
    @Column(nullable = false)
    private @NotNull YearMonth month;
    
    @Column(nullable = false)
    private @NotNull @DecimalMin("0") BigDecimal hours;
    
    @Column(nullable = false)
    private @NotNull @DecimalMin("0") BigDecimal overtime30;
    
    @Column(nullable = false)
    private @NotNull @DecimalMin("0") BigDecimal overtime50;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "monthlySummary", orphanRemoval = true)
    private List<PresenceEventSummary> presenceEventSummaries;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "monthlySummary", orphanRemoval = true)
    private List<LunchBreakTicketSummary> ticketSummaries;
    
    @Column(nullable = false)
    private @NotNull @DecimalMin("0") BigDecimal totalReimburseForDistanceTraveled;
    
    @Column(nullable = false)
    private @NotNull @DecimalMin("0") Integer distanceTraveled;   //in km

    public MonthlySummary() {
        hours = BigDecimal.ZERO;
        overtime30 = BigDecimal.ZERO;
        overtime50 = BigDecimal.ZERO;
        presenceEventSummaries = new ArrayList<>();
        ticketSummaries = new ArrayList<>();
        totalReimburseForDistanceTraveled = BigDecimal.ZERO;
        distanceTraveled = 0;
    }

    public BigDecimal getHours() {
        return hours;
    }

    public void setHours(BigDecimal hours) {
        this.hours = hours;
    }

    public BigDecimal getOvertime30() {
        return overtime30;
    }

    public void setOvertime30(BigDecimal overtime30) {
        this.overtime30 = overtime30;
    }

    public BigDecimal getOvertime50() {
        return overtime50;
    }

    public void setOvertime50(BigDecimal overtime50) {
        this.overtime50 = overtime50;
    }
    
    public void addPresenceEventSummary(PresenceEventSummary eventSummary) {
        if (!presenceEventSummaries.contains(eventSummary)) {
            presenceEventSummaries.add(eventSummary);
            eventSummary.setMonthlySummary(this);
        }
    }
    
    public void removePresenceEventSummary(PresenceEventSummary eventSummary) {
        if (presenceEventSummaries.contains(eventSummary)) {
            presenceEventSummaries.remove(eventSummary);
            eventSummary.setMonthlySummary(null);
        }
    }

    public List<PresenceEventSummary> getPresenceEventSummaries() {
        return presenceEventSummaries;
    }
    
    public void addLunchBreakTicketSummary(LunchBreakTicketSummary ticketSummary) {
        if (!ticketSummaries.contains(ticketSummary)) {
            ticketSummaries.add(ticketSummary);
            ticketSummary.setMonthlySummary(this);
        }
    }
    
    public void removeLunchBreakTicketSummary(LunchBreakTicketSummary ticketSummary) {
        if (ticketSummaries.contains(ticketSummary)) {
            ticketSummaries.remove(ticketSummary);
            ticketSummary.setMonthlySummary(null);
        }
    }
    
    public List<LunchBreakTicketSummary> getTicketSummaries() {
        return ticketSummaries;
    }

    public BigDecimal getTotalReimburseForDistanceTraveled() {
        return totalReimburseForDistanceTraveled;
    }

    public void setTotalReimburseForDistanceTraveled(BigDecimal totalReimburseForDistanceTraveled) {
        this.totalReimburseForDistanceTraveled = totalReimburseForDistanceTraveled;
    }

    public Integer getDistanceTraveled() {
        return distanceTraveled;
    }

    public void setDistanceTraveled(Integer distanceTraveled) {
        this.distanceTraveled = distanceTraveled;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public YearMonth getMonth() {
        return month;
    }

    public void setMonth(YearMonth month) {
        this.month = month;
    }
    
}
