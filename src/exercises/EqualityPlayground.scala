package exercises

import lecture.advscalap4implicits.TypeClasses.User

object EqualityPlayground extends App {

  /* Equality */
  trait Equal[T] {
    def apply(x: T, y: T): Boolean
  }

  // Compare users by name and compare user by both name * email
  implicit object NameEquality extends Equal[User] {
    override def apply(x: User, y: User): Boolean = x.name == y.name
  }

  object FullEquality extends Equal[User] {
    override def apply(x: User, y: User): Boolean = NameEquality(x, y) && x.email == y.email
  }

  /* EXERCISE: implement type class pattern for Equality type class */
  object Equal {
    //    def apply[T](x: T, y: T)(implicit equality: Equal[T]) = equality(x, y)
    def apply[T](implicit equality: Equal[T]) = equality
  }

  val bob = User("Bob", 21, "bob@gmail.com")
  val bob2 = User("Bob", 24, "bob2@gmail.com")
  println(Equal[User].apply(bob, bob2))

}
