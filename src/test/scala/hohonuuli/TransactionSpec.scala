package hohonuuli

import java.time.{Duration, Instant}

import org.scalatest.{FlatSpec, Matchers}

class TransactionSpec extends FlatSpec with Matchers {

  "BudgetEvent" should "generate a stream" in {
    val b = Ongoing("Foo", 1234.56, Instant.now, Duration.ofDays(14))
    println(b.stream.take(52).last.accumulatedValue)
  }
}