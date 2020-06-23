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
package com.prosystemingegneri.preesence.business.worker.boundary;

import com.prosystemingegneri.preesence.business.auth.boundary.UserAppService;
import com.prosystemingegneri.preesence.business.auth.entity.UserApp;
import com.prosystemingegneri.preesence.business.auth.entity.UserApp_;
import com.prosystemingegneri.preesence.business.worker.entity.Worker;
import com.prosystemingegneri.preesence.business.worker.entity.Worker_;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
public class WorkerService implements Serializable {
    @PersistenceContext
    EntityManager em;
    
    @Inject
    private UserAppService userAppService;
    
    public Worker create() {
        return new Worker();
    }
    
    public Worker save(Worker worker) {
        if (worker.getId() == null)
            em.persist(worker);
        else
            return em.merge(worker);
        
        return worker;
    }
    
    public Worker find(Long id) {
        return em.find(Worker.class, id);
    }
    
    public void delete(Long id) {
        em.remove(find(id));
    }
    
    public List<Worker> list(int first, int pageSize, String sortField, Boolean isAscending, String name, Boolean isDismissed) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Worker> query = cb.createQuery(Worker.class);
        Root<Worker> root = query.from(Worker.class);
        CriteriaQuery<Worker> select = query.select(root).distinct(true);
        
        List<Predicate> conditions = calculateConditions(cb, root, name, isDismissed);

        if (!conditions.isEmpty())
            query.where(conditions.toArray(new Predicate[conditions.size()]));
        
        Order order = cb.asc(root.get(Worker_.name));
        if (isAscending != null && sortField != null && !sortField.isEmpty()) {
            Path<?> path;
            switch (sortField) {
                case "name":
                    path = root.get(Worker_.name);
                    break;
                case "isDismissed":
                    path = root.get(Worker_.dismission);
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
        
        TypedQuery<Worker> typedQuery = em.createQuery(select);
        if (pageSize > 0) {
            typedQuery.setMaxResults(pageSize);
            typedQuery.setFirstResult(first);   
        }

        return typedQuery.getResultList();
    }
    
    public Long getCount(String name, Boolean isDismissed) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Worker> root = query.from(Worker.class);
        CriteriaQuery<Long> select = query.select(cb.count(root));

        List<Predicate> conditions = calculateConditions(cb, root, name, isDismissed);

        if (!conditions.isEmpty())
            query.where(conditions.toArray(new Predicate[conditions.size()]));

        return em.createQuery(select).getSingleResult();
    }
    
    private List<Predicate> calculateConditions(CriteriaBuilder cb, Root<Worker> root, String name, Boolean isDismissed) {
        List<Predicate> conditions = new ArrayList<>();
        
        //Worker's name
        if (name != null && !name.isEmpty())
            conditions.add(cb.like(cb.lower(root.get(Worker_.name)), "%" + name.toLowerCase() + "%"));
        
        //If worker has been dismissed
        if (isDismissed != null) {
            if (isDismissed)
                conditions.add(cb.isNotNull(root.get(Worker_.dismission)));
            else
                conditions.add(cb.isNull(root.get(Worker_.dismission)));
        }
        
        return conditions;
    }
    
    public Optional<Worker> find(String userAppUsername, UserApp userApp) {
        if ((userAppUsername != null && !userAppUsername.isEmpty()) || userApp != null) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Worker> query = cb.createQuery(Worker.class);
            Root<Worker> root = query.from(Worker.class);

            List<Predicate> conditions = new ArrayList<>();
            
            if (userAppUsername != null && !userAppUsername.isEmpty())
                conditions.add(cb.like(root.join(Worker_.userApp).get(UserApp_.username), userAppUsername));
            
            if (userApp != null)
                conditions.add(cb.equal(root.get(Worker_.userApp), userApp));

            if (!conditions.isEmpty())
                query.where(conditions.toArray(new Predicate[conditions.size()]));

            Worker result;
            try {
                result = em.createQuery(query).getSingleResult();
                if (result == null)
                    return Optional.empty();
            } catch (NoResultException e) {
                return Optional.empty();
            }

            return Optional.of(result);
        }
        
        return Optional.empty();
    }
    
    public Optional<Worker> getLoggedWorker() {
        if (userAppService.getLoggedUser().isPresent())
            return findByUserapp(null, userAppService.getLoggedUser().get());
        else
            return Optional.empty();
    }
    
    private Optional<Worker> findByUserapp(String userAppUsername, UserApp userApp) {
        if ((userAppUsername != null && !userAppUsername.isEmpty()) || userApp != null) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Worker> query = cb.createQuery(Worker.class);
            Root<Worker> root = query.from(Worker.class);

            List<Predicate> conditions = new ArrayList<>();
            
            if (userAppUsername != null && !userAppUsername.isEmpty())
                conditions.add(cb.like(root.join(Worker_.userApp).get(UserApp_.username), userAppUsername));
            
            if (userApp != null)
                conditions.add(cb.equal(root.get(Worker_.userApp), userApp));

            if (!conditions.isEmpty())
                query.where(conditions.toArray(new Predicate[conditions.size()]));

            Worker result;
            try {
                result = em.createQuery(query).getSingleResult();
                if (result == null)
                    return Optional.empty();
            } catch (NoResultException e) {
                return Optional.empty();
            }

            return Optional.of(result);
        }
        
        return Optional.empty();
    }
}