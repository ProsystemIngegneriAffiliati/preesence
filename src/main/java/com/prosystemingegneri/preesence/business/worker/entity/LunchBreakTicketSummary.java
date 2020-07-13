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
package com.prosystemingegneri.preesence.business.worker.entity;

import com.prosystemingegneri.preesence.business.entity.BaseEntity;
import com.prosystemingegneri.preesence.business.presence.entity.MonthlySummary;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Mainardi Davide <davide at mainardisoluzioni.com>
 */
@Entity
public class LunchBreakTicketSummary extends BaseEntity {
    @ManyToOne(optional = false)
    private @NotNull MonthlySummary monthlySummary;
    
    @ManyToOne(optional = false)
    private @NotNull LunchBreakTicket lunchBreakTicket;
    
    @Column(nullable = false)
    private @NotNull @DecimalMin(value = "0", inclusive = false) Integer quantity;

    public LunchBreakTicketSummary() {
    }

    public LunchBreakTicketSummary(LunchBreakTicket lunchBreakTicket, Integer quantity) {
        this();
        this.lunchBreakTicket = lunchBreakTicket;
        this.quantity = quantity;
    }

    public MonthlySummary getMonthlySummary() {
        return monthlySummary;
    }

    public void setMonthlySummary(MonthlySummary monthlySummary) {
        this.monthlySummary = monthlySummary;
    }

    public LunchBreakTicket getLunchBreakTicket() {
        return lunchBreakTicket;
    }

    public void setLunchBreakTicket(LunchBreakTicket lunchBreakTicket) {
        this.lunchBreakTicket = lunchBreakTicket;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
}
