package lecture.advscalap2

import scala.util.Random

object FunctionalCollections_2 extends App {
  // Set
  val aSet = Set(1,4,5)
  // set instanced are callable(they have apply()). set instance behaves like an actual function.
  //  Sets ARE Functions - sets collections is implemented as function

  /* Lets create a callable class that generates a random character from given size */
  class MyRandomChar(rand: Random = new Random()) {
    val chars = ('a' to 'z').map(_.toString)
    def apply(max: Int = 26): String = chars(rand.nextInt(max % 26))
  }

  val randChar = new MyRandomChar()
  println(1 to 12 map { _ => randChar(4) })

  trait MySet[A] extends (A => Boolean) {
    def contains(i: A): Boolean
    def +(i: A): MySet[A]
    def ++(anotherMySet: MySet[A]): MySet[A]

    def map[B](fn: A => B): MySet[B]
    def flatMap[B](fn: A => MySet[B]): MySet[B]
    def filter(predicate: A => Boolean): MySet[A]
    def foreach(fn: A => Unit): Unit
  }
  // Small exercise to implement this MySet trait.
}
