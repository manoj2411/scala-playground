package exercises

import scala.annotation.tailrec

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
  def ++[B >: A](anotherStream: => MyStream[B]):MyStream[B] // concat streams

  def foreach(f: A => Unit): Unit
  def map[B](f: A => B): MyStream[B]
  def flatMap[B](f: A => MyStream[B]): MyStream[B]
  def filter(predicate: A => Boolean): MyStream[A]

  def take(n: Int): MyStream[A]
  def takeAsList(n: Int): List[A] = take(n).toList()
  @tailrec
  final def toList[B >: A](acc: List[B] = Nil): List[B] =
    if (isEmpty) acc.reverse
    else tail.toList(head :: acc)
}

object EmptyStream extends MyStream[Nothing] {
  def isEmpty: Boolean = true
  def head = throw new NoSuchElementException("no head for empty stream")
  def tail = throw new NoSuchElementException("no tail for empty stream")

  def #::[B >: Nothing](element: B): MyStream[B] = new NonEmptyStream(element, this)
  def ++[B >: Nothing](anotherStream: => MyStream[B]):MyStream[B] = anotherStream // concat streams

  def foreach(f: Nothing => Unit): Unit = ()
  def map[B](f: Nothing => B): MyStream[B] = this
  def flatMap[B](f: Nothing => MyStream[B]): MyStream[B] = this
  def filter(predicate: Nothing => Boolean): MyStream[Nothing] = this

  def take(n: Int): MyStream[Nothing] = this
  // def takeAsList(n: Int): List[Nothing] = Nil // Nil– Represents an empty List of anything of zero length
}

// _tail "call by name" is required to make it lazy
class NonEmptyStream[A](_head: A, _tail: => MyStream[A]) extends MyStream[A] {
  def isEmpty: Boolean = false

  // overriding it as a val because I might need it more than once throughout the implementation
  // So I wanted to evaluate it as value so that I can then reuse it throughout the entire body.
  override val head: A = _head
  override lazy val tail: MyStream[A] = _tail // call by need

  // NonEmptyStream(_, tail) - tail in this case lazily evaluated,
  def #::[B >: A](element: B): MyStream[B] = new NonEmptyStream(element, this) //prepend
  def ++[B >: A](anotherStream: => MyStream[B]):MyStream[B] = new NonEmptyStream(head, tail ++ anotherStream) // concat streams

  // it forces the evaluation.
  def foreach(f: A => Unit): Unit = {
    f(head)
    tail.foreach(f)
  }
  def map[B](f: A => B): MyStream[B] = new NonEmptyStream(f(head), tail.map(f))
  def flatMap[B](f: A => MyStream[B]): MyStream[B] = f(head) ++ tail.flatMap(f)
  def filter(predicate: A => Boolean): MyStream[A] =
    if (predicate(head)) new NonEmptyStream(head, tail.filter(predicate))
    else tail.filter(predicate)

  def take(n: Int): MyStream[A] =
    if (n <= 0) EmptyStream
    else if (n == 1) new NonEmptyStream(head, EmptyStream)
    else new NonEmptyStream(head, tail.take(n - 1))
}

object MyStream {
  def from[A](start: A)(generator: A => A): MyStream[A] =
    new NonEmptyStream(start, MyStream.from(generator(start))(generator))
}


object StreamsPlayground extends App {
  val numbers = MyStream.from(0)(_ + 1)
  // println(numbers.head)
  // println(numbers.tail.head)
  // println(numbers.tail.tail.head)

  val startWithMinus1 = -1 #:: numbers
  // println(numbers.head)
  // println(numbers.tail.head)

  // numbers.take(10000).foreach(println)

  // map & flatMap
  // println(numbers.map(_ * 2).take(50).toList())
  // println(numbers.flatMap(x => new NonEmptyStream(x, new NonEmptyStream(x + 1, EmptyStream))).take(10).toList())
  // println(numbers.filter(_ < 10).take(10).toList())

  /* Exercise on streams
  1. stream of fibonacci numbers
  */
  def fibonacci(first: BigInt = 1, second: BigInt = 1): MyStream[BigInt] =
    new NonEmptyStream(first, fibonacci(second, first + second))
  // println(fibonacci().take(200).toList())

  /* Exercise on streams
   2. stream of primer numbers with Eratosthenes' sieve
     [2 3 4 5 6 7 ...]
     filter all divisible by 2
     [2 3 5 7 9 11 ...]
     filter all divisible by 3
     [2 5 7 11 13 17...]
     filter all divisible by 5
  */
  // eratosthenes sieve
  // 2 eratosthenes  applied to numbers filtered by n % 2 != 0
  def eratosthenes(numbers: MyStream[Int]): MyStream[Int] =
    if (numbers.isEmpty) numbers
    else new NonEmptyStream(numbers.head, eratosthenes(numbers.tail.filter(_ % numbers.head != 0)))

  val nPrimes = eratosthenes(MyStream.from(2)(_ + 1)).take(100)
  println(nPrimes.toList())
}

