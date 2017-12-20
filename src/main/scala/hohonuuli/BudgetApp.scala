package hohonuuli

import java.time.{Duration, Instant}

/**
  *
  *
  * @author Brian Schlining
  * @since 2017-12-17T13:20:00
  */
object BudgetApp extends App {

  val budget = CurrentBudget
  val endDate = Instant.now.plus(Duration.ofDays(365))
  def filter(s: Stream[Transaction]): Stream[Transaction] = s.takeWhile(i => i.date.isBefore(endDate))
  def money(s: Stream[Transaction]): Double = s.last.accumulatedValue

  val sum = budget.events.map(_.stream) // Convert each transaction into a stream
    .map(filter)     // Include all transactions for 1 year
    .map(money)      // Convert each transaction stream to it's accumulated value
    .sum             // Sum up all the accumulated values to get a grand total

  printf("Total: %.2f\n", sum)

}
