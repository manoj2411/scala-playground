package learningCPinScala

import scala.collection.mutable

object SynchronisedBadPool extends App {
  type Task = () => Unit
  var tasks = mutable.Queue[Task]()

  val worker = new Thread() {

    def poll(): Option[Task] = tasks.synchronized {
      if (tasks.nonEmpty) Some(tasks.dequeue())
      else None
    }

    override def run(): Unit = while (true) poll() match {
        case Some(task) => task()
        case _ =>
      }
  }
  worker.setName("worker")

  // if not set, main will not terminate. JVM process terminated when all non-daemon threads termnate
  worker.setDaemon(true)
  worker.start()

  def async(task: => Unit) = tasks.synchronized {
    tasks.enqueue(() => task)
  }

  async { log("Hey!") }
  async { log("Enjoy ") }

  /*
    Problem: after worker completes work, it still keep checking for task which make 1 cpu core busy all time.
  */

  Thread.sleep(3000)

}
