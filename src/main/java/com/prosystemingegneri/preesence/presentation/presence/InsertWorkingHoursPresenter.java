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
package com.prosystemingegneri.preesence.presentation.presence;

import com.prosystemingegneri.preesence.business.presence.boundary.PresenceService;
import com.prosystemingegneri.preesence.business.presence.controller.PresenceEvent;
import com.prosystemingegneri.preesence.business.presence.entity.Presence;
import com.prosystemingegneri.preesence.business.worker.entity.Worker;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

/**
 *
 * @author Davide Mainardi <ingmainardi at live.com>
 */
@Named
@ViewScoped
public class InsertWorkingHoursPresenter implements Serializable{
    private Worker worker;
    private String yearMonth;
    private LocalDate start;
    private LocalDate end;
    
    @Inject
    private FacesContext facesContext;
    
    private List<Presence> presences;
    
    @Inject
    private PresenceService service;
    
    @PostConstruct
    public void init() {
        presences = new ArrayList<>();
    }
    
    public String reload() {
        return facesContext.getViewRoot().getViewId() + "?faces-redirect=true";
    }
    
    public String save() {
        boolean isSaved = false;
        for (Presence presence : presences) {
            if (
                    (presence.getEvent() == PresenceEvent.WORK && presence.getStartMorning() != null || presence.getEndMorning() != null || presence.getStartAfternoon() != null || presence.getEndAfternoon() != null)
                    ||
                    presence.getEvent() != PresenceEvent.WORK
                    ) {
                service.save(presence);
                isSaved = true;
            }
        }
        if (isSaved)
            Messages.create("success").detail("saved").flash().add();
        else
            Messages.create("warning").warn().detail("presence.insertWorkingHours.warning.noPresencesSaved").flash().add();
        
        return reload();
    }
    
    public void onYearMonthUpdate() {
        if (yearMonth != null && !yearMonth.isEmpty()) {
            try {
                start = LocalDate.of(
                        Integer.parseInt(yearMonth.substring(0, 4)),
                        Integer.parseInt(yearMonth.substring(5)),
                        1
                );  //start of month
                end = start.plusMonths(1).minusDays(1); //end of month
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                Messages.create("warning").warn().detail("presence.insertWorkingHours.warning.yearMonth").add();
            }
        }
    }
    
    public void populateDays() {
        presences = service.populateDays(worker, start, end);
    }
    
    public void postProcessExportWorkingHours(Object document) {
        XSSFWorkbook wb = (XSSFWorkbook) document;
        XSSFSheet sheet = wb.getSheetAt(0);
        sheet.shiftRows(0, 0, 1);
        sheet.createRow(0).createCell(0).setCellValue(worker.getName());
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public List<Presence> getPresences() {
        return presences;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }
    
}
