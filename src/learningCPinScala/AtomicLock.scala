package learningCPinScala

import java.util.concurrent.atomic.AtomicBoolean

import scala.concurrent.ExecutionContext

object AtomicLock extends App {

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
