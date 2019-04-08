package lecture.advscalap3concurrency

object Intro extends App {

  val runnable = new Runnable {
    override def run(): Unit = println("Inside runnable!")
  }
  val aThread = new Thread(runnable)
  aThread.start() // Gives signal to JVM to START a JVM thread.
  runnable.run() // BAD: doesn't do anything in parallel. call it via thread.start
  aThread.join() // BLOCKS until aThread finishes running.

  val helloThreads = new Thread(() => (1 to 5).foreach(_ => println("hello")))
  val byeThreads = new Thread(() => (1 to 5).foreach(_ => println("Bye")))
  helloThreads.start
  byeThreads.start

}
