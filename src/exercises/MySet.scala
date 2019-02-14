package exercises

import scala.annotation.tailrec

trait MySet[A] extends (A => Boolean) {
  // Exercise: Implement a functional set.
  def apply(element: A) = contains(element)

  def contains(element: A): Boolean
  def +(element: A): MySet[A]
  def ++(anotherSet: MySet[A]): MySet[A] // Union

  def map[B](f: A => B): MySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B]
  def filter(predicate: A => Boolean): MySet[A]
  def foreach(f: A => Unit): Unit
  // part 2
  def -(element: A): MySet[A]
  def &(anotherSet: MySet[A]): MySet[A] // intersection
  def --(anotherSet: MySet[A]): MySet[A] // difference
  // part 3
  def unary_! : MySet[A] // NEGATION of a set
}

class EmptySet[A] extends MySet[A] {
  def contains(element: A): Boolean = false
  def +(element: A): MySet[A] = new NonEmptySet(element, this)
  def ++(anotherSet: MySet[A]): MySet[A] = anotherSet

  def map[B](f: A => B): MySet[B] = new EmptySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]
  def filter(f: A => Boolean): MySet[A] = this
  def foreach(f: A => Unit): Unit = ()
  // part 2
  def -(element: A) = this
  def &(anotherSet: MySet[A]): MySet[A] = this
  def --(anotherSet: MySet[A]): MySet[A] = this
  def unary_! : MySet[A] = new PropertyBasedSet[A](_ => true)
}

// All elements of type A which satisfies a property: { x in A | property(x) }
class PropertyBasedSet[A](property: A => Boolean) extends MySet[A] {
  def contains(element: A): Boolean = property(element)
  def +(element: A): MySet[A] = new PropertyBasedSet[A](x => property(x) || x == element)
  def ++(anotherSet: MySet[A]): MySet[A] = new PropertyBasedSet[A](x => property(x) || anotherSet(x))

  def map[B](f: A => B): MySet[B] = deepHoleException
  def flatMap[B](f: A => MySet[B]): MySet[B] = deepHoleException
  def foreach(f: A => Unit): Unit = deepHoleException
  def filter(predicate: A => Boolean): MySet[A] = new PropertyBasedSet[A](x => property(x) && predicate(x))

  def -(element: A): MySet[A] = filter(_ != element)
  def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)
  def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)

  def unary_! : MySet[A] = new PropertyBasedSet[A](!property(_))

  def deepHoleException = throw new IllegalArgumentException("Deep rabit hole!")
}

class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A] {

  def contains(element: A): Boolean = head == element || tail.contains(element)
  def +(element: A): MySet[A] =
    if (this contains element) this
    else new NonEmptySet(element, this)

  def ++(anotherSet: MySet[A]): MySet[A] =
    tail ++ anotherSet + head
//    val newElements = anotherSet.filter((x: A)=> !this.contains(x))
//    tail ++ newElements

  def map[B](f: A => B): MySet[B] = tail.map(f) + f(head)
  def flatMap[B](f: A => MySet[B]): MySet[B] = f(head) ++ tail.flatMap(f)
  def filter(predicate: A => Boolean): MySet[A] =
    if (predicate(head)) (tail filter predicate) + head
    else tail filter predicate

  def foreach(f: A => Unit): Unit = {
    f(head)
    tail foreach f
  }
  // part 2
  def -(element: A): MySet[A] =
    if (head == element) tail
    else tail - element + head
  def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet) // filter(x => anotherSet.contains(x))
  def --(anotherSet: MySet[A]): MySet[A] = filter(x => !anotherSet(x))

  //part 3
  override def unary_! : MySet[A] = new PropertyBasedSet[A](!contains(_))
}

object MySet {
  def apply[A](values: A*): MySet[A] = {
    @tailrec
    def buildSet(seq: Seq[A], acc: MySet[A]): MySet[A] =
      if (seq.isEmpty) acc
      else buildSet(seq.tail, acc + seq.head)
    buildSet(values, new EmptySet)
  }
}

object MySetPlayground extends App {
  val set = MySet(2,3,5) + 1 ++ MySet(3,5,6)
  set map(_ * 10) flatMap(x => MySet(x, x / 10)) filter(_ % 2 == 0) foreach println
  println(set(20))
  println(set(3))
  println("Remove")
  set - 6 foreach println
  println("intersection")
  set & MySet(3,4,5)  foreach println
  println("difference")
  set -- MySet(3,4,5) foreach println

  val negative = !set // => s.unary_! = all naturals not in 1,2,3,5,6
  println(negative(2))
  println(negative(4))
}