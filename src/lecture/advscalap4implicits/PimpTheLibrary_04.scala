package lecture.advscalap4implicits

import scala.annotation.tailrec

object PimpTheLibrary extends App {

  /* type enrichment === pimping
      2.isEven : how can we do it?
        Add new functionality to the existing class
        We can do it with implicits

    So enrichment allow us to decorate existing classes that we may not have access to, with additional
      methods and properties.
    We define implicit class which TAKE 1 AND ONLY 1 ARGUMENT.

    Other examples: 1 to 10, or 2.seconds etc
  */

  implicit class RichInt(value: Int) {
    def isEven : Boolean = value % 2 == 0
    def isOdd : Boolean = !isEven
  }
//  println(24.isEven, 24.isOdd)

  /* How it works

    when I type 42.even the compiler interprets that as an error, but it doesn't give up.
    It searches for all the implicit classes or the implicit conversions that can wrap a type int into
      something that contains the method isEven.
    The compiler then inserts an invisible construct call. It rewrites it to :
    24.isEven => new RichInt(24).isEven

   NOTE: complier doesn't do multiple implicit searches. Ex:
    if we a class => implicit class RicherInt(value: RichInt) { def sqrt = ??? } then
    42.sqrt DOESN'T WORK

    Exercise:
      1. Enrich String class, add following methods:
        - asInt
        - encrypt = "John" -> "Lqjo"
      2. Enrich Int class
        - times(function) = 1.times(() => ...)
        - * for List = 3 * List(1, 2) => List(1,2,1,2,1,2)
  */

  implicit class EnrichString(str: String) {
    def asInt = Integer.valueOf(str)
//    def encrypt(cypherDistance: Int) = str.map(x => (x + cypherDistance).toChar)
    def encrypt(cypherDistance: Int) = str.map(x => (x + cypherDistance).asInstanceOf[Char])
  }
//  println("24".asInt * 10)
//  println("John".encrypt(2))

  implicit class EnrichingInt(value: Int) {
    def times(f: Int => Unit) =
      for (i <- 1 to value)  f(i)

    def *[T](list: List[T]) = {
      @tailrec
      def nTimes(n: Int, acc: List[T]): List[T] =
        if (n <= 1 ) acc
        else nTimes( n - 1, acc ++ list)
      nTimes(value, list)
    }
  }

//  3.times((i) => println("I am awesome!"))
//  println(4 * List("a", "b"))

  /* Just in case, can we do? "3" / 4
      yes, with implicit conversions.
  */
  implicit def stringToInt(str: String): Int = Integer.parseInt(str)
  println("24" / 6 )
  /* Note: implicit conversion with methods are discouraged.

  Danger zone example: lets say we want to do something like
    if (n) ....
  */
  implicit def intToBool(i: Int): Boolean = i == 1

  println( if (24) "Ok" else "Something is wrong.")
  // This is discouraged because if there is a bug in implicit method conversion, its very difficult to trace.

  /* TIPS:
      - Keep type enrichment to implicit classes and type classes
      - Avoid implicit defs as much as possible.
      - package implicits clearly, bring them to scope ONLY WHEN YOU NEED, WHAT YOU NEED.
      - If you need conversions, make them as specific as possible.
  * */
}
