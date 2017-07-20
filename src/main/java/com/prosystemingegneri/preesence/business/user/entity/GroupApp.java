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
package com.prosystemingegneri.preesence.business.user.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Davide Mainardi <ingmainardi@live.com>
 */
@Entity
@Table(name = "group_app")
public class GroupApp implements Serializable {
    @Id
    @Column(name = "group_name")
    private String groupName;

    public GroupApp() {
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
    @Override
    public boolean equals(Object other) {
        return (other != null && getClass() == other.getClass() && groupName != null)
            ? groupName.equals(((GroupApp) other).groupName)
            : (other == this);
    }

    @Override
    public int hashCode() {
        return (groupName != null) 
            ? (getClass().hashCode() + groupName.hashCode())
            : super.hashCode();
    }
    
}
