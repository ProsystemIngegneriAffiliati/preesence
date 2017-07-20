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
package com.prosystemingegneri.preesence.business.worker.boundary;

import com.prosystemingegneri.preesence.business.worker.entity.Worker;
import com.prosystemingegneri.preesence.business.worker.entity.Worker_;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
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
public class WorkerService implements Serializable {
    @PersistenceContext
    EntityManager em;
    
    public Worker saveWorker(Worker worker) {
        if (worker.getId() == null)
            em.persist(worker);
        else
            return em.merge(worker);
        
        return null;
    }
    
    public Worker readWorker(Long id) {
        return em.find(Worker.class, id);
    }
    
    public void deleteWorker(Long id) {
        em.remove(readWorker(id));
    }
    
    public List<Worker> listWorkers() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Worker> query = cb.createQuery(Worker.class);
        Root<Worker> root = query.from(Worker.class);
        CriteriaQuery<Worker> select = query.select(root).distinct(true);
        
        query.orderBy(cb.asc(root.get(Worker_.name)));

        return em.createQuery(select).getResultList();
    }
    
    public Worker findWorker(String name, boolean isCaseSensitive) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Worker> query = cb.createQuery(Worker.class);
        Root<Worker> root = query.from(Worker.class);
        CriteriaQuery<Worker> select = query.select(root).distinct(true);
        
        List<Predicate> conditions = new ArrayList<>();
        if (name != null && !name.isEmpty()) {
            if (isCaseSensitive)
                conditions.add(cb.equal(root.get(Worker_.name), name));
            else
                conditions.add(cb.equal(cb.lower(root.get(Worker_.name)), name.toLowerCase()));
        }
        
        if (!conditions.isEmpty()) {
            query.where(conditions.toArray(new Predicate[conditions.size()]));
        }
        
        try {
            return em.createQuery(select).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException ex) {
            return em.createQuery(select).getResultList().get(0);
        }
    }
}