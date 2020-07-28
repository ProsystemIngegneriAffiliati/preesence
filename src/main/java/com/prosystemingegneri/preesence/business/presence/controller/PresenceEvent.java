/*
 * Copyright (C) 2018-2020 Prosystem Ingegneri Affiliati
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
package com.prosystemingegneri.preesence.business.presence.controller;

/**
 *
 * @author Davide Mainardi <ingmainardi at live.com>
 */
public enum PresenceEvent {
    WORK(0),
    HOLIDAY(1),
    VACATION(2),
    REST(3),
    OFF(4),
    SICK(5),
    INJURY(6),
    FORMER_HOLIDAY(7),
    LEGGE_104(8),
    LEAVE_OF_ABSENCE(9),
    LACTATION(10),
    AVIS(11);
    
    private final int value;

    private PresenceEvent(int value) {
        this.value = value;
    }
}