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

  def filter(s: Stream[BudgetEvent]): Stream[BudgetEvent] = s.takeWhile(i => i.date.isBefore(endDate))
  def money(s: Stream[BudgetEvent]): Double = s.last.accumulatedValue

  val events: Seq[BudgetEvent] = Seq(
    OneTimeEvent("Savings", 85000, Instant.now()),
    OngoingEvent("B's Pay", 3343.95, Instant.now, Duration.ofDays(14)), // B pay
    OngoingEvent("K's Pay", 1579.74, Instant.now, Duration.ofDays(14)), // K pay
    MonthlyEvent("Mortgage", -2882.56, Instant.now()), // Mortgage
    MonthlyEvent("Rent", -2500, Instant.now()),  // Rent
    OngoingEvent("Claudia", -125, Instant.now(), Duration.ofDays(14)),
    MonthlyEvent("Yard Guys", -150, Instant.now())
  )

  val sum = events.map(_.stream)
      .map(filter)
      .map(money)
      .sum


  println("Total: " + sum)




}
