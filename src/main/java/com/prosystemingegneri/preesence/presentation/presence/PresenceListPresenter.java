/*
 * Copyright (C) 2017-2020 Prosystem Ingegneri Affiliati
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
package com.prosystemingegneri.preesence.presentation.presence;

import com.prosystemingegneri.preesence.business.presence.boundary.PresenceService;
import com.prosystemingegneri.preesence.business.presence.entity.Presence;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;


/**
 *
 * @author Davide Mainardi <ingmainardi@live.com>
 */
@Named
@ViewScoped
public class PresenceListPresenter implements Serializable{
    @Inject
    private PresenceService service;
    
    private PresenceLazyDataModel lazyPresences;
    private List<Presence> selectedPresences;
    
    @PostConstruct
    public void init() {
        lazyPresences = new PresenceLazyDataModel(service);
    }
    
    public void delete() {
        if (selectedPresences != null && !selectedPresences.isEmpty())
            for (Presence presence : selectedPresences)
                service.delete(presence.getId());
        else
            Messages.create("missingSelection").warn().detail("missingSelection.tip").add();
    }

    public PresenceLazyDataModel getLazyPresences() {
        return lazyPresences;
    }

    public void setLazyPresences(PresenceLazyDataModel lazyPresences) {
        this.lazyPresences = lazyPresences;
    }

    public List<Presence> getSelectedPresences() {
        return selectedPresences;
    }

    public void setSelectedPresences(List<Presence> selectedPresence) {
        this.selectedPresences = selectedPresence;
    }
    
}