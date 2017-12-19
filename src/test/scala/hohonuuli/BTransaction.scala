package hohonuuli

import java.time.{Duration, Instant}

import org.scalatest.{FlatSpec, Matchers}

/**
  *
  *
  * @author Brian Schlining
  * @since 2017-12-17T13:18:00
  */
class BTransaction extends FlatSpec with Matchers {

  "Transaction" should "generate a stream" in {
    val b = Ongoing("foo", 3343.95, Instant.now, Duration.ofDays(14))
    println("Foo: " + b.stream.take(26).last.accumulatedValue)

    val k = Ongoing("bar", 1579.74, Instant.now, Duration.ofDays(14))
    println("Bar: " + k.stream.take(26).last.accumulatedValue)

  }
}
