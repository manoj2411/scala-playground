package learningCPinScala

import java.util.Calendar

object SynchronisedGuardedBlock extends App {
  /*
    Problem: we have a message variable, we need to log it whenever a value assigned to it from diff thread.
      while true is 1 way of doing it but we should use wait and notify to use optimal resources.
  */
  val lock = new AnyRef
  var message: Option[String] = None

  val greeter = new Thread(() => {
    lock.synchronized {
      while (message == None) {
        println("[greeter]: waiting")
        lock.wait()
      }
      log(message.get)
    }
  })
  greeter.start()

  lock.synchronized {
    message = Some("Hey!")
    println("[main]")
    lock.notify()
  }

  greeter.join()


  def log(msg: String): Unit = {
    val currTime = Calendar.getInstance().getTime()
    println(s"[${currTime}]: $msg")
  }
}
