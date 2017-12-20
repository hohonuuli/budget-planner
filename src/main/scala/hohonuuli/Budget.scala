package hohonuuli

import java.time.format.DateTimeFormatter
import java.time.{Duration, Instant}

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

  implicit def stringToInstant(s: String): Instant = {
    val df = DateTimeFormatter.ofPattern("yyyy-MM-dd")

  }

  override def events: Seq[Transaction] = Seq(
    OneTime("Savings", 5000, Instant.now()),
    Ongoing("My Pay", 2000, Instant.now, Duration.ofDays(14)), // B pay
    Ongoing("Spouses Pay", 2000, Instant.now, Duration.ofDays(14)), // K pay
    Monthly("Mortgage", -2500.01, Instant.now()), // Mortgaget
    Ongoing("Insurance", -400, Instant.now(), Duration.ofDays(90)), // INsurane
    Monthly("Cell Phones", -90, Instant.now()), // adult phones
    Monthly("PG&E", -100, Instant.now()), // PG&E
    Ongoing("Gas Honda", -35, Instant.now(), Duration.ofDays(4)), // Gas B
    Ongoing("Gas Mazda", -40, Instant.now(), Duration.ofDays(4)), // Gas K
    Ongoing("Groceries", -200, Instant.now(), Duration.ofDays(7)), // Groceries
    Ongoing("Netflix", -10.99, Instant.now(), Duration.ofDays(7)), // Netflix
    Ongoing("Amazon Prime", -109, Instant.now(), Duration.ofDays(365)), // Amazon prime
    Ongoing("Eat out", -60, Instant.now(), Duration.ofDays(2)),
    Ongoing("Coffee", -5, Instant.now(), Duration.ofDays(3))
  )
}
