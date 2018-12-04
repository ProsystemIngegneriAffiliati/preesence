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
package com.prosystemingegneri.preesence.presentation;

import java.util.Iterator;
import javax.ejb.EJBException;
import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.transaction.RollbackException;
import org.omnifaces.util.Messages;

/**
 *
 * @author Davide Mainardi <ingmainardi at live.com>
 */
public class EJBExceptionHandler extends ExceptionHandlerWrapper {

    public EJBExceptionHandler(ExceptionHandler wrapped) {
        super(wrapped);
    }

    @Override
    public void handle() {
        handleEJBException(FacesContext.getCurrentInstance());
        getWrapped().handle();
    }

    protected void handleEJBException(FacesContext context) {
        Iterator<ExceptionQueuedEvent> unhandledExceptionQueuedEvents = getUnhandledExceptionQueuedEvents().iterator();
        if (context == null || !unhandledExceptionQueuedEvents.hasNext()) {
            return;
        }
        Throwable exception = unhandledExceptionQueuedEvents.next().getContext().getException();
        while (exception.getCause() != null && (exception instanceof FacesException || exception instanceof ELException)) {
            exception = exception.getCause();
        }
        if (!(exception instanceof EJBException)) {
            return;
        }
        if (exception.getCause() instanceof RollbackException) {
            exception = exception.getCause().getCause().getCause();
        }
        context.addMessage(null,
                Messages.create("error")
                        .fatal()
                        .detail(exception.getCause().getLocalizedMessage())
                        .get());
        context.validationFailed();
        context.getPartialViewContext().getRenderIds().add("globalMessages");
        unhandledExceptionQueuedEvents.remove();
        while (unhandledExceptionQueuedEvents.hasNext()) {
            unhandledExceptionQueuedEvents.next();
            unhandledExceptionQueuedEvents.remove();
        }
    }

    public static class Factory extends ExceptionHandlerFactory {

        public Factory(ExceptionHandlerFactory wrapped) {
            super(wrapped);
        }

        @Override
        public ExceptionHandler getExceptionHandler() {
            return new EJBExceptionHandler(getWrapped().getExceptionHandler());
        }
    }
}
