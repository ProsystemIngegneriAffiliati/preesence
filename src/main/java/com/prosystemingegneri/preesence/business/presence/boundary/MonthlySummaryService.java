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
package com.prosystemingegneri.preesence.business.presence.boundary;

import com.prosystemingegneri.preesence.business.presence.entity.MonthlySummary;
import com.prosystemingegneri.preesence.business.presence.entity.MonthlySummary_;
import com.prosystemingegneri.preesence.business.worker.entity.Worker;
import com.prosystemingegneri.preesence.business.worker.entity.Worker_;
import java.time.YearMonth;
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
 * @author Mainardi Davide <davide at mainardisoluzioni.com>
 */
@Stateless
public class MonthlySummaryService {
    @PersistenceContext
    EntityManager em;
    
    public MonthlySummary create() {
        return new MonthlySummary();
    }
    
    public MonthlySummary save(MonthlySummary monthlySummary) {
        MonthlySummary tempMonthlySummary = monthlySummary;
        
        if (monthlySummary.getId() == null)
            em.persist(monthlySummary);
        else
            tempMonthlySummary = em.merge(monthlySummary);
        
        return tempMonthlySummary;
    }
    
    public MonthlySummary find(Long id) {
        return em.find(MonthlySummary.class, id);
    }
    
    public void delete(Long id) {
        em.remove(find(id));
    }
    
    public List<MonthlySummary> list(int first, int pageSize, String sortField, Boolean isAscending, YearMonth start, YearMonth end, Worker worker) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MonthlySummary> query = cb.createQuery(MonthlySummary.class);
        Root<MonthlySummary> root = query.from(MonthlySummary.class);
        CriteriaQuery<MonthlySummary> select = query.select(root).distinct(true);
        
        List<Predicate> conditions = calculateConditions(cb, root, start, end, worker);

        if (!conditions.isEmpty())
            query.where(conditions.toArray(new Predicate[conditions.size()]));
        
        Order order = cb.desc(root.get(MonthlySummary_.month));
        if (isAscending != null && sortField != null && !sortField.isEmpty()) {
            Path<?> path;
            switch (sortField) {
                case "month":
                    path = root.get(MonthlySummary_.month);
                    break;
                case "nameWorker":
                    path = root.get(MonthlySummary_.worker).get(Worker_.name);
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
        
        TypedQuery<MonthlySummary> typedQuery = em.createQuery(select);
        if (pageSize > 0) {
            typedQuery.setMaxResults(pageSize);
            typedQuery.setFirstResult(first);   
        }

        return typedQuery.getResultList();
    }
    
    public Long getCount(YearMonth start, YearMonth end, Worker worker) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<MonthlySummary> root = query.from(MonthlySummary.class);
        CriteriaQuery<Long> select = query.select(cb.count(root));

        List<Predicate> conditions = calculateConditions(cb, root, start, end, worker);

        if (!conditions.isEmpty())
            query.where(conditions.toArray(new Predicate[conditions.size()]));

        return em.createQuery(select).getSingleResult();
    }
    
    private List<Predicate> calculateConditions(CriteriaBuilder cb, Root<MonthlySummary> root, YearMonth start, YearMonth end, Worker worker) {
        List<Predicate> conditions = new ArrayList<>();
        
        if (start != null && end != null)
            conditions.add(cb.between(root.get(MonthlySummary_.month), start, end));
        
        if (worker != null)
            conditions.add(cb.equal(root.get(MonthlySummary_.worker), worker));
        
        return conditions;
    }
}
