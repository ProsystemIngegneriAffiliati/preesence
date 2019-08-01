/*
 * Copyright (C) 2017-2019 Prosystem Ingegneri Affiliati
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

import com.prosystemingegneri.preesence.business.presence.entity.Presence;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author Davide Mainardi <ingmainardi@live.com>
 */
public class EndAfterStartValidator implements ConstraintValidator<EndAfterStart, Presence> {

    @Override
    public void initialize(EndAfterStart constraintAnnotation) {
    }

    @Override
    public boolean isValid(Presence presence, ConstraintValidatorContext context) {
        if (presence.getStartMorning() == null && presence.getEndMorning() == null && presence.getStartAfternoon() == null && presence.getEndAfternoon() == null)
            return false;
        
        if (presence.getStartMorning() != null && presence.getEndMorning() != null)
            return !presence.getEndMorning().isBefore(presence.getStartMorning());
        if (presence.getStartAfternoon() != null && presence.getEndAfternoon() != null)
            return !presence.getEndAfternoon().isBefore(presence.getStartAfternoon());
        
        return false;
    }
}