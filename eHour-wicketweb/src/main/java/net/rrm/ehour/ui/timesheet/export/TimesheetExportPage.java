/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.timesheet.export;

import net.rrm.ehour.domain.Project;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.common.border.CustomTitledGreyRoundedBorder;
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.AjaxEventListener;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.panel.calendar.CalendarAjaxEventType;
import net.rrm.ehour.ui.common.panel.calendar.CalendarPanel;
import net.rrm.ehour.ui.common.panel.contexthelp.ContextualHelpPanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.report.page.AbstractReportPage;
import net.rrm.ehour.util.DateUtil;
import org.apache.wicket.Component;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Print month page
 */
@AuthorizeInstantiation("ROLE_CONSULTANT")
public class TimesheetExportPage extends AbstractReportPage<ReportCriteria> implements AjaxEventListener {
    private static final long serialVersionUID = 1891959724639181159L;

    private static final String ID_SELECTION_FORM = "selectionForm";
    private static final String ID_FRAME = "printSelectionFrame";
    private static final String ID_BLUE_BORDER = "blueBorder";

    private Label titleLabel;

    public TimesheetExportPage() {
        this(EhourWebSession.getSession().getNavCalendar());
    }

    public TimesheetExportPage(Calendar forMonth) {
        super(new ResourceModel("printMonth.title"));

        setDefaultModel(createModelForMonth(forMonth));

        add(createCalendarPanel("sidePanel"));

        titleLabel = getTitleLabel(forMonth);
        titleLabel.setOutputMarkupId(true);

        add(new ContextualHelpPanel("contextHelp", "printMonth.help.header", "printMonth.help.body"));

        CustomTitledGreyRoundedBorder greyBorder = new CustomTitledGreyRoundedBorder(ID_FRAME, titleLabel);
        GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder(ID_BLUE_BORDER);
        greyBorder.add(blueBorder);
        add(greyBorder);

        blueBorder.add(createExportCriteriaPanel(ID_SELECTION_FORM));
    }

    private TimesheetExportCriteriaPanel createExportCriteriaPanel(String id) {
        return new TimesheetExportCriteriaPanel(id, getPageModel());
    }

    private CalendarPanel createCalendarPanel(String id) {
        return new CalendarPanel(id, getEhourWebSession().getUser().getUser(), false);
    }

    private CompoundPropertyModel<ReportCriteria> createModelForMonth(Calendar forMonth) {
        ReportCriteria reportCriteria = getReportCriteria();

        reportCriteria.getUserCriteria().setReportRange(DateUtil.getDateRangeForMonth(forMonth));

        if (reportCriteria.getUserCriteria().getProjects() == null) {
            reportCriteria.getUserCriteria().setProjects(new ArrayList<Project>());
        }

        return new CompoundPropertyModel<ReportCriteria>(reportCriteria);
    }

    @Override
    protected final boolean isReportForSingleUser() {
        return true;
    }

    public boolean ajaxEventReceived(AjaxEvent ajaxEvent) {
        if (ajaxEvent.getEventType() == CalendarAjaxEventType.MONTH_CHANGE) {
            changeMonth(ajaxEvent);
        }

        return true;
    }

    private void changeMonth(AjaxEvent ajaxEvent) {
        createModelForMonth(getEhourWebSession().getNavCalendar());

        Component originalForm = get(ID_FRAME + ":" + ID_BLUE_BORDER + ":" + ID_SELECTION_FORM);

        TimesheetExportCriteriaPanel replacementPanel = createExportCriteriaPanel(ID_SELECTION_FORM);

        originalForm.replaceWith(replacementPanel);
        ajaxEvent.getTarget().add(replacementPanel);

        Label newLabel = getTitleLabel(getEhourWebSession().getNavCalendar());
        newLabel.setOutputMarkupId(true);
        titleLabel.replaceWith(newLabel);
        titleLabel = newLabel;
        ajaxEvent.getTarget().add(newLabel);
    }

    private Label getTitleLabel(Calendar cal) {
        return new Label("title", new StringResourceModel("printMonth.header",
                this, null,
                new Object[]{new DateModel(cal, getConfig(), DateModel.DATESTYLE_MONTHONLY)}));
    }
}
