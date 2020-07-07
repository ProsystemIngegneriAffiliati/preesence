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
package com.prosystemingegneri.preesence.business.worker.boundary;

import com.prosystemingegneri.preesence.business.worker.entity.LunchBreakTicket;
import com.prosystemingegneri.preesence.business.worker.entity.LunchBreakTicket_;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
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
 * @author Mainardi Davide <davide@mainardisoluzioni.com>
 */
@Stateless
public class LunchBreakTicketService {
    @PersistenceContext
    EntityManager em;
    
    public LunchBreakTicket create() {
        return new LunchBreakTicket();
    }
    
    public LunchBreakTicket save(LunchBreakTicket lunchBreakTicket) {
        if (lunchBreakTicket.getId() == null)
            em.persist(lunchBreakTicket);
        else
            return em.merge(lunchBreakTicket);
        
        return lunchBreakTicket;
    }
    
    public LunchBreakTicket find(Long id) {
        return em.find(LunchBreakTicket.class, id);
    }
    
    public void delete(Long id) {
        em.remove(find(id));
    }
    
    public List<LunchBreakTicket> list(int first, int pageSize, String sortField, Boolean isAscending, String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<LunchBreakTicket> query = cb.createQuery(LunchBreakTicket.class);
        Root<LunchBreakTicket> root = query.from(LunchBreakTicket.class);
        CriteriaQuery<LunchBreakTicket> select = query.select(root).distinct(true);
        
        List<Predicate> conditions = calculateConditions(cb, root, name);

        if (!conditions.isEmpty())
            query.where(conditions.toArray(new Predicate[conditions.size()]));
        
        Order order = cb.asc(root.get(LunchBreakTicket_.name));
        if (isAscending != null && sortField != null && !sortField.isEmpty()) {
            Path<?> path;
            switch (sortField) {
                case "name":
                    path = root.get(LunchBreakTicket_.name);
                    break;
                case "value":
                    path = root.get(LunchBreakTicket_.value);
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
        
        TypedQuery<LunchBreakTicket> typedQuery = em.createQuery(select);
        if (pageSize > 0) {
            typedQuery.setMaxResults(pageSize);
            typedQuery.setFirstResult(first);   
        }

        return typedQuery.getResultList();
    }
    
    public Long getCount(String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<LunchBreakTicket> root = query.from(LunchBreakTicket.class);
        CriteriaQuery<Long> select = query.select(cb.count(root));

        List<Predicate> conditions = calculateConditions(cb, root, name);

        if (!conditions.isEmpty())
            query.where(conditions.toArray(new Predicate[conditions.size()]));

        return em.createQuery(select).getSingleResult();
    }
    
    private List<Predicate> calculateConditions(CriteriaBuilder cb, Root<LunchBreakTicket> root, String name) {
        List<Predicate> conditions = new ArrayList<>();
        
        //LunchBreakTicket's name
        if (name != null && !name.isEmpty())
            conditions.add(cb.like(cb.lower(root.get(LunchBreakTicket_.name)), "%" + name.toLowerCase() + "%"));
        
        return conditions;
    }
    
}
