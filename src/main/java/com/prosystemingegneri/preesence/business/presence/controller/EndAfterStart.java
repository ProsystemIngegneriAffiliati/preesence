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
package com.prosystemingegneri.preesence.business.presence.controller;

import com.prosystemingegneri.preesence.business.presence.controller.EndAfterStart.EndAfterStartValidator;
import com.prosystemingegneri.preesence.business.presence.entity.Presence;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

/**
 *
 * @author Davide Mainardi <ingmainardi@live.com>
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EndAfterStartValidator.class)
public @interface EndAfterStart {

    String message() default "{com.prosystemingegneri.preesence.EndAfterStart.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class EndAfterStartValidator implements ConstraintValidator<EndAfterStart, Presence> {

        @Override
        public void initialize(EndAfterStart constraintAnnotation) {
        }

        @Override
        public boolean isValid(Presence presence, ConstraintValidatorContext context) {
            if (presence.getStartMorning() != null && presence.getEndMorning() != null) {
                return !presence.getEndMorning().isBefore(presence.getStartMorning());
            }
            if (presence.getStartAfternoon() != null && presence.getEndAfternoon() != null) {
                return !presence.getEndAfternoon().isBefore(presence.getStartAfternoon());
            }

            //valid is everything is null or nothing is null, not valid otherwise
            return ! (Boolean.logicalXor(presence.getStartMorning() == null, presence.getEndMorning() == null) && Boolean.logicalXor(presence.getStartAfternoon() == null, presence.getEndAfternoon() == null));
        }
    }
}
