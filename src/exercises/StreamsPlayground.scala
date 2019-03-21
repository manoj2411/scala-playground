package exercises

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
  def ++[B >: A](anotherStream: MyStream[B]):MyStream[B] // concat streams

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

object EmptyStream extends MyStream[Nothing] {
  def isEmpty: Boolean = true
  def head = throw new NoSuchElementException("no head for empty stream")
  def tail = throw new NoSuchElementException("no tail for empty stream")

  def #::[B >: Nothing](element: B): MyStream[B] = new NonEmptyStream(element, this)
  def ++[B >: Nothing](anotherStream: MyStream[B]):MyStream[B] = anotherStream // concat streams

  def foreach(f: Nothing => Unit): Unit = ()
  def map[B](f: Nothing => B): MyStream[B] = this
  def flatMap[B](f: Nothing => MyStream[B]): MyStream[B] = this
  def filter(predicate: Nothing => Boolean): MyStream[Nothing] = this

  def take(n: Int): MyStream[Nothing] = this
  def takeAsList(n: Int): List[Nothing] = Nil // Nil– Represents an emptry List of anything of zero length
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
  def ++[B >: A](anotherStream: MyStream[B]):MyStream[B] = new NonEmptyStream(head, tail ++ anotherStream) // concat streams

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
//    else if (n == 1) new NonEmptyStream(head, EmptyStream)
    else new NonEmptyStream(head, tail.take(n - 1))

  def takeAsList(n: Int): List[A] = ???

}

object StreamsPlayground extends App {

}
