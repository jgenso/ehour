package net.rrm.ehour.ui.report.panel.aggregate

import net.rrm.ehour.report.reports.ReportData
import scalaj.collection.Imports._
import nl.tecon.highcharts.HighChart
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement
import nl.tecon.highcharts.config._
import java.lang.String
import net.rrm.ehour.config.EhourConfig
import collection.Seq
import net.rrm.ehour.ui.common.session.EhourWebSession

case class ChartContext(renderToId: String, reportData: ReportData, currencySymbol: String, withTurnover: Boolean)

object AggregateReportChartGenerator {

  def generateEmployeeReportChart(chartContext: ChartContext): String =
    generateReportChart(chartContext, _.getProjectAssignment.getUser.getFullName, "Users in hours")

  def generateCustomerReportChart(chartContext: ChartContext): String =
    generateReportChart(chartContext, _.getProjectAssignment.getProject.getCustomer.getFullName, "Customers in hours")

  def generateProjectReportChart(chartContext: ChartContext): String =
    generateReportChart(chartContext, _.getProjectAssignment.getFullName, "Projects in hours")

  private def generateReportChart(chartContext: ChartContext, findCategory: (AssignmentAggregateReportElement) => String, chartTitle: String): String = {
    import nl.tecon.highcharts.config.Conversions.valueToOption

    val elements = chartContext.reportData.getReportElements.asScala.asInstanceOf[Seq[AssignmentAggregateReportElement]]

    val categoryData = extractCategoryData(elements, findCategory)

    val categories = categoryData map (_._1)
    val hourSeries = Series(name = "Booked hours", data = categoryData map (_._2), yAxis = 0)
    val legend = Labels(formatter = JavascriptFunction("function() { return this.value.toLocaleString();}"))

    // not winning a beauty contest with this..
    val series: Option[List[Series[Int]]] = Some(if (chartContext.withTurnover) {
      val turnoverSeries = Series(name = "Turnover", data = categoryData map (_._3), yAxis = 1)
      List(hourSeries, turnoverSeries)
    } else {
      List(hourSeries)
    })

    val yAxis: Option[Seq[Axis]] = Some(if (chartContext.withTurnover) {
      Seq(Axis(title = Title(text = "Hours"), opposite = true), Axis(title = Title(text = chartContext.currencySymbol), labels = legend))
    } else {
      Seq(Axis(title = Title(text = "Hours"), opposite = true))
    })


    val chartTitleText = if (chartContext.withTurnover) chartTitle + " and turnover" else chartTitle

    val height = (categories.size * 35) + 110
    val chart = Chart(defaultSeriesType = SeriesType.Bar, height = if (height < 400) 400 else height)

    HighChart(chart = chart,
      xAxis = Seq(Axis(Some(categories.toArray))),
      yAxis = yAxis,
      series = series,
      title = Title(text = chartTitleText),
      tooltip = Tooltip(shared = true, formatter = Some(JavascriptFunction("""function() { var s = '<b>'+ this.x +'</b>'; $.each(this.points, function(i, point) { s += '<br/>'+ point.series.name +': ' + point.y.toLocaleString(); }); return s; }"""))),
      plotOptions = PlotOptions(PlotOptionsSeries(shadow = false))
    ).build(chartContext.renderToId)
  }

  private def extractCategoryData(elements: Seq[AssignmentAggregateReportElement], findCategory: (AssignmentAggregateReportElement) => String): List[(String, Int, Int)] = {
    val categories = (elements map (findCategory(_))).toSet

    val aggregator = (id: String, valueOf: (AssignmentAggregateReportElement) => Int) => elements.foldLeft(0)((total, element) => if (findCategory(element) == id) total + valueOf(element) else total)

    val hourAggregator = (id: String) => aggregator(id, _.getHours.intValue())
    val turnOverAggregator = (id: String) => aggregator(id, _.getTurnOver.intValue())

    val categoryTuples = categories map (category => (category, hourAggregator(category), turnOverAggregator(category)))

    categoryTuples.toList.sortBy(_._1)
  }
}