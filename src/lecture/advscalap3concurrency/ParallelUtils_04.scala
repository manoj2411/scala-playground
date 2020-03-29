package lecture.advscalap3concurrency

import java.util.concurrent.atomic.AtomicReference

import scala.collection.parallel.ForkJoinTaskSupport
import scala.concurrent.forkjoin.ForkJoinPool

object ParallelUtils_04 extends App {
  /*
    1. Parallel collections
      Seq
      Vector
      Array
      Map - Hash, Trie
      Set - Hash, Trie

    Parallel collections operates on Map-Reduce model
      - Splitter : slit elements into chunks
      - Operation
      - recombine - Combiner(Reduce step)
  **/
  val parList = List(1,2,4,5).par

  /* Problem with parallel collection : race conditions (synchronization) */
  var sum = 0
  List(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1).par.foreach(sum += _)
  println(sum) // expected 20

  /*  configuration  */
  parList.tasksupport = new ForkJoinTaskSupport(new ForkJoinPool(2))

  /*    2. Atomic Operations & References
          Atomic op - cannot be intercepted by another thread
          Java introduced atomic types that are thread safe
  */
  val atomic = new AtomicReference[Int](24)
  //  thread safe operations
  atomic.set(33)
  atomic.get()
  atomic.getAndSet(42)
  atomic.compareAndSet(36, 66)
  // => if the value is 36 then set it to 66
  atomic.updateAndGet( _ + 1)
  atomic.getAndUpdate( _ + 1)
  atomic.accumulateAndGet(10, _ + _)
  atomic.getAndAccumulate(20, _ + _)

}
