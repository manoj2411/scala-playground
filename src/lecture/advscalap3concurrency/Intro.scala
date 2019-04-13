package lecture.advscalap3concurrency

import java.util.concurrent.Executors

object Intro extends App {

  val runnable = new Runnable {
    override def run(): Unit = println("Inside runnable!")
  }
  val aThread = new Thread(runnable)
  // aThread.start() // Gives signal to JVM to START a JVM thread.
  // runnable.run() // BAD: doesn't do anything in parallel. call it via thread.start
  // aThread.join() // BLOCKS until aThread finishes running.

  val helloThreads = new Thread(() => (1 to 5).foreach(_ => println("hello")))
  val byeThreads = new Thread(() => (1 to 5).foreach(_ => println("Bye")))
  // helloThreads.start
  // byeThreads.start

  // Note: with JVM - Its very expensive to start and kill a thread. The solution is to reuse them.
  // ## Executors - Its a part of java.util.concurrent package.
  val pool = Executors.newFixedThreadPool(10)

  // pool.execute(() => {
  //   Thread.sleep(1000)
  //   println("after 1 sec")
  // })
  // pool.execute(() => {
  //   Thread.sleep(1000)
  //   println("almost after 1 sec")
  // })
  // pool.execute(() => {
  //   Thread.sleep(2000)
  //   println("I slept 2 secs :)")
  // })

  // pool.shutdown() // graceful shutdown
  // pool.shutdownNow() // force shutdown and raises "sleep interrupted" exception
  // println(pool.isShutdown)

  class BankAccount(var amount: Int) {
    override def toString: String = s"$amount"
  }

  def buy(account: BankAccount, thing: String, price: Int) = {
    account.amount -= price
    //println(s"I've bought $thing")
    //println(s"My acount is now $account")
  }

//  for (i <- (1 to 2000)) {
//    val account = new BankAccount(5000)
//    val thread1 = new Thread(() => buy(account, "shoes", 2000))
//    val thread2 = new Thread(() => buy(account, "tShirt", 1000))
//    thread1.start
//    thread2.start
//
//    Thread.sleep(10)
//    if (account.amount != 2000 ) println(s"Dispute: ${account}")
//  }
  // Solution #1 to race condition:
  //  use synchronised() to solve concurrency problem (option1)
  def safeBuy(account: BankAccount, thing: String, price: Int) = {
    account.synchronized {
      // with synchronized method, no 2 threads can evaluate this expression at the same time.
      account.amount -= price
    }
  }
//  println("Safe buy:")
//  for (i <- (1 to 2000)) {
//    val account = new BankAccount(5000)
//    val thread1 = new Thread(() => safeBuy(account, "shoes", 2000))
//    val thread2 = new Thread(() => safeBuy(account, "tShirt", 1000))
//    thread1.start
//    thread2.start
//
//    Thread.sleep(10)
//    if (account.amount != 2000 ) println(s"Dispute: ${account}")
//  }

  // Solution #2 to race condition:
  //  add annotation @volatile on a val or var, all reads/writes to val/var will be synchronised.
  class BankAccountSafe(@volatile var amount: Int) // Its less used since synchronised block give more control.

  /* Exercise: construct 50 inception threads
      Thread1 -> Thread2 -> ...
      println("hello form thread #2")
    in reverse order
  */

  def inceptionThreads(maxThreads: Int, currentCount: Int = 1): Thread = new Thread(() => {
    if (maxThreads > currentCount) {
      val currThread = inceptionThreads(maxThreads, currentCount + 1)
      currThread.start
      currThread.join // This is important, this holds the control until processing finishes.
    }
    println(s"Hello from thread #${currentCount}")
  })
  inceptionThreads(50).start

  Thread.sleep(2000 )
}
