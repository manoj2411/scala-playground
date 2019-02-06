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
}

class EmptySet[A] extends MySet[A] {
  def contains(element: A): Boolean = false
  def +(element: A): MySet[A] = new NonEmptySet(element, this)
  def ++(anotherSet: MySet[A]): MySet[A] = anotherSet

  def map[B](f: A => B): MySet[B] = new EmptySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]
  def filter(f: A => Boolean): MySet[A] = this
  def foreach(f: A => Unit): Unit = ()
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

object Runner extends App {
  val set = MySet(2,3,5) + 1 ++ MySet(3,5,6)
  set map(_ * 10) flatMap(x => MySet(x, x / 10)) filter(_ % 2 == 0) foreach println
  println(set(20))
  println(set(3))
}