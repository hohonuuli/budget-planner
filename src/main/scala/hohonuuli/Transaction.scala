package hohonuuli

import java.time.temporal.{ChronoField, TemporalAdjusters}
import java.time.{Duration, Instant, ZoneId}

/**
  * Some event that occurs during a budget timeline
  */
trait Transaction {

  /** The amount of money */
  def value: Double

  /** When this event occurs */
  def date: Instant

  /** Most events reoccur or repeat. This returns Some if there is another event, None if it's the last even in the sequence */
  def next: Option[Transaction]

  /** The accumlated value of an event as it progresses in time */
  def accumulatedValue: Double

  def label: String

  def stream: Stream[Transaction] = {
    def loop(b0: Transaction): Stream[Transaction] = {
      b0.next match {
        case Some(b) => b0 #:: loop(b)
        case None => Stream.empty[Transaction]
      }
    }
    loop(this)
  }
}
/**
  * A OneTime transaction does not reoccur in our model.
  */
case class OneTime(label: String,
                   value: Double,
                   date: Instant) extends Transaction {

  override val next: Option[Transaction] = None
  override val accumulatedValue: Double = value
  override def stream: Stream[Transaction] = this #:: Stream.empty[Transaction]
}

/**
  * Ongoing transactions occur at a regular interval. e.g. a paycheck every 14 days.
  */
case class Ongoing(label: String, value: Double,
                   date: Instant,
                   repeatInterval: Duration,
                   accumulatedValue: Double = 0)
  extends Transaction {

  lazy val next: Option[Transaction] = {
    Some(Ongoing(label, value, date.plus(repeatInterval), repeatInterval,
      accumulatedValue + value))
  }
}

object Monthly {

  /**
    * Helper function to get next the a date on the same day in the next month.
    * For example, if the date of a transaction is 2017-11-04, nextDate will
    * return 2017-12-04
    */
  def nextDate(date: Instant): Instant = {
    val zdt = date.atZone(ZoneId.systemDefault())
    val dayOfMonth = zdt.get(ChronoField.DAY_OF_MONTH)
    zdt.`with`(TemporalAdjusters.firstDayOfNextMonth())
      .plus(Duration.ofDays(dayOfMonth - 1))
      .toInstant
  }
}

/**
  * A transaction that occurs on the same day of every month ... forever!
  */
case class Monthly(label: String,
                   value: Double,
                   date: Instant,
                   accumulatedValue: Double = 0)
  extends Transaction {

  lazy val next: Option[Transaction] = {
    Some(Monthly(label, value, Monthly.nextDate(date),
      accumulatedValue + value))
  }
}

/**
  * A monthly event that does not occur forever. e.g. A car loan.
  */
case class Loan(label: String,
                value: Double,
                date: Instant,
                endDate: Instant,
                accumulatedValue: Double = 0)
  extends Transaction {

  override lazy val next: Option[Transaction] = {
    val nextDate = Monthly.nextDate(date)
    if (nextDate.isBefore(endDate)) {
      Some(Monthly(label, value, nextDate, accumulatedValue + value))
    }
    else None
  }

}