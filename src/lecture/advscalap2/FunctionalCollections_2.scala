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

  // val randChar = new MyRandomChar()
  // println(1 to 12 map { _ => randChar(4) })

  trait MySet[A] extends (A => Boolean) {
    override def apply(v1: A): Boolean = contains(v1)
    def contains(i: A): Boolean
    def +(i: A): MySet[A]
    def ++(anotherMySet: MySet[A]): MySet[A]

    def map[B](fn: A => B): MySet[B]
    def flatMap[B](fn: A => MySet[B]): MySet[B]
    def filter(predicate: A => Boolean): MySet[A]
    def foreach(fn: A => Unit): Unit

    /* Exercise
        - removing element from set
        - intersection with another set : return common elements
        - difference with another set : remove common elements
    * */
    def -(i: A): MySet[A]
    def &(anotherSet: MySet[A]): MySet[A]
    def --(anotherSet: MySet[A]): MySet[A]

  }
  // Small exercise to implement this MySet trait.
  class EmptyMySet[A] extends MySet[A] {
    def contains(i: A): Boolean = false
    def +(i: A): MySet[A] = new MySetImpl(i, this)
    def ++(anotherMySet: MySet[A]): MySet[A] = anotherMySet

    def map[B](fn: A => B): MySet[B] = new EmptyMySet[B]
    def flatMap[B](fn: A => MySet[B]): MySet[B] = new EmptyMySet[B]
    def filter(predicate: A => Boolean): MySet[A] = this
    def foreach(fn: A => Unit): Unit = ()

    def -(i: A): MySet[A] = this // throw new RuntimeException("Cannot remove item from empty set")
    def &(anotherSet: MySet[A]): MySet[A] = this
    def --(anotherSet: MySet[A]): MySet[A] = this

  }

  class MySetImpl[A](head: A, tail: MySet[A] = new EmptyMySet[A]) extends MySet[A] {
    def contains(i: A): Boolean = i == head || tail.contains(i)
    def +(i: A): MySet[A] = if (this(i)) this else new MySetImpl(i, this)

    def ++(anotherMySet: MySet[A]): MySet[A] = tail ++ anotherMySet + head

    def map[B](fn: A => B): MySet[B] = (tail map fn) + fn(head)
    def flatMap[B](fn: A => MySet[B]): MySet[B] =  tail.flatMap(fn) ++ fn(head)
    def filter(predicate: A => Boolean): MySet[A] =
      if (predicate(head)) (tail filter predicate) + head
      else tail filter predicate

    def foreach(fn: A => Unit): Unit = {
      fn(head)
      tail foreach fn
    }

    def -(i: A): MySet[A] =
      if (head == i) tail
      else tail - i + head

    def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)

    def --(anotherSet: MySet[A]): MySet[A] = filter(x => !anotherSet(x))
  }
  // val set1 = new EmptyMySet[Int] + 1 + 2 + 1 ++ new MySetImpl[Int](3) ++ new MySetImpl[Int](2)
  // set1 flatMap(x => new MySetImpl[Int](x) + x * 10 ) filter(_ % 2 == 0) foreach println
  val set1 = new MySetImpl[Int](3) + 5 + 1
  // set1 - 5 foreach println
  val set2 = new MySetImpl[Int](2) + 3 + 4 + 5
  // set1 & set2 foreach println
  set1 -- set2 foreach println
}
