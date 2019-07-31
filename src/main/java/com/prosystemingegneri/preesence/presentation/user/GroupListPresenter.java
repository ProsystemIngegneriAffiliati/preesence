/*
 * Copyright (C) 2018 Prosystem Ingegneri Affiliati
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
package com.prosystemingegneri.preesence.presentation.user;

import com.prosystemingegneri.preesence.business.auth.boundary.GroupAppService;
import com.prosystemingegneri.preesence.business.auth.entity.GroupApp;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;

/**
 *
 * @author Davide Mainardi <ingmainardi at live.com>
 */
@Named
@ViewScoped
public class GroupListPresenter implements Serializable {
    @Inject
    private GroupAppService service;
    
    private List<GroupApp> groups;
    
    @PostConstruct
    public void updateGroups() {
        groups = service.list(0, 0, null, null, null);
    }

    public List<GroupApp> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupApp> groups) {
        this.groups = groups;
    }
    
}
