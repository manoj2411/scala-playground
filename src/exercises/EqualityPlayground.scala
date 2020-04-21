package exercises

import lecture.advscalap4implicits.TypeClasses_03.User

object EqualityPlayground extends App {

  /* Equality */
  trait Equal[T] {
    def apply(x: T, y: T): Boolean
  }

  // Compare users by name and compare user by both name * email
  object NameEquality extends Equal[User] {
    override def apply(x: User, y: User): Boolean = x.name == y.name
  }

  implicit object FullEquality extends Equal[User] {
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

  /* Exercise: Improve Equal TC with implicit conversion class
      This conversion class will have 2 methods:
        1. ===(anotherValue: T)
        2. !==(anotherValue: T)
  */

  implicit class EnrichedEqual[T](value: T) {
    def ===(anotherValue: T)(implicit equality: Equal[T]) =
      equality(value, anotherValue)

    def !==(anotherValue: T)(implicit equality: Equal[T]) =
      !(value === anotherValue)
  }

  println(bob === bob2)

  /* This === is TYPE SAFE, lets see how
      if we write bob === 24 it'll not compile whereas bob == 24 will return false but NOT TYPE SAFE
  */

}
