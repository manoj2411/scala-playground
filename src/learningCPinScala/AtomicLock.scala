package learningCPinScala

import java.util.concurrent.atomic.AtomicBoolean

import scala.concurrent.ExecutionContext
import SynchronisedGuardedBlock.log

object AtomicLock extends App {
  import Commons._

  private val lock = new AtomicBoolean(false)

  def mySynchroised(body : => Unit ) {
    // This is dangerous, much worse than `synchronized`. This is an example of implicit lock
    while (!lock.compareAndSet(false, true)) {}
    try body finally lock.set(false)
  }

  var count = 0

  for (i <- - 0 until 10) execute { mySynchroised { count += 1} }

  Thread.sleep(1000)

  log(s"count is : $count")
}

object Commons {
  def execute(body: => Unit) {
    ExecutionContext.global.execute(
      new Runnable { def run() = body }
    )
  }
}