/*
 * Copyright (C) 2018-2019 Prosystem Ingegneri Affiliati
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
package com.prosystemingegneri.preesence.business.auth.control;

/**
 *
 * @author Davide Mainardi <ingmainardi at live.com>
 */
public enum Role {
    WORKER(Constants.WORKER),
    HUMAN_RESOURCES(Constants.HUMAN_RESOURCES),
    FULL_CONTROL(Constants.FULL_CONTROL);

    private Role(String roleString) {
        if (!roleString.equals(this.name()))
            throw new IllegalArgumentException();
    }

    public static class Constants {

        //I am using this class to allow using enum in @RolesAllowed
        //see https://stackoverflow.com/a/16384334
        public static final String WORKER = "WORKER";
        public static final String HUMAN_RESOURCES = "HUMAN_RESOURCES";
        public static final String FULL_CONTROL = "FULL_CONTROL";
    }
}
