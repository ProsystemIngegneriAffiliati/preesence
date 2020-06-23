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
package com.prosystemingegneri.preesence.business.worker.boundary;

import com.prosystemingegneri.preesence.business.worker.entity.EmploymentContract;
import com.prosystemingegneri.preesence.business.worker.entity.EmploymentContract_;
import java.io.Serializable;
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
public class EmploymentContractService implements Serializable {
    @PersistenceContext
    EntityManager em;
    
    public EmploymentContract create() {
        return new EmploymentContract();
    }
    
    public EmploymentContract save(EmploymentContract worker) {
        if (worker.getId() == null)
            em.persist(worker);
        else
            return em.merge(worker);
        
        return worker;
    }
    
    public EmploymentContract find(Long id) {
        return em.find(EmploymentContract.class, id);
    }
    
    public void delete(Long id) {
        em.remove(find(id));
    }
    
    public List<EmploymentContract> list(int first, int pageSize, String sortField, Boolean isAscending, String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<EmploymentContract> query = cb.createQuery(EmploymentContract.class);
        Root<EmploymentContract> root = query.from(EmploymentContract.class);
        CriteriaQuery<EmploymentContract> select = query.select(root).distinct(true);
        
        List<Predicate> conditions = calculateConditions(cb, root, name);

        if (!conditions.isEmpty())
            query.where(conditions.toArray(new Predicate[conditions.size()]));
        
        Order order = cb.asc(root.get(EmploymentContract_.name));
        if (isAscending != null && sortField != null && !sortField.isEmpty()) {
            Path<?> path;
            switch (sortField) {
                case "name":
                    path = root.get(EmploymentContract_.name);
                    break;
                case "hoursDaily":
                    path = root.get(EmploymentContract_.hoursDaily);
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
        
        TypedQuery<EmploymentContract> typedQuery = em.createQuery(select);
        if (pageSize > 0) {
            typedQuery.setMaxResults(pageSize);
            typedQuery.setFirstResult(first);   
        }

        return typedQuery.getResultList();
    }
    
    public Long getCount(String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<EmploymentContract> root = query.from(EmploymentContract.class);
        CriteriaQuery<Long> select = query.select(cb.count(root));

        List<Predicate> conditions = calculateConditions(cb, root, name);

        if (!conditions.isEmpty())
            query.where(conditions.toArray(new Predicate[conditions.size()]));

        return em.createQuery(select).getSingleResult();
    }
    
    private List<Predicate> calculateConditions(CriteriaBuilder cb, Root<EmploymentContract> root, String name) {
        List<Predicate> conditions = new ArrayList<>();
        
        //EmploymentContract's name
        if (name != null && !name.isEmpty())
            conditions.add(cb.like(cb.lower(root.get(EmploymentContract_.name)), "%" + name.toLowerCase() + "%"));
        
        return conditions;
    }
    
}