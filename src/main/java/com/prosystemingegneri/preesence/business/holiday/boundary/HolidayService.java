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
package com.prosystemingegneri.preesence.business.holiday.boundary;

import com.prosystemingegneri.preesence.business.holiday.entity.BankHoliday;
import com.prosystemingegneri.preesence.business.holiday.entity.BankHoliday_;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 *
 * @author Mainardi Davide <davide at mainardisoluzioni.com>
 */
@Stateless
public class HolidayService {

    @PersistenceContext
    EntityManager em;

    @Inject
    @ConfigProperty(name = "holiday-api-key")
    private String apikey;

    private Client client;
    private WebTarget target;

    @PostConstruct
    public void init() {
        client = ClientBuilder.newClient();
    }
    
    public BankHoliday find(LocalDate date) {
        return em.find(BankHoliday.class, date);
    }
    
    public void delete(LocalDate date) {
        em.remove(find(date));
    }
    
    public List<BankHoliday> list(int first, int pageSize, String sortField, Boolean isAscending) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BankHoliday> query = cb.createQuery(BankHoliday.class);
        Root<BankHoliday> root = query.from(BankHoliday.class);
        CriteriaQuery<BankHoliday> select = query.select(root).distinct(true);
        
        query.orderBy(cb.desc(root.get(BankHoliday_.daytime)));
        
        TypedQuery<BankHoliday> typedQuery = em.createQuery(select);
        if (pageSize > 0) {
            typedQuery.setMaxResults(pageSize);
            typedQuery.setFirstResult(first);   
        }

        return typedQuery.getResultList();
    }
    
    public Long getCount() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<BankHoliday> root = query.from(BankHoliday.class);
        CriteriaQuery<Long> select = query.select(cb.count(root));

        return em.createQuery(select).getSingleResult();
    }

    public void updateBankHolidaysCurrentYear() throws WebApplicationException {
        int year = LocalDate.now().getYear();
        for (int month = 1; month <= 12; month++) {
            updateBankHolidays(year, month);
        }
    }

    private void updateBankHolidays(int year, int month) throws WebApplicationException {
        target = client.target(
                "https://getfestivo.com/v1/holidays")
                .queryParam("api_key", apikey)
                .queryParam("country", "IT")
                .queryParam("year", year)
                .queryParam("month", month)
                .queryParam("language", "it")
                .queryParam("type", "public")
                .queryParam("public", true);
        JsonObject jsonObject = target
                    .request(MediaType.APPLICATION_JSON)
                    .get(JsonObject.class);
        JsonObject holidayObject = jsonObject.getJsonObject("holidays");
        if (!holidayObject.isEmpty()) {
            JsonArray emailsJson = holidayObject.getJsonArray("holidays");
            for (JsonObject j : emailsJson.getValuesAs(JsonObject.class)) {
                LocalDate newBankHoliday = LocalDate.parse(j.getString("date"));
                if (em.find(BankHoliday.class, newBankHoliday) == null)
                    em.persist(new BankHoliday(newBankHoliday, j.getString("name")));
            }
        }
    }

    @PreDestroy
    public void close() {
        client.close();
    }
}
