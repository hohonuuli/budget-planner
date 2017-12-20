package hohonuuli

import java.time.temporal.{ChronoField, TemporalAdjusters}
import java.time.{Duration, Instant, ZoneId}

/**
  * Some event that occurs during a budget timeline
  */
trait BudgetEvent {

  /** The amount of money */
  def value: Double

  /** When this event occurs */
  def date: Instant

  /** Most events reoccur or repeat. This returns Some if there is another event, None if it's the last even in the sequence */
  def next: Option[BudgetEvent]

  /** The accumlated value of an event as it progresses in time */
  def accumulatedValue: Double

  def label: String

  def stream: Stream[BudgetEvent] = {
    def loop(b0: BudgetEvent): Stream[BudgetEvent] = {
      b0.next match {
        case Some(b1) => b0 #:: loop(b1)
        case None => Stream.empty[BudgetEvent]
      }
    }
    loop(this)
  }
}

case class OneTimeEvent(label: String, value: Double, date: Instant) extends BudgetEvent {
  /** Most events reoccur or repeat. This returns Some if there is another event, None if it's the last even in the sequence */
  override def next: Option[BudgetEvent] = None

  /** The accumlated value of an event as it progresses in time */
  override def accumulatedValue: Double = value

  override def stream: Stream[BudgetEvent] = this #:: Stream.empty[BudgetEvent]
}

/**
  * An event that reoccurs at some period pretty much indefinitly. Good for paychecks
  * @param value
  * @param date
  * @param repeatInterval
  * @param accumulatedValue
  */
case class OngoingEvent(label: String, value: Double,
                        date: Instant,
                        repeatInterval: Duration,
                        accumulatedValue: Double = 0)
    extends BudgetEvent {
      lazy val next: Option[BudgetEvent] = {
          Some(OngoingEvent(label, value, date.plus(repeatInterval), repeatInterval, accumulatedValue + value))
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
case class LimitedEvent(label: String,
                        value: Double,
                        date: Instant,
                        repeatInterval: Duration,
                        endDate: Instant,
                        accumulatedValue: Double = 0)
    extends BudgetEvent {
  override def next: Option[BudgetEvent] = {
    val nextDate = date.plus(repeatInterval)
    if (nextDate.isBefore(endDate)) {
      Some(LimitedEvent(label, value, nextDate, repeatInterval, endDate, accumulatedValue + value))
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
case class RepeatedEvent(label: String,
                            value: Double,
                         date: Instant,
                         repeatInterval: Duration,
                         repeatCount: Int,
                         accumulatedValue: Double = 0) extends BudgetEvent {
  override def next: Option[BudgetEvent] = {
    if (repeatCount > 0) {
      Some(RepeatedEvent(label, value, date.plus(repeatInterval), repeatInterval, repeatCount - 1, accumulatedValue + value))
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
case class MonthlyEvent(label: String, value: Double, date: Instant, accumulatedValue: Double = 0)
    extends BudgetEvent {
      
      lazy val next: Option[BudgetEvent] = {
        val zdt = date.atZone(ZoneId.systemDefault())
        val dayOfMonth = zdt.get(ChronoField.DAY_OF_MONTH)
        val newDate = zdt.`with`(TemporalAdjusters.firstDayOfNextMonth())
          .plus(Duration.ofDays(dayOfMonth - 1))
          .toInstant
        Some(MonthlyEvent(label, value, newDate, accumulatedValue + value))
      }
    
}