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
package com.prosystemingegneri.preesence.business.user.boundary;

import com.prosystemingegneri.preesence.business.user.entity.UserApp;
import com.prosystemingegneri.preesence.business.worker.boundary.WorkerService;
import com.prosystemingegneri.preesence.business.worker.entity.Worker;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Davide Mainardi <ingmainardi@live.com>
 */
@Stateless
public class UserService {
    @PersistenceContext
    EntityManager em;
    
    @Inject
    WorkerService workerService;
    
    public UserApp saveUserApp(UserApp userApp, boolean isCreatingWorker) {
        if (isCreatingWorker && workerService.findWorker(userApp) == null)
            workerService.createWorker(userApp);
        
        return em.merge(userApp);
    }
    
    public void createUserApp(UserApp userApp) {
        em.persist(userApp);
    }
    
    public UserApp readUserApp(String userName) {
        return em.find(UserApp.class, userName);
    }
    
    @RolesAllowed("admin")
    public void deleteUserApp(UserApp userApp) {
        em.remove(em.merge(userApp));
    }

    @RolesAllowed("admin")
    public List<UserApp> listUserApps() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UserApp> query = cb.createQuery(UserApp.class);
        Root<UserApp> root = query.from(UserApp.class);
        query.select(root);
        return em.createQuery(query).getResultList();
    }
}
