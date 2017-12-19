package hohonuuli

import java.time.{Duration, LocalDate, Instant, ZoneId}

/**
  *
  *
  * @author Brian Schlining
  * @since 2017-12-17T21:28:00
  */
trait Budget {

  def events: Seq[Transaction]

}

object CurrentBudget extends Budget {

  implicit def toInstant(s: String): Instant = LocalDate.parse(s)
      .atStartOfDay(ZoneId.systemDefault())
      .toInstant


  override def events: Seq[Transaction] = Seq(
    OneTime("Savings", 5000, Instant.now()),
    Ongoing("My Pay", 2000, "2017-12-14", Duration.ofDays(14)), // B pay
    Ongoing("Spouses Pay", 2000, "2017-12-14", Duration.ofDays(14)), // K pay
    Monthly("Mortgage", -2500.01, "2017-12-01"), // Mortgaget
    Ongoing("Insurance", -400, "2017-12-10", Duration.ofDays(90)), // INsurane
    Monthly("Cell Phones", -90, "2017-12-07"), // adult phones
    Monthly("PG&E", -100, "2017-12-03"), // PG&E
    Ongoing("Gas Honda", -35, "2017-12-01", Duration.ofDays(4)), // Gas B
    Ongoing("Gas Mazda", -40, "2017-12-01", Duration.ofDays(4)), // Gas K
    Ongoing("Groceries", -200, "2017-12-03", Duration.ofDays(7)), // Groceries
    Ongoing("Netflix", -10.99, "2017-12-01", Duration.ofDays(7)), // Netflix
    Ongoing("Amazon Prime", -109, "2017-10-07", Duration.ofDays(365)), // Amazon prime
    Ongoing("Eat out", -60, Instant.now(), Duration.ofDays(2)),
    Ongoing("Coffee", -5, Instant.now(), Duration.ofDays(3))
  )
}
