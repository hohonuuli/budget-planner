package hohonuuli

import java.time.{Duration, Instant}

/**
  *
  *
  * @author Brian Schlining
  * @since 2017-12-17T13:20:00
  */
object BudgetApp extends App {


  val endDate = Instant.now.plus(Duration.ofDays(365))

  def filter(s: Stream[Transaction]): Stream[Transaction] = s.takeWhile(i => i.date.isBefore(endDate))
  def money(s: Stream[Transaction]): Double = s.last.accumulatedValue

  val events: Seq[Transaction] = Seq(
    OneTime("Savings", 85000, Instant.now()),
    Ongoing("B's Pay", 3343.95, Instant.now, Duration.ofDays(14)), // B pay
    Ongoing("K's Pay", 1579.74, Instant.now, Duration.ofDays(14)), // K pay
    Monthly("Mortgage", -2882.56, Instant.now()), // Mortgage
    Monthly("Rent", -2500, Instant.now()),  // Rent
    Ongoing("Claudia", -125, Instant.now(), Duration.ofDays(14)),
    Monthly("Yard Guys", -150, Instant.now())
  )

  val sum = events.map(_.stream)
      .map(filter)
      .map(money)
      .sum


  println("Total: " + sum)




}
