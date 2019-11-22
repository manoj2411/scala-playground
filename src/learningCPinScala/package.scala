import java.util.Calendar

import scala.concurrent.ExecutionContext

package object learningCPinScala {

  def log(msg: String): Unit = {
    val currTime = Calendar.getInstance().getTime()
    println(s"[${currTime}]: $msg")
  }

  def execute(body: => Unit) {
    ExecutionContext.global.execute(
      new Runnable { def run() = body }
    )
  }
}
