/*
 * Copyright (C) 2018-2020 Prosystem Ingegneri Affiliati
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
package com.prosystemingegneri.preesence.business.auth.entity;

import com.prosystemingegneri.preesence.business.auth.control.Role;
import java.io.Serializable;
import java.util.Set;
import static java.util.stream.Collectors.toSet;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Davide Mainardi <ingmainardi at live.com>
 */
@Entity
public class UserApp implements Serializable {

    @Id
    private @NotEmpty String username;

    @Column(nullable = false)
    private @NotEmpty String password;
    
    @ManyToOne(optional = false)
    private @NotNull GroupApp groupApp;

    public UserApp() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public GroupApp getGroupApp() {
        return groupApp;
    }

    public void setGroupApp(GroupApp groupApp) {
        this.groupApp = groupApp;
    }

    public Set<Role> getRoles() {
        return groupApp.getRoles().stream().collect(toSet());
    }

    public Set<String> getRolesAsStrings() {
        return getRoles().stream().map(Role::name).collect(toSet());
    }
    
    @Override
    public int hashCode() {
        return (username != null) 
            ? (getClass().getSimpleName().hashCode() + username.hashCode())
            : super.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return (other != null && username != null
                && other.getClass().isAssignableFrom(getClass()) 
                && getClass().isAssignableFrom(other.getClass())) 
            ? username.equals(((UserApp) other).getUsername())
            : (other == this);
    }

    @Override
    public String toString() {
        return String.format("%s[id=%s]", getClass().getSimpleName(), getUsername());
    }
}

