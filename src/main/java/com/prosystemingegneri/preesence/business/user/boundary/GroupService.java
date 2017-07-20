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

import com.prosystemingegneri.preesence.business.user.entity.GroupApp;
import java.util.List;
import javax.ejb.Stateless;
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
public class GroupService {
    @PersistenceContext
    EntityManager em;
    
    public GroupApp readGroupApp(String groupName) {
        return em.find(GroupApp.class, groupName);
    }
    
    public List<GroupApp> listGroupApps() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<GroupApp> query = cb.createQuery(GroupApp.class);
        Root<GroupApp> root = query.from(GroupApp.class);
        query.select(root);
        return em.createQuery(query).getResultList();
    }
}
