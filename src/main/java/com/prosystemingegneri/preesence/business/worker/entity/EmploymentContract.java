/*
 * Copyright (C) 2019-2020 Prosystem Ingegneri Affiliati
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
import com.prosystemingegneri.preesence.business.worker.control.EmploymentContractTime;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Mainardi Davide <davide at mainardisoluzioni.com>
 */
@Entity
public class EmploymentContract extends BaseEntity {
    @Transient
    public static final int SCALE = 2; //If zero or positive, the scale is the number of digits to the right of the decimal point.
    @Transient
    public static final int PRECISION = 4;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false, columnDefinition = "smallint")
    private @NotNull EmploymentContractTime contractTime;
    
    @Column(nullable = false, unique = true)
    private @NotBlank String name;
    
    @Column(nullable = false, scale = SCALE, precision = PRECISION)
    private @NotNull @DecimalMin(value = "0", inclusive = false) BigDecimal hoursDaily;
    
    @ManyToOne
    private LunchBreakTicket lunchBreakTicket;

    public EmploymentContract() {
        hoursDaily = BigDecimal.ZERO;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getHoursDaily() {
        return hoursDaily;
    }

    public void setHoursDaily(BigDecimal hoursDaily) {
        this.hoursDaily = hoursDaily;
    }

    public LunchBreakTicket getLunchBreakTicket() {
        return lunchBreakTicket;
    }

    public void setLunchBreakTicket(LunchBreakTicket lunchBreakTicket) {
        this.lunchBreakTicket = lunchBreakTicket;
    }

    public EmploymentContractTime getContractTime() {
        return contractTime;
    }

    public void setContractTime(EmploymentContractTime contractTime) {
        this.contractTime = contractTime;
    }
    
}
