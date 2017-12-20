package hohonuuli

import java.time.{Duration, Instant}

import org.scalatest.{FlatSpec, Matchers}

class BudgetEventSpec extends FlatSpec with Matchers {

  "BudgetEvent" should "generate a stream" in {
    val b = OngoingEvent(3343.95, Instant.now, Duration.ofDays(14))
    println(b.stream.take(52).last.accumulatedValue)
  }
}