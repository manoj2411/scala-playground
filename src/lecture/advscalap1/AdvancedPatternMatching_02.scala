package lecture.advscalap1

// 02 - This section is mostly about how can you make your own created classes, collection compatible with Pattern Matching
//      and the key methods for this are unapply and unapplySeq
object AdvancedPatternMatching_02 extends App {

  val numbers = List(24)
  numbers match {
    case head :: Nil => println(s"Only element: $head")
    case _ =>
  }

  /* How make our own structures compatible with PM. 99% standard PM will be enough.
      - just create an companion object and define method `unapply` and return tuple
  */
  class Person(val name: String, val age: Int)
  object Person {
    def unapply(person: Person): Option[(String, Int)] =
      if (person.age < 18 ) None
      else Some((person.name, person.age))

    def unapply(age: Int): Option[String] = Some(if (age < 18) "minor" else "major")
  }
  val tom = new Person("Tom", 35)
  tom match {
    // In this case, Person.unapply is being called simply and its returned values are fed to the args.
    case Person(n, a) => println(s"Hey, I am $n and I am $a years old!")
    case _ => println("None")
  }
  /* `object Person` need not to be same as class, it can be any singleton object but its a good practice to keep the same name as class.
   - We can overload the unapply method and define multiple patterns
  */
  tom.age match {
    case Person(status) => println(s"My legal status us $status")
  }

  // Exercise: Create own PM in which multiple conditions of a val will be applied 1 after another. Like
  val n = 4
  val mathProperty = n match {
    case x if x < 10 => "Single digit"
    case x if x % 2 == 0 => "an even number"
    case _ => "No property"
  }

  object isEven {
    def unapply(number: Int): Boolean = number % 2 == 0
  }
  object isSingleDigit {
    def unapply(number: Int): Boolean = number < 10
  }

  val newMathProp = n match {
    case isSingleDigit() => "Single digit number"
    case isEven() => "An even number"
    case _ => "No property"
  }
  println(newMathProp)
  // A quick way to write tests for pattern matching is to define Singleton object with unapply which returns Boolean.
  // The advantage of this approach is that you can reuse these boolean and tests in other pattern matches

  // ===================================================

  // Infix patterns, it'll work only when you 2 things in pattern.
  case class Or[A, B](a: A, b: B) // In scala this is called Either
  val either = Or(4, "Four")
  val desc = either match {
    // case Or(number, string) => s"$number is written as $string"
    // ==
    case number Or string => s"$number is written as $string"
  }
  println(desc)
  // Similarly this can be written as
  case class ||[A, B](a: A, b: B)
  val myOr = ||(4, "Four")
  val myResult = myOr match {
    // case ||(number, string) => s"$number is written as $string"
    // ==
    case number || string => s"$number is written as $string"
    // similarly case head :: Nil, :: is the infix pattern here.
  }
  println(myResult)

  // ===================================================

  // Decomposing sequences - This is useful when you are building your own Collection and want to use it with
  //   match { case MyList(2,4,_*) ... }
  abstract class MyList[+A] {
    def head: A = ???
    def tail: MyList[A] = ???
  }
  case object Empty extends MyList[Nothing]
  case class Cons[+A](override val head: A, override val tail: MyList[A]) extends MyList[A]
  object MyList {
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] =
      if (list == Empty) Some(Seq.empty)
      else unapplySeq(list.tail).map(list.head +: _)
  }
  val myList: MyList[Int] = Cons(2, Cons(4, Cons(5, Empty)))
  val decomposed = myList match {
    case MyList(2,4,_*) => "Starting with 2, 4"
    case _ => "Something else"
  }
  println(decomposed)

  // ===================================================

  // Custom return types for unapply:
  // The DataStructure we want to return only needs to have 2 methods:
  //  isEmpty: Boolean, get: something

  trait Wrapper[T] {
    def isEmpty: Boolean
    def get: T
  }

  object PersonWrapper {
    def unapply(person: Person): Wrapper[String] = new Wrapper[String] {
      override def isEmpty: Boolean = false
      override def get: String = person.name
    }
  }

  val person = new Person("Modi", 42)

  val name = person match {
    case PersonWrapper(personName) => s"Hey, this is $personName"
  }
  println(name)

}
