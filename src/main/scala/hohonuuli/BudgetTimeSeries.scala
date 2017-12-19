package hohonuuli

import java.time.Instant
import java.util.{Map => JMap}

import scala.collection.JavaConverters._
import scala.collection.mutable
import scilube.Matlib

/**
  * Build a series
  *
  * @author Brian Schlining
  * @since 2017-12-17T21:43:00
  */
class BudgetTimeSeries {

  private[this] val series = new mutable.TreeMap[Instant, Double]

  def addToSeries(events: Seq[BudgetEvent]): Unit = {
    events.foreach(e => {
      val t = e.date
      val v = e.value
      series.get(t) match {
        case None => series.put(t, v)
        case Some(c) => series.put(t, c + v)
      }
    })
  }

  def raw: Map[Instant, Double] = series.toMap

  def cumulative(): Map[Instant, Double] = {
    val cs = Matlib.cumsum(series.values.toArray)
    series.keys.zip(cs).toMap
  }

  def jCumulative: JMap[Instant, Double] = cumulative().asJava

}
