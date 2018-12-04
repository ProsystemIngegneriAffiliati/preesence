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
package com.prosystemingegneri.preesence.business.auth.control;

/**
 *
 * @author Davide Mainardi <ingmainardi at live.com>
 */
public enum Role {
    LAVORATORE(Constants.LAVORATORE),
    GESTORE_PERSONALE(Constants.GESTORE_PERSONALE),
    CONTROLLO_COMPLETO(Constants.CONTROLLO_COMPLETO);

    private Role(String roleString) {
        if (!roleString.equals(this.name()))
            throw new IllegalArgumentException();
    }

    public static class Constants {

        //I am using this class to allow using enum in @RolesAllowed
        //see https://stackoverflow.com/a/16384334
        public static final String LAVORATORE = "LAVORATORE";
        public static final String GESTORE_PERSONALE = "GESTORE_PERSONALE";
        public static final String CONTROLLO_COMPLETO = "CONTROLLO_COMPLETO";
    }
}
