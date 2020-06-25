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
package com.prosystemingegneri.preesence.business.holiday.entity;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Mainardi Davide <davide at mainardisoluzioni.com>
 */
@Entity
public class BankHoliday implements Serializable {
    @Id
    @Column(nullable = false, columnDefinition = "date", unique = true, updatable = false)
    private @NotNull LocalDate daytime;
    
    @Column(nullable = false, updatable = false)
    private @NotBlank String name;

    public BankHoliday() {
    }

    public BankHoliday(LocalDate daytime, String name) {
        this();
        this.daytime = daytime;
        this.name = name;
    }

    public LocalDate getDaytime() {
        return daytime;
    }

    public void setDaytime(LocalDate daytime) {
        this.daytime = daytime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
