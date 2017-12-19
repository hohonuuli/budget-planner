package hohonuuli

import java.lang.{Double => JDouble}
import java.time.{Duration, Instant}
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.chart.{LineChart, NumberAxis, XYChart}
import javafx.stage.Stage

import hohonuuli.BudgetApp.endDate

import scala.collection.JavaConverters._

/**
  *
  *
  * @author Brian Schlining
  * @since 2017-12-17T21:27:00
  */
object BudgetPlot extends App with scalax.chart.module.Charting {
  val endDate = Instant.now.plus(Duration.ofDays(365))
  val budget = CurrentBudget
  def filter(s: Stream[Transaction]): Stream[Transaction] = s.takeWhile(i => i.date.isBefore(endDate))
  val ev = budget.events
      .map(_.stream)
      .map(filter)
      .map(_.toSeq)
  val ts = new BudgetTimeSeries
  ev.foreach(ts.addToSeries)

  //val data = for (i <- 1 to 5) yield (i,i)
  val data = ts.cumulative()
      .map(v => v._1.toEpochMilli -> v._2)
      .toSeq
  val chart = XYLineChart(data)
  chart.saveAsPNG("target/chart.png")
}

//class BudgetPlot extends Application {
//  override def start(primaryStage: Stage): Unit = {
//    primaryStage.setTitle("Budget")
//  }
//
//  def plot(timeSeries: TimeSeries): Scene = {
//    val xAxis = new DateAxis()
//    xAxis.setLabel("Date")
//    val yAxis = new NumberAxis()
//    yAxis.setLabel("Money")
//    val lineChart = new LineChart(xAxis, yAxis)
//    lineChart.setTitle("Budget")
//    val ts = new XYChart.Series[Long, JDouble]
//    timeSeries.cumulative()
//        .map(v => new XYChart.Data(v._1.toEpochMilli, new JDouble(v._2)))
//        .foreach(v => ts.getData.add(v))
//    lineChart.getData.add(ts)
//
//  }
//}
