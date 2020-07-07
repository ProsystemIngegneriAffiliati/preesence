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
package com.prosystemingegneri.preesence.business.presence.controller;

import com.prosystemingegneri.preesence.business.presence.controller.CompulsoryPreesenceEventWhenDifference.CompulsoryPreesenceEventWhenDifferenceValidator;
import com.prosystemingegneri.preesence.business.presence.entity.Presence;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

/**
 *
 * @author Mainardi Davide <davide@mainardisoluzioni.com>
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CompulsoryPreesenceEventWhenDifferenceValidator.class)
public @interface CompulsoryPreesenceEventWhenDifference {

    String message() default "{com.prosystemingegneri.preesence.CompulsoryPreesenceEventWhenDifference.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class CompulsoryPreesenceEventWhenDifferenceValidator implements ConstraintValidator<CompulsoryPreesenceEventWhenDifference, Presence> {

        @Override
        public void initialize(CompulsoryPreesenceEventWhenDifference constraintAnnotation) {
        }

        @Override
        public boolean isValid(Presence presence, ConstraintValidatorContext context) {
            presence.updateAllTimings();
            return !(BigDecimal.ZERO.compareTo(presence.getDifference()) < 0 && presence.getDifferenceEvent() == null);
        }
    }
}
