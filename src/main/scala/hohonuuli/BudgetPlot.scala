package hohonuuli

import java.time.{Duration, Instant}

object BudgetPlot extends App with scalax.chart.module.Charting {

  val budget = CurrentBudget
  val endDate = Instant.now.plus(Duration.ofDays(365))
  def filter(s: Stream[Transaction]): Stream[Transaction] = s.takeWhile(i => i.date.isBefore(endDate))

  val timeseries = new BudgetTimeSeries
  budget.events
    .map(_.stream)
    .map(filter)
    .map(_.toSeq)
    .foreach(timeseries.addToSeries)

  val data = timeseries.cumulative()
    .map(v => v._1.toEpochMilli -> v._2)
    .toSeq
  val chart = XYLineChart(data)
  chart.saveAsPNG("target/chart.png")

}

