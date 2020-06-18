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
package com.prosystemingegneri.preesence.business.worker.entity;

import com.prosystemingegneri.preesence.business.auth.entity.UserApp;
import com.prosystemingegneri.preesence.business.entity.BaseEntity;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Davide Mainardi <ingmainardi@live.com>
 */
@Entity
public class Worker extends BaseEntity {
    @Column(nullable = false, unique = true)
    private @NotEmpty String name;
    
    @Column(columnDefinition = "date")
    private LocalDate dismission;
    
    @ManyToOne(optional = false)
    private @NotNull EmploymentContract contract;
    
    @OneToOne
    private UserApp userApp;
    
    @Column(nullable = false)
    private @NotNull Boolean ticketEligibleInLunchBreak; //se vero allora il ticket viene concesso quando si lavorano più di cinque ore, se falso le ore devono essere consecutive

    public Worker() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserApp getUserApp() {
        return userApp;
    }

    public void setUserApp(UserApp userApp) {
        this.userApp = userApp;
    }

    public LocalDate getDismission() {
        return dismission;
    }

    public void setDismission(LocalDate dismission) {
        this.dismission = dismission;
    }

    public EmploymentContract getContract() {
        return contract;
    }

    public void setContract(EmploymentContract contract) {
        this.contract = contract;
    }

    public Boolean getTicketEligibleInLunchBreak() {
        return ticketEligibleInLunchBreak;
    }

    public void setTicketEligibleInLunchBreak(Boolean ticketEligibleInLunchBreak) {
        this.ticketEligibleInLunchBreak = ticketEligibleInLunchBreak;
    }
    
}
