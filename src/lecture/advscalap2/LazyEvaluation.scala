package lecture.advscalap2

object LazyEvaluation extends App {
  // lazy value are evaluated once but only when they are used for the 1st time.
  // Its DELAYS the evaluation of values
  lazy val exc: Int = throw new RuntimeException

  // Examples of implications:
  // Side effects
  def sideEffectCondn: Boolean = {
    println("Bool")
    true
  }
  def simpleCondn = false

  // Be very careful if you use lazy values and have side effects in your program.
  lazy val lazyCondn = sideEffectCondn
  println(if (simpleCondn && lazyCondn) "Yes" else "No") // It'll not print "Bool"

  // in conjunction with call by name. Common bug
  def byNameMethod(n: => Int): Int = {
    // n + n + n // It'll do long processing 3 times

    lazy val tmp = n // CALL BY NEED
    tmp + tmp + tmp
  }
  def retrieveProcessing: Int = {
    // side effect OR long processing
    println("long processing")
    Thread.sleep(1000)
    24
  }
  println(byNameMethod(retrieveProcessing))

  def lessThan30(n: Int): Boolean = {
    println(s"$n is less than 30?")
    n < 30
  }
  def greaterThan20(n: Int): Boolean = {
    println(s"$n is greater than 20?")
    n > 20
  }
  // Normal filtering
  val numbers = List(1, 24, 40, 5, 27)
  val lt30 = numbers.filter(lessThan30)
  val gt20 = lt30.filter(greaterThan20)
  println(gt20)
  println()

  // lazy filtering
  val lazyLt30 = numbers.withFilter(lessThan30) // lazy vals under the hood
  val lazyGt20 = lazyLt30.withFilter(greaterThan20)
  println(lazyGt20) // it'll not trigger evaluation
  lazyGt20.foreach(println) // it evaluate on need basis.

  // for-comprehensions use withFilter with guards
  for {
    a <- List(1,2,3) if (a % 2 == 0) // use lazy vals
  } yield a + 1 // same as =>
  List(1,2,3).withFilter(_ % 2 == 0).map(_ + 1)// List[Int]

  /* Exercise: implement a lazy evaluated, singly linked STREAM of elements.
      naturals = MyStream.from(1)(x => x + 1) - stream of natural numbers, potentially infinite
      naturals.take(100) - lazily evaluated stream of the first 100 naturals, finite stream
      naturals.take(100).foreach(println) - print 100 naturals
      naturals.foreach(println) - will crash, infinite!
      naturals.map(_ * 2) // stream of all even numbers, potentially infinite
  */
  abstract class MyStream[+A] {
    def isEmpty: Boolean
    def head: A
    def tail: MyStream[A]

    def #::[B >: A](element: B): MyStream[B] //prepend
    def ++[B >: A](anotherSteram: MyStream[B]):MyStream[B] // concat streams

    def foreach(f: A => Unit): Unit
    def map[B](f: A => B): MyStream[B]
    def flatMap[B](f: A => MyStream[B]): MyStream[B]
    def filter(predicate: A => Boolean): MyStream[A]

    def take(n: Int): MyStream[A]
    def takeAsList(n: Int): List[A]
  }

  object MyStream {
    def from[A](start: A)(generator: A => A): MyStream[A] = ???
  }
}
