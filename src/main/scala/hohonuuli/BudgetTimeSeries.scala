package hohonuuli

import java.time.Instant
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

  def addToSeries(events: Seq[Transaction]): Unit = {
    events.foreach(e => {
      val t = e.date
      val v = e.value
      series.get(t) match {
        case None => series.put(t, v)
        case Some(c) => series.put(t, c + v)
      }
    })
  }

  def raw: Seq[(Instant, Double)] = series.toSeq.sortBy(_._1)

  def cumulative(): Seq[(Instant, Double)] = {
    val cs = Matlib.cumsum(series.values.toArray)
    series.keys.zip(cs).toSeq.sortBy(_._1)
  }

}
