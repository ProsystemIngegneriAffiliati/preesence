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
package com.prosystemingegneri.preesence.business.presence.boundary;

import com.prosystemingegneri.preesence.business.presence.entity.Presence;
import com.prosystemingegneri.preesence.business.presence.entity.Presence_;
import com.prosystemingegneri.preesence.business.worker.boundary.WorkerService;
import com.prosystemingegneri.preesence.business.worker.entity.Worker;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Davide Mainardi <ingmainardi@live.com>
 */
@Stateless
public class PresenceService implements Serializable {
    @PersistenceContext
    EntityManager em;
    
    @Inject
    WorkerService workerService;
    
    public Presence savePresence(Presence presence) {
        Presence tempPresence = presence;
        
        if (presence.getId() == null)
            em.persist(presence);
        else
            tempPresence = em.merge(presence);
        
        Worker worker = tempPresence.getWorker();
        worker.addPresence(tempPresence);
        workerService.saveWorker(worker);
        
        return tempPresence;
    }
    
    public Presence readPresence(Long id) {
        return em.find(Presence.class, id);
    }
    
    public void deletePresence(Long id) {
        Presence presence = readPresence(id);
        
        Worker worker = presence.getWorker();
        worker.removePresence(presence);
        workerService.saveWorker(worker);
        
        em.remove(presence);
    }
    
    public List<Presence> listPresences(Date start, Date end, Worker worker, Boolean isPresenceNotEnded) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Presence> query = cb.createQuery(Presence.class);
        Root<Presence> root = query.from(Presence.class);
        CriteriaQuery<Presence> select = query.select(root).distinct(true);
        
        List<Predicate> conditions = new ArrayList<>();
        
        if (isPresenceNotEnded != null && isPresenceNotEnded) {
            conditions.add(cb.isNull(root.get(Presence_.endTimestamp)));
            if (start != null)
                conditions.add(cb.greaterThanOrEqualTo(root.<Date>get(Presence_.startTimestamp), start));
        }
        
        if (start != null && end != null)
            conditions.add(cb.between(root.<Date>get(Presence_.startTimestamp), start, end));
        
        if (worker != null)
            conditions.add(cb.equal(root.get(Presence_.worker), worker));

        if (!conditions.isEmpty())
            query.where(conditions.toArray(new Predicate[conditions.size()]));
        
        query.orderBy(cb.desc(root.get(Presence_.startTimestamp)));

        return em.createQuery(select).getResultList();
    }
}
