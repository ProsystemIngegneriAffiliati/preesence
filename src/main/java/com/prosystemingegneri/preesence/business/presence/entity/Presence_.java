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
package com.prosystemingegneri.preesence.business.presence.entity;

import com.prosystemingegneri.preesence.business.presence.controller.PresenceEvent;
import com.prosystemingegneri.preesence.business.worker.entity.Worker;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 *
 * @author Davide Mainardi <ingmainardi@live.com>
 */
@StaticMetamodel(Presence.class)
public class Presence_ {
    public static volatile SingularAttribute<Presence, Worker> worker;
    public static volatile SingularAttribute<Presence, LocalDate> daytime;
    public static volatile SingularAttribute<Presence, LocalTime> startMorning;
    public static volatile SingularAttribute<Presence, LocalTime> endMorning;
    public static volatile SingularAttribute<Presence, LocalTime> startAfternoon;
    public static volatile SingularAttribute<Presence, LocalTime> endAfternoon;
    public static volatile SingularAttribute<Presence, PresenceEvent> event;
    public static volatile SingularAttribute<Presence, PresenceEvent> differenceEvent;
}
