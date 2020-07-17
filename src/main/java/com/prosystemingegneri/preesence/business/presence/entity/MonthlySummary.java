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
import com.prosystemingegneri.preesence.business.presence.controller.PresenceEvent;
import com.prosystemingegneri.preesence.business.worker.entity.Worker;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;
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
    
    private Map<Long, Integer> ticketSummaries;
    
    @ElementCollection
    @MapKeyColumn(columnDefinition = "smallint")
    @MapKeyEnumerated(EnumType.ORDINAL)
    private Map<PresenceEvent, BigDecimal> presenceEventSummaries;
    
    @Column(nullable = false)
    private @NotNull @DecimalMin("0") BigDecimal totalReimburseForDistanceTraveled;
    
    @Column(nullable = false)
    private @NotNull @DecimalMin("0") Integer distanceTraveled;   //in km
    
    @Column(nullable = false)
    private @NotNull @DecimalMin("0") BigDecimal dressingAllowance;
    
    private @NotNull @DecimalMin("0") Integer presenceNumber;

    public MonthlySummary() {
        hours = BigDecimal.ZERO;
        overtime30 = BigDecimal.ZERO;
        overtime50 = BigDecimal.ZERO;
        dressingAllowance = BigDecimal.ZERO;
        presenceEventSummaries = new HashMap<>();
        ticketSummaries = new HashMap<>();
        totalReimburseForDistanceTraveled = BigDecimal.ZERO;
        distanceTraveled = 0;
        presenceNumber = 0;
    }

    public MonthlySummary(Worker worker, YearMonth month) {
        this();
        this.worker = worker;
        this.month = month;
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

    public Map<Long, Integer> getTicketSummaries() {
        return ticketSummaries;
    }

    public void setTicketSummaries(Map<Long, Integer> ticketSummaries) {
        this.ticketSummaries = ticketSummaries;
    }

    public Map<PresenceEvent, BigDecimal> getPresenceEventSummaries() {
        return presenceEventSummaries;
    }

    public void setPresenceEventSummaries(Map<PresenceEvent, BigDecimal> presenceEventSummaries) {
        this.presenceEventSummaries = presenceEventSummaries;
    }

    public BigDecimal getDressingAllowance() {
        return dressingAllowance;
    }

    public void setDressingAllowance(BigDecimal dressingAllowance) {
        this.dressingAllowance = dressingAllowance;
    }

    public Integer getPresenceNumber() {
        return presenceNumber;
    }

    public void setPresenceNumber(Integer presenceNumber) {
        this.presenceNumber = presenceNumber;
    }

    public boolean isEmpty() {
        if (
                BigDecimal.ZERO.compareTo(hours) < 0 ||
                BigDecimal.ZERO.compareTo(overtime30) < 0 ||
                BigDecimal.ZERO.compareTo(overtime50) < 0 ||
                !ticketSummaries.isEmpty() ||
                !presenceEventSummaries.isEmpty() ||
                BigDecimal.ZERO.compareTo(totalReimburseForDistanceTraveled) < 0 ||
                distanceTraveled > 0 ||
                BigDecimal.ZERO.compareTo(dressingAllowance) < 0 ||
                presenceNumber > 0
                )
            return false;
        
        return true;
    }
    
}
