package hohonuuli

import java.time.{Duration, Instant}

/**
  *
  *
  * @author Brian Schlining
  * @since 2017-12-17T21:28:00
  */
trait Budget {

  def events: Seq[BudgetEvent]

}

object CurrentBudget extends Budget {
  override def events: Seq[BudgetEvent] = Seq(
    OneTimeEvent(5000, Instant.now()),
    OngoingEvent("My Pay", 2000, Instant.now, Duration.ofDays(14)), // B pay
    OngoingEvent("Spouses Pay", 2000, Instant.now, Duration.ofDays(14)), // K pay
    MonthlyEvent("Mortgage", -2500.01, Instant.now()), // Mortgaget
    OngoingEvent("Insurance", -400, Instant.now(), Duration.ofDays(90)), // INsurane
    MonthlyEvent("Cell Phones", -90, Instant.now()), // adult phones
    MonthlyEvent("PG&E", -100, Instant.now()), // PG&E
    OngoingEvent("Gas Honda", -35, Instant.now(), Duration.ofDays(4)), // Gas B
    OngoingEvent("Gas Mazda", -40, Instant.now(), Duration.ofDays(4)), // Gas K
    OngoingEvent("Groceries", -200, Instant.now(), Duration.ofDays(7)), // Groceries
    OngoingEvent("Netflix", -10.99, Instant.now(), Duration.ofDays(7)), // Netflix
    OngoingEvent("Amazon Prime", -109, Instant.now(), Duration.ofDays(365)), // Amazon prime
    OngoingEvent("Eat out", -60, Instant.now(), Duration.ofDays(2)),
    OngoingEvent("Coffee", -5, Instant.now(), Duration.ofDays(3))
  )
}
