package lecture.part4pm

import scala.util.Random

object PatternMatching extends App {

  val rand = new Random
  val x = rand.nextInt(5)
  val desc = x match {
    case 1 => "The one"
    case 2 => "Double"
    case 3 => "Three is lucky for some"
    case _ => "Something else" // _ is WILDCARD
  }
  println(x + ": " + desc)

  // Properties:
  // 1. Can Decompose values
  case class Person(name: String, age: Int)
  val tom = Person("Tom", 24)

  val greeting = tom match {
    case Person(n, a) if a < 25 => s"Hey, I'm $n and I can't go to bars."
    case Person(n, a) => s"Hey, I'm $n and I'm $a years old"
    case _ => "I am not identified :|"
  }
  println(greeting)

}
