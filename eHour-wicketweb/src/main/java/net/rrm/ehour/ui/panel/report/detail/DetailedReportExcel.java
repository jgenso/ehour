package net.rrm.ehour.ui.panel.report.detail;

import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.ui.panel.report.AbstractExcelReport;
import net.rrm.ehour.ui.panel.report.ReportConfig;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

public class DetailedReportExcel  extends AbstractExcelReport<FlatReportElement>
{
	private static final long serialVersionUID = 7211392869328367507L;
	
	public DetailedReportExcel()
	{
		super(ReportConfig.DETAILED_REPORT);
	}
	
	@Override
	protected IModel getExcelReportName()
	{
		return new ResourceModel("report.title.detailed");
	}

	@Override
	protected IModel getHeaderReportName()
	{
		return new ResourceModel("report.title.detailed");
	}
}
