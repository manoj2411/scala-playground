package lecture.advscalap4implicits

import java.util.Date

object TypeClasses extends App {
  /*
  - a type class is a trait that takes a type and describes what operations can be applied to that type.
  */
  // Ex: we have a trait
  trait HTMLWritable {
    def toHTML: String
  }

  case class User(name: String, age: Int, email: String) extends HTMLWritable {
    override def toHTML: String = s"<div>$name ($age yo) <a href=$email /> </div>"
  }
  User("Bob", 21, "bob@gmail.com").toHTML
  /* The code above will work but it has 2 disadvantages:
  1. This only works with the types we write.
    - Other types like java standard Date, we need to conversion which is not elegant
  2. Only one implementation.
    - if we want to have different implementation for user who is not logged in.

  Another solution is Pattern Matching. ex:
  */

  object HTMLSerializerPM {
    def serializeHTML(value: Any) = value match {
      case User(n, a, e) =>
//      case Date =>
      case _ =>
    }
  }
  /* Problems with this:
  1. We lost type safety
  2. Need code modification every time
  3. Still 1 implementation with each given type
  Still not a good design

  Let see a better design
  */
  trait HTMLSerializer[T] {
    def serialize(value: T): String
    def foo = "bar"
  }
  // Now add a serializer for User

  implicit object UserSerializer extends HTMLSerializer[User] {
    override def serialize(user: User): String =
      s"<div>${user.name} (${user.age} yo) <a href=${user.email} /> </div>"
  }
  val bob = User("Bob", 21, "bob@gmail.com")
//  println(UserSerializer.serialize(bob))

  /* What is good in this design?
  1. we can define serializers for other types
    - even the type we have't written, from some library, ex:
  2. We can define multiple serializers for a type
  */

  object DateSerializer extends HTMLSerializer[Date] {
    override def serialize(date: Date): String = s"<div>${date.toString}</div>"
  }

  object NotLoggedInUserSerializer extends HTMLSerializer[User] {
    override def serialize(user: User): String = s"<div>${user.name}</div>"
  }
  /* This HTMLSerializer things is called TYPE CLASS
  - a type class specifies a set of operations that can be applied to a given type
  - Implementors of type class are called type class instances which are singleton objects mostly
  */

  // A typical type class template: always have a type param, have some actions that returns something.
  trait MyTypeClassTemplate[T] {
    def action1(value: T): String
    def action2(value: T): List[String]
  }

  /* PART 2 */
  object HTMLSerializer {
//    def serialize[T](value: T)(implicit serializer: HTMLSerializer[T]): String = serializer.serialize(value)
    def apply[T](implicit serializer: HTMLSerializer[T]) = serializer
  }
  implicit object IntSerializer extends HTMLSerializer[Int] {
    override def serialize(value: Int): String = s"<div style='color: blue'>$value</div>"
  }
//  println(HTMLSerializer.serialize(24)(IntSerializer))
  // if we make serializer instances implicit then we can omit 2nd parameter like:
  // implicit object IntSerializer...
  println(HTMLSerializer[Int].serialize(24))
  println(HTMLSerializer[User].serialize(bob))
  /* The biggest advantage of this approach is, we can just write "HTMLSerializer.serialize(value)" and it'll work if we have
      implicit serializer instance available for the given value type

    Even better design is, have an factory method apply in companion object which take an implicit serializer and just returns it
      The good thing about this design is that when we call apply like: HTMLSerializer[User], we have access to entire type class interface.
      i.e. not only to the serialize methods but other methods as well.
  */
  HTMLSerializer[User] // here we have access to the entire type class interface

  implicit class HTMLEnrichment[T](value: T) {
    def toHtml(implicit serializer: HTMLSerializer[T]) = serializer.serialize(value)
  }
  println(bob.toHtml)
  // this will be rewritten by complier like println(new HTMLEnrichment[User](bob.toHtml(UserSerializer))
  // THIS IS COOL!!!
  println(24.toHtml)

  /* Main elements to enhance existing types with type class:
      - type classes --- MyTypeClassTemplate or HTMLSerializer[T]
      - type class instances --- DateSerializer, UserSerializer, IntSerializer
      - conversion/enrichment with implicit classes --- HTMLEnrichment
  */
}
