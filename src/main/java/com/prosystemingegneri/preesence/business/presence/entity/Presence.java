/*
 * Copyright (C) 2017-2019 Prosystem Ingegneri Affiliati
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
import com.prosystemingegneri.preesence.business.presence.controller.EndAfterStart;
import com.prosystemingegneri.preesence.business.presence.controller.PresenceEvent;
import com.prosystemingegneri.preesence.business.worker.entity.Worker;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Davide Mainardi <ingmainardi@live.com>
 */
@Entity
@EndAfterStart
public class Presence extends BaseEntity {
    @ManyToOne(optional = false)
    private @NotNull Worker worker;
    
    @Column(columnDefinition = "date")
    private LocalDate daytime;
    
    @Column(nullable = false, columnDefinition = "time")
    private @NotNull LocalTime startMorning;
    
    @Column(nullable = false, columnDefinition = "time")
    private @NotNull LocalTime endMorning;
    
    @Column(nullable = false, columnDefinition = "time")
    private @NotNull LocalTime startAfternoon;
    
    @Column(nullable = false, columnDefinition = "time")
    private @NotNull LocalTime endAfternoon;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false, columnDefinition = "smallint")
    private @NotNull PresenceEvent event;
    
    private String notes;

    public Presence() {
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
