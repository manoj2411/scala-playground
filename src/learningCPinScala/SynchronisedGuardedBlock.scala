package learningCPinScala

import java.util.Calendar

object SynchronisedGuardedBlock extends App {
  val lock = new AnyRef
  var message: Option[String] = None

  val greeter = new Thread(() => {
    lock.synchronized {
      println("[greeter]")
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


  private def log(msg: String): Unit = {
    val currTime = Calendar.getInstance().getTime()
    println(s"[${currTime}]: $msg")
  }
}
