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
        case Some(b1) => b0 #:: loop(b1)
        case None => Stream.empty[Transaction]
      }
    }
    loop(this)
  }
}

case class OneTime(label: String,
                   value: Double,
                   date: Instant) extends Transaction {
  /** Most events reoccur or repeat. This returns Some if there is another event, None if it's the last even in the sequence */
  override def next: Option[Transaction] = None

  /** The accumlated value of an event as it progresses in time */
  override def accumulatedValue: Double = value

  override def stream: Stream[Transaction] = this #:: Stream.empty[Transaction]
}

/**
  * An event that reoccurs at some period pretty much indefinitly. Good for paychecks
  * @param value
  * @param date
  * @param repeatInterval
  * @param accumulatedValue
  */
case class Ongoing(label: String, value: Double,
                   date: Instant,
                   repeatInterval: Duration,
                   accumulatedValue: Double = 0)
    extends Transaction {
      lazy val next: Option[Transaction] = {
          Some(Ongoing(label, value, date.plus(repeatInterval), repeatInterval, accumulatedValue + value))
      }
}

/**
  * A repeated event that terminates at some date. Car-payments, braces.
  * @param value
  * @param date
  * @param repeatInterval
  * @param endDate The date to terminate the repitition.  Exclusive
  * @param accumulatedValue
  */
case class LimitedTerm(label: String,
                       value: Double,
                       date: Instant,
                       repeatInterval: Duration,
                       endDate: Instant,
                       accumulatedValue: Double = 0)
    extends Transaction {
  override def next: Option[Transaction] = {
    val nextDate = date.plus(repeatInterval)
    if (nextDate.isBefore(endDate)) {
      Some(LimitedTerm(label, value, nextDate, repeatInterval, endDate, accumulatedValue + value))
    }
    else None
  }

}

/**
  * A repeated event that reoccurs a given number of times
  * @param value
  * @param date
  * @param repeatInterval
  * @param repeatCount
  * @param accumulatedValue
  */
case class Repeating(label: String,
                     value: Double,
                     date: Instant,
                     repeatInterval: Duration,
                     repeatCount: Int,
                     accumulatedValue: Double = 0) extends Transaction {
  override def next: Option[Transaction] = {
    if (repeatCount > 0) {
      Some(Repeating(label, value, date.plus(repeatInterval), repeatInterval, repeatCount - 1, accumulatedValue + value))
    }
    else None
  }
}

/**
  * An event that reoccurs on the same day of the month every month.
  * @param value
  * @param date
  * @param accumulatedValue
  */
case class Monthly(label: String, value: Double, date: Instant, accumulatedValue: Double = 0)
    extends Transaction {

  lazy val next: Option[Transaction] = {
    Some(Monthly(label, value, Monthly.nextDate(date), accumulatedValue + value))
  }

}

object Monthly {
  def nextDate(date: Instant): Instant = {
    val zdt = date.atZone(ZoneId.systemDefault())
    val dayOfMonth = zdt.get(ChronoField.DAY_OF_MONTH)
    zdt.`with`(TemporalAdjusters.firstDayOfNextMonth())
        .plus(Duration.ofDays(dayOfMonth - 1))
        .toInstant
  }
}

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