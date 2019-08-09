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
package com.prosystemingegneri.preesence.business.presence.boundary;

import com.prosystemingegneri.preesence.business.holiday.boundary.HolidayService;
import com.prosystemingegneri.preesence.business.presence.controller.PresenceEvent;
import com.prosystemingegneri.preesence.business.presence.entity.Presence;
import com.prosystemingegneri.preesence.business.presence.entity.Presence_;
import com.prosystemingegneri.preesence.business.worker.boundary.WorkerService;
import com.prosystemingegneri.preesence.business.worker.entity.Worker;
import com.prosystemingegneri.preesence.business.worker.entity.Worker_;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
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
    
    @Inject
    HolidayService holidayService;
    
    public Presence create() {
        return new Presence();
    }
    
    public Presence save(Presence presence) {
        Presence tempPresence = presence;
        
        if (presence.getId() == null)
            em.persist(presence);
        else
            tempPresence = em.merge(presence);
        
        return tempPresence;
    }
    
    public Presence find(Long id) {
        return em.find(Presence.class, id);
    }
    
    public void delete(Long id) {
        em.remove(find(id));
    }
    
    public List<Presence> list(int first, int pageSize, String sortField, Boolean isAscending, LocalDateTime start, LocalDateTime end, Worker worker, String workerName, Boolean isNotEnded) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Presence> query = cb.createQuery(Presence.class);
        Root<Presence> root = query.from(Presence.class);
        CriteriaQuery<Presence> select = query.select(root).distinct(true);
        
        List<Predicate> conditions = calculateConditions(cb, root, start, end, worker, workerName, isNotEnded);

        if (!conditions.isEmpty())
            query.where(conditions.toArray(new Predicate[conditions.size()]));
        
        Order order = cb.desc(root.get(Presence_.startTimeStamp));
        if (isAscending != null && sortField != null && !sortField.isEmpty()) {
            Path<?> path;
            switch (sortField) {
                case "start":
                    path = root.get(Presence_.startTimeStamp);
                    break;
                case "end":
                    path = root.get(Presence_.endTimeStamp);
                    break;
                case "workerName":
                    path = root.get(Presence_.worker).get(Worker_.name);
                    break;
                default:
                    path = root.get(sortField);
            }
            if (isAscending)
                order = cb.asc(path);
            else
                order = cb.desc(path);
        }
        query.orderBy(order);
        
        TypedQuery<Presence> typedQuery = em.createQuery(select);
        if (pageSize > 0) {
            typedQuery.setMaxResults(pageSize);
            typedQuery.setFirstResult(first);   
        }

        return typedQuery.getResultList();
    }
    
    public Long getCount(LocalDateTime start, LocalDateTime end, Worker worker, String workerName, Boolean isNotEnded) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Presence> root = query.from(Presence.class);
        CriteriaQuery<Long> select = query.select(cb.count(root));

        List<Predicate> conditions = calculateConditions(cb, root, start, end, worker, workerName, isNotEnded);

        if (!conditions.isEmpty())
            query.where(conditions.toArray(new Predicate[conditions.size()]));

        return em.createQuery(select).getSingleResult();
    }
    
    private List<Predicate> calculateConditions(CriteriaBuilder cb, Root<Presence> root, LocalDateTime start, LocalDateTime end, Worker worker, String workerName, Boolean isNotEnded) {
        List<Predicate> conditions = new ArrayList<>();
        
        if (isNotEnded != null && isNotEnded) {
            conditions.add(cb.isNull(root.get(Presence_.endTimeStamp)));
            if (start != null)
                conditions.add(cb.greaterThanOrEqualTo(root.get(Presence_.startTimeStamp), start));
        }
        
        if (start != null && end != null)
            conditions.add(cb.or(
                    cb.between(root.get(Presence_.startTimeStamp), start, end),
                    cb.between(root.get(Presence_.endTimeStamp), start, end)));
        
        if (worker != null)
            conditions.add(cb.equal(root.get(Presence_.worker), worker));
        
        return conditions;
    }
    
    public List<Presence> populateDays(Worker worker, LocalDate start, LocalDate end) {
        List<Presence> presences = new ArrayList<>();
        for (int i = 0; i < ChronoUnit.DAYS.between(start, end) + 1; i++) {
            Presence presence = create();
            presence.setWorker(worker);
            LocalDate currentDay = start.plusDays(i);
            presence.setDaytime(LocalDate.from(currentDay));
            if (currentDay.getDayOfWeek().equals(DayOfWeek.SUNDAY) || holidayService.find(currentDay) != null)
                presence.setEvent(PresenceEvent.HOLIDAY);
            else
                presence.setEvent(PresenceEvent.WORK);
            presences.add(presence);
        }
        
        return presences;
    }
}
