package hohonuuli

import java.time.{Duration, Instant}

import org.scalatest.{FlatSpec, Matchers}

/**
  *
  *
  * @author Brian Schlining
  * @since 2017-12-17T13:18:00
  */
class BTransactionSpec extends FlatSpec with Matchers {

  "BudgetEvent" should "generate a stream" in {
    val b = OngoingEvent(3343.95, Instant.now, Duration.ofDays(14))
    println("Brian: " + b.stream.take(26).last.accumulatedValue)

    val k = OngoingEvent(1579.74, Instant.now, Duration.ofDays(14))
    println("Kyra: " + k.stream.take(26).last.accumulatedValue)

  }
}
