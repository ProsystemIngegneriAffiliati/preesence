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
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Mainardi Davide <davide at mainardisoluzioni.com>
 */
@Entity
public class PresenceEventSummary extends BaseEntity {
    @ManyToOne(optional = false)
    private @NotNull MonthlySummary monthlySummary;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false, columnDefinition = "smallint")
    private @NotNull PresenceEvent event;
    
    @Column(nullable = false)
    private @NotNull @DecimalMin("0") BigDecimal hours;

    public PresenceEventSummary() {
        hours = BigDecimal.ZERO;
    }

    public PresenceEventSummary(PresenceEvent event, BigDecimal hours) {
        this();
        this.event = event;
        this.hours = hours;
    }

    public MonthlySummary getMonthlySummary() {
        return monthlySummary;
    }

    public void setMonthlySummary(MonthlySummary monthlySummary) {
        this.monthlySummary = monthlySummary;
    }

    public PresenceEvent getEvent() {
        return event;
    }

    public void setEvent(PresenceEvent event) {
        this.event = event;
    }

    public BigDecimal getHours() {
        return hours;
    }

    public void setHours(BigDecimal hours) {
        this.hours = hours;
    }
    
}
