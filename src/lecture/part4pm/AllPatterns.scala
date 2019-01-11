package lecture.part4pm

import exercises.Empty

object AllPatterns extends App {

  // PM on Tuples
  val aTuple = (2,4)
  val matchTuple = aTuple match {
    case (1,2) =>
    case (something, 4) =>  s"I'v found $something"
  }
  println(matchTuple)

  val nestedTuple = (1, (2,4))
  val matchNestedTuple = nestedTuple match {
    case (_, (a, 4)) => s"In nested tuple $a"
  }
  println(matchNestedTuple)

  // List pattern
  val aList = List(1,2,24)
  val matchList = aList match {
    case List(_ ,_ ,24) => // extractor
    case List(1, _*) => // list of arbitrary length
    case 1 :: List(_) => // infix pattern
    case List(1,2) :+ 24 => // infix pattern
  }

  // Type specifiers
  val any: Any = 24
  val anyMatch = any match {
    case list: List[Int] => // explicit type specifier
    case _ => "unknown"
  }
  println(anyMatch)

  // Name binding
  val nameBindingMatch = aList match {
    case lst @ List(_, _, _) => // Name binding, use the name here
  }

  // multi patterns
  val multiMatch = any match {
    case Empty | List => // return same expression for multiple cases
    case _ =>
  }

  val listMatch = aList match {
    case stringList: List[String] => "List of string"
    case intList: List[Int] => "List of int"
  }

  println(listMatch)
}

