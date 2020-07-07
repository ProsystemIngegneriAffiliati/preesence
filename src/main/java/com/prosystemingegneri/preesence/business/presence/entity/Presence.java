/*
 * Copyright (C) 2017-2020 Prosystem Ingegneri Affiliati
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
import com.prosystemingegneri.preesence.business.presence.controller.CompulsoryPreesenceEventWhenDifference;
import com.prosystemingegneri.preesence.business.presence.controller.EndAfterStart;
import com.prosystemingegneri.preesence.business.presence.controller.PresenceEvent;
import com.prosystemingegneri.preesence.business.worker.entity.LunchBreakTicket;
import com.prosystemingegneri.preesence.business.worker.entity.Worker;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Davide Mainardi <ingmainardi@live.com>
 */
@Entity
@EndAfterStart
@CompulsoryPreesenceEventWhenDifference
public class Presence extends BaseEntity {
    @ManyToOne(optional = false)
    private @NotNull Worker worker;
    
    @Column(columnDefinition = "date")
    private LocalDate daytime;
    
    @Column(columnDefinition = "time")
    private LocalTime startMorning;
    
    @Column(columnDefinition = "time")
    private LocalTime endMorning;
    
    @Column(columnDefinition = "time")
    private LocalTime startAfternoon;
    
    @Column(columnDefinition = "time")
    private LocalTime endAfternoon;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false, columnDefinition = "smallint")
    private @NotNull PresenceEvent event;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(columnDefinition = "smallint")
    private PresenceEvent differenceEvent; //what the worker did if he/she worked less hours than expected?
    
    @Transient
    private BigDecimal total;
    
    @Transient
    private final BigDecimal REIMBURSE = new BigDecimal(0.07);     //€/km
    
    @Column(nullable = false)
    private @NotNull @DecimalMin("0") Integer distanceTraveled;   //in km
    
    @ManyToOne
    private LunchBreakTicket lunchBreakTicket;
    
    @Transient
    private BigDecimal difference;
    
    @Transient
    private BigDecimal overtime30;
    
    @Transient
    private BigDecimal overtime50;
    
    @Transient
    private final BigDecimal HOURS_FOR_TICKET_FULL_TIME = new BigDecimal(5);
    @Transient
    private final BigDecimal HOURS_FOR_TICKET_PART_TIME = new BigDecimal(4);
    
    private String notes;

    public Presence() {
        distanceTraveled = 0;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public LocalDate getDaytime() {
        return daytime;
    }

    public void setDaytime(LocalDate daytime) {
        this.daytime = daytime;
    }

    public LocalTime getStartMorning() {
        return startMorning;
    }

    public void setStartMorning(LocalTime startMorning) {
        this.startMorning = startMorning;
    }

    public LocalTime getEndMorning() {
        return endMorning;
    }

    public void setEndMorning(LocalTime endMorning) {
        this.endMorning = endMorning;
    }

    public LocalTime getStartAfternoon() {
        return startAfternoon;
    }

    public void setStartAfternoon(LocalTime startAfternoon) {
        this.startAfternoon = startAfternoon;
    }

    public LocalTime getEndAfternoon() {
        return endAfternoon;
    }

    public void setEndAfternoon(LocalTime endAfternoon) {
        this.endAfternoon = endAfternoon;
    }

    public PresenceEvent getEvent() {
        return event;
    }

    public void setEvent(PresenceEvent event) {
        this.event = event;
    }

    public PresenceEvent getDifferenceEvent() {
        return differenceEvent;
    }

    public void setDifferenceEvent(PresenceEvent differenceEvent) {
        this.differenceEvent = differenceEvent;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    
    private void updateTotal() {
        total = null;
        if (event != null && event != PresenceEvent.HOLIDAY) {
            total = hoursBetweenTimes(startMorning, endMorning, total);
            total = hoursBetweenTimes(startAfternoon, endAfternoon, total);
            if (total == null && startMorning != null && endAfternoon != null)
                total = hoursBetweenTimes(startMorning, endAfternoon, total);
        }
    }

    public BigDecimal getDifference() {
        return difference;
    }

    public void setDifference(BigDecimal difference) {
        this.difference = difference;
    }
    
    private void updateDifference() {
        difference = BigDecimal.ZERO;
        if (worker != null && total != null)
            difference = worker.getContract().getHoursDaily().subtract(total);
    }
    
    private BigDecimal hoursBetweenTimes(LocalTime start, LocalTime end, BigDecimal previous) {
        if (start != null && end != null) {
            if (previous == null)
                previous = BigDecimal.ZERO;
            previous = previous.add(BigDecimal.valueOf(ChronoUnit.MINUTES.between(start, end)).divide(BigDecimal.valueOf(60), 2, RoundingMode.DOWN));
        }
        
        return previous;
    }

    public BigDecimal getOvertime30() {
        return overtime30;
    }

    public void setOvertime30(BigDecimal overtime30) {
        this.overtime30 = overtime30;
    }
    
    private void updateOvertime30() {
        overtime30 = null;
        if (total != null)
            if (total.compareTo(worker.getContract().getHoursDaily()) > 0)
                overtime30 = total.subtract(worker.getContract().getHoursDaily());
    }

    public BigDecimal getOvertime50() {
        return overtime50;
    }

    public void setOvertime50(BigDecimal overtime50) {
        this.overtime50 = overtime50;
    }
    
    private void updateOvertime50() {
        overtime50 = null;
        if (event != null && event == PresenceEvent.HOLIDAY) {
            overtime50 = hoursBetweenTimes(startMorning, endMorning, overtime50);
            overtime50 = hoursBetweenTimes(startAfternoon, endAfternoon, overtime50);
        }
    }
    
    public void updateAllTimings() {
        updateTotal();
        updateDifference();
        updateOvertime30();
        updateOvertime50();
        updateTicket();
    }

    public Integer getDistanceTraveled() {
        return distanceTraveled;
    }

    public void setDistanceTraveled(Integer distanceTraveled) {
        this.distanceTraveled = distanceTraveled;
    }
    
    public BigDecimal getMileageReimbursement() {
        return REIMBURSE.multiply(new BigDecimal(distanceTraveled));
    }
    
    private void updateTicket() {
        if (lunchBreakTicket == null && worker != null && event.equals(PresenceEvent.WORK)) {
            BigDecimal workingHours = hoursBetweenTimes(startAfternoon, endAfternoon, hoursBetweenTimes(startMorning, endMorning, BigDecimal.ZERO));
            switch (worker.getContract().getContractTime()) {
                case FULL_TIME:
                    if (workingHours.compareTo(HOURS_FOR_TICKET_FULL_TIME) > 0)
                        lunchBreakTicket = worker.getContract().getLunchBreakTicket();
                    break;
                case PART_TIME:
                    if (workingHours.compareTo(HOURS_FOR_TICKET_PART_TIME) > 0)
                        lunchBreakTicket = worker.getContract().getLunchBreakTicket();
                    break;
                default:
                    return;
            }
        }
    }

    public LunchBreakTicket getLunchBreakTicket() {
        return lunchBreakTicket;
    }

    public void setLunchBreakTicket(LunchBreakTicket lunchBreakTicket) {
        this.lunchBreakTicket = lunchBreakTicket;
    }
    
}
