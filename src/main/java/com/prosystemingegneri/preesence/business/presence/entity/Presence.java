/*
 * Copyright (C) 2017 Prosystem Ingegneri Affiliati
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
import com.prosystemingegneri.preesence.business.worker.entity.Worker;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
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
    
    @Column(nullable = false)
    private @NotNull LocalDateTime startTimeStamp;
    
    private LocalDateTime endTimeStamp;
    
    private String notes;

    public Presence() {
        startTimeStamp = LocalDateTime.now();
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public LocalDateTime getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(LocalDateTime startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public LocalDateTime getEndTimeStamp() {
        return endTimeStamp;
    }

    public void setEndTimeStamp(LocalDateTime endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    
}
