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

  // Helper function. Creates an Instant from dates like "2017-12-19"
  implicit def toInstant(s: String): Instant = LocalDate.parse(s)
      .atStartOfDay(ZoneId.systemDefault())
      .toInstant

  override def events: Seq[Transaction] = Seq(
    OneTime("Savings", 15000, "2017-12-14"),
    Ongoing("My Pay", 2000, "2017-12-14", Duration.ofDays(14)),
    Ongoing("Spouses Pay", 2000, "2017-12-14", Duration.ofDays(14)),
    Monthly("Mortgage", -2500.01, "2018-01-01"),
    Loan("Auto Loan", 350.01, "2017-12-01", "2020-12-02"),
    Ongoing("Insurance", -400, "2017-12-10", Duration.ofDays(90)),
    Monthly("Cell Phones", -90, "2017-12-07"),
    Monthly("Gas and Electric", -100, "2017-12-03"),
    Ongoing("Gas Honda", -35, "2017-12-01", Duration.ofDays(4)),
    Ongoing("Gas Mazda", -40, "2017-12-01", Duration.ofDays(4)),
    Ongoing("Groceries", -200, "2017-12-03", Duration.ofDays(7)),
    Ongoing("Netflix", -10.99, "2017-12-01", Duration.ofDays(7)),
    Ongoing("Amazon Prime", -109, "2017-10-07", Duration.ofDays(365)),
    Ongoing("Eat out", -60, "2017-12-14", Duration.ofDays(2)),
    Ongoing("Coffee", -5, "2017-12-14", Duration.ofDays(3))
  )
}
