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
package com.prosystemingegneri.preesence.business.auth.boundary;

import com.prosystemingegneri.preesence.business.auth.control.ChangingPasswordResult;
import com.prosystemingegneri.preesence.business.auth.entity.GroupApp;
import com.prosystemingegneri.preesence.business.auth.entity.GroupApp_;
import com.prosystemingegneri.preesence.business.auth.entity.UserApp;
import com.prosystemingegneri.preesence.business.auth.entity.UserApp_;
import com.prosystemingegneri.preesence.business.worker.entity.Worker;
import com.prosystemingegneri.preesence.business.worker.entity.Worker_;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import javax.persistence.criteria.Subquery;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.credential.Password;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;

/**
 *
 * @author Davide Mainardi <ingmainardi at live.com>
 */
@Stateless
public class UserAppService implements Serializable {
    @PersistenceContext
    EntityManager em;
    
    @Inject
    private Pbkdf2PasswordHash passwordHash;
    
    @Inject
    private SecurityContext securityContext;
    
    public UserApp find(String id) {
        return em.find(UserApp.class, id);
    }
    
    public void delete(String id) {
        em.remove(find(id));
    }
    
    public boolean isUsernamePresent(String username) {
        return find(username) != null;
    }
    
    public Optional<UserApp> getLoggedUser() {
        if (securityContext.getCallerPrincipal() != null) {
            UserApp user = find(securityContext.getCallerPrincipal().getName());
            if (user != null)
                return Optional.of(user);
        }
        
        return Optional.empty();
    }
    
    public UserApp create(String username, Password password) {
        GroupApp userGroup = em.find(GroupApp.class, "onesitereadonly");
        if (userGroup != null) {
            UserApp userApp = new UserApp();
            userApp.setUsername(username);
            userApp.setGroupApp(userGroup);
            
            initializePasswordHash();
            
            userApp.setPassword(passwordHash.generate(password.getValue()));

            em.persist(userApp);
            
            return userApp;
        }
        return null;
    }
    
    public ChangingPasswordResult changePassword(String username, Password currentPassword, Password newPassword) {
        initializePasswordHash();
        UserApp user = find(username);
        if (user != null) {
            if (passwordHash.verify(currentPassword.getValue(), user.getPassword())) {
                user.setPassword(passwordHash.generate(newPassword.getValue()));
                update(user);
                return ChangingPasswordResult.SUCCESS;
            }
            return ChangingPasswordResult.WRONG_PASSWORD;
        }
        
        return ChangingPasswordResult.USER_NOT_FOUND;
    }
    
    private void initializePasswordHash() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("Pbkdf2PasswordHash.Iterations", "3072");
        parameters.put("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA512");
        parameters.put("Pbkdf2PasswordHash.SaltSizeBytes", "64");
        passwordHash.initialize(parameters);
    }
    
    public UserApp update(UserApp userApp) {
        return em.merge(userApp);
    }
    
    public List<UserApp> list(int first, int pageSize, String sortField, Boolean isAscending, String username, String groupAppName, Boolean isAssociatedToPersona) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UserApp> query = cb.createQuery(UserApp.class);
        Root<UserApp> root = query.from(UserApp.class);
        CriteriaQuery<UserApp> select = query.select(root).distinct(true);
        
        List<Predicate> conditions = calculateConditions(cb, query.subquery(UserApp.class), root, username, groupAppName, isAssociatedToPersona);

        if (!conditions.isEmpty())
            query.where(conditions.toArray(new Predicate[conditions.size()]));
        
        Order order = cb.asc(root.get(UserApp_.username));
        if (isAscending != null && sortField != null && !sortField.isEmpty()) {
            Path<?> path;
            switch (sortField) {
                case "username":
                    path = root.get(UserApp_.username);
                    break;
                case "groupAppName":
                    path = root.get(UserApp_.groupApp).get(GroupApp_.name);
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
        
        TypedQuery<UserApp> typedQuery = em.createQuery(select);
        if (pageSize > 0) {
            typedQuery.setMaxResults(pageSize);
            typedQuery.setFirstResult(first);
        }

        return typedQuery.getResultList();
    }
    
    public Long getCount(String username, String groupAppName, Boolean isAssociatedToPersona) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<UserApp> root = query.from(UserApp.class);
        CriteriaQuery<Long> select = query.select(cb.count(root));

        List<Predicate> conditions = calculateConditions(cb, query.subquery(UserApp.class), root, username, groupAppName, isAssociatedToPersona);

        if (!conditions.isEmpty())
            query.where(conditions.toArray(new Predicate[conditions.size()]));

        return em.createQuery(select).getSingleResult();
    }
    
    private List<Predicate> calculateConditions(CriteriaBuilder cb, Subquery<UserApp> subquery, Root<UserApp> root, String username, String groupAppName, Boolean isAssociatedToWorker) {
        List<Predicate> conditions = new ArrayList<>();

        //username
        if (username != null && !username.isEmpty())
            conditions.add(cb.like(cb.lower(root.get(UserApp_.username)), "%" + username.toLowerCase() + "%"));
        
        //groupApp name
        if (groupAppName != null && !groupAppName.isEmpty())
            conditions.add(cb.like(cb.lower(root.join(UserApp_.groupApp).get(GroupApp_.name)), "%" + groupAppName.toLowerCase() + "%"));
        
        //is user is associated to worker or not
        if (isAssociatedToWorker != null) {
            Path<Object> path = root.get(UserApp_.username.getName()); // field to map with sub-query
            Root<Worker> subRoot = subquery.from(Worker.class);
            subquery.select(subRoot.get(Worker_.userApp.getName()).get(UserApp_.username.getName())); // field to map with main-query

            if (isAssociatedToWorker)
                conditions.add(cb.in(path).value(subquery));
            else
                conditions.add(cb.not(cb.in(path).value(subquery)));
        }
        
        return conditions;
    }
}

