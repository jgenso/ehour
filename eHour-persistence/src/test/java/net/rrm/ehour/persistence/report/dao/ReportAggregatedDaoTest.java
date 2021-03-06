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

package net.rrm.ehour.persistence.report.dao;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@SuppressWarnings({ "deprecation" })
public class ReportAggregatedDaoTest extends AbstractAnnotationDaoTest
{
	@Autowired
	private ReportAggregatedDao reportAggregatedDAO;

	public ReportAggregatedDaoTest()
	{
		super("dataset-reportaggregated.xml");
	}
	
	@Test
	public void shouldGetMinMaxDateTimesheetEntry()
	{
		// ms accuracy rocks..
		Date endDate = new Date(2007 - 1900, 2 - 1, 2, 23, 59, 59);
		endDate = new Date(endDate.getTime() + 999);
		DateRange range = reportAggregatedDAO.getMinMaxDateTimesheetEntry();
		assertEquals(new Date(2006 - 1900, 10 - 1, 2), range.getDateStart());
		assertEquals(endDate, range.getDateEnd());

	}

	@Test
	public void shouldGetMinMaxDateTimesheetEntryForUser()
	{
		DateRange range = reportAggregatedDAO.getMinMaxDateTimesheetEntry(new User(1));
		Date endDate = new Date(2007 - 1900, 2 - 1, 2, 23, 59, 59);
		endDate = new Date(endDate.getTime() + 999);

		assertEquals(new Date(2006 - 1900, 10 - 1, 2), range.getDateStart());
		assertEquals(endDate, range.getDateEnd());
	}

	/**
	 * 
	 *
	 */
	@Test
	public void shouldGetCumulatedHoursPerAssignmentForUserAndDate()
	{
		DateRange dateRange = new DateRange(new Date(2006 - 1900, 10 - 1, 1), // deprecated?
																				// hmm
																				// ;)
				new Date(2007 - 1900, 10, 30));

		List<User> ids = new ArrayList<User>();
		ids.add(new User(1));

		List<AssignmentAggregateReportElement> results = reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(ids, dateRange);

		// test if collection is properly initialized
		AssignmentAggregateReportElement rep = results.get(0);
		assertEquals("eHour", rep.getProjectAssignment().getProject().getName());

		rep = results.get(0);
		assertEquals(3676.5f, rep.getTurnOver().floatValue(), 0.1);

		assertEquals(3, results.size());
	}

	/**
	 * 
	 *
	 */
	@Test
	public void shouldGetCumulatedHoursPerAssignmentForUser()
	{
		List<User> ids = new ArrayList<User>();
		ids.add(new User(1));
		ids.add(new User(2));

		List<AssignmentAggregateReportElement> results = reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(ids);

		// test if collection is properly initialized
		AssignmentAggregateReportElement rep = results.get(0);
		assertEquals("eHour", rep.getProjectAssignment().getProject().getName());

		assertEquals(38.7f, rep.getHours().floatValue(), 0.1);

		assertEquals(5, results.size());
	}

	/**
	 * 
	 *
	 */
	@Test
	public void shouldGetCumulatedHoursPerAssignmentForUserProject()
	{
		List<User> ids = new ArrayList<User>();
		ids.add(new User(1));
		List<Project> pids = new ArrayList<Project>();
		pids.add(new Project(1));

		List<AssignmentAggregateReportElement> results = reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(ids, pids);

		// test if collection is properly initialized
		AssignmentAggregateReportElement rep = results.get(0);
		assertEquals(38.7f, rep.getHours().floatValue(), 0.1);

		assertEquals(2, results.size());
	}

	/**
	 * 
	 *
	 */
	@Test
	public void shouldGetCumulatedHoursPerAssignmentForUserProjectDate()
	{
		DateRange dateRange = new DateRange(new Date(2006 - 1900, 10 - 1, 1), // deprecated?
																				// hmm
																				// ;)
				new Date(2006 - 1900, 10 - 1, 4));
		List<User> ids = new ArrayList<User>();
		ids.add(new User(1));
		List<Project> pids = new ArrayList<Project>();
		pids.add(new Project(1));
		List<AssignmentAggregateReportElement> results = reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(ids, pids, dateRange);

		// test if collection is properly initialized
		AssignmentAggregateReportElement rep = results.get(0);
		assertEquals(14f, rep.getHours().floatValue(), 0.1);

		assertEquals(2, results.size());
	}

	@Test
	public void shouldGetCumulatedHoursPerAssignment()
	{
		DateRange dateRange = new DateRange(new Date(2006 - 1900, 10 - 1, 1), // deprecated?
																				// hmm
																				// ;)
				new Date(2006 - 1900, 10 - 1, 4));

		List<AssignmentAggregateReportElement> results = reportAggregatedDAO.getCumulatedHoursPerAssignment(dateRange);

		assertEquals(3, results.size());
	}

	@Test
	public void shouldGetCumulatedHoursPerAssignmentForProjects()
	{
		DateRange dateRange = new DateRange(new Date(2006 - 1900, 10 - 1, 1), // deprecated?
																				// hmm
																				// ;)
				new Date(2006 - 1900, 10 - 1, 4));
		List<Project> pids = new ArrayList<Project>();
		pids.add(new Project(1));

		List<AssignmentAggregateReportElement> results = reportAggregatedDAO.getCumulatedHoursPerAssignmentForProjects(pids, dateRange);

		assertEquals(2, results.size());
	}

}
