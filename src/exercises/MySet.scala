package exercises

trait MySet[A] extends (A => Boolean) {
  // Exercise: Implement a functional set.
  def contains(element: A): Boolean
  def +(element: A): MySet[A]
  def ++(anotherSet: MySet[A]): MySet[A] // Union

  def map[B](f: A => B): MySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B]
  def filter[A](predicate: A => Boolean): MySet[A]
  def foreach(f: A => Unit): Unit
}
