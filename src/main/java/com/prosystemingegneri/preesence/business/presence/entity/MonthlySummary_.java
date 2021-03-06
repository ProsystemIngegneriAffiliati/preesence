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
package com.prosystemingegneri.preesence.business.presence.entity;

import com.prosystemingegneri.preesence.business.worker.entity.Worker;
import java.time.YearMonth;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 *
 * @author Mainardi Davide <davide at mainardisoluzioni.com>
 */
@StaticMetamodel(MonthlySummary.class)
public class MonthlySummary_ {
    public static volatile SingularAttribute<MonthlySummary, Worker> worker;
    public static volatile SingularAttribute<MonthlySummary, YearMonth> month;
}
