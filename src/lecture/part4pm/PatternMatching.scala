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

  /* Exercise
    Simple function uses PM, takes an expression and return human readable form
    ex:
      Sum(Number(3), Number(2), Number(1)) => "3 + 2 + 1"
      Prod(Sum(Number(1), Number(2)), Number(3)) => "(1 + 2) * 3"
      Sum(Prod(Number(2), Number(1)), Number(3)) => 2 * 1 + 3
  */
  trait Expr
  case class Number(n: Int) extends Expr
  case class Sum(e1: Expr, e2: Expr) extends Expr
  case class Prod(e1: Expr, e2: Expr) extends Expr

  def show(exp: Expr): String = exp match {
    case Sum(a, b) => s"${show(a)} + ${show(b)}"
    case Number(n) => s"$n"
    case Prod(a, b) if a.isInstanceOf[Number] && b.isInstanceOf[Number] =>
      s"${show(a)} * ${show(b)}"
    case Prod(a, b) if a.isInstanceOf[Number] =>
      s"${show(a)} * ( ${show(b)})"
    case Prod(a, b) if b.isInstanceOf[Number] =>
      s"(${show(a)}) * ${show(b)}"
    case Prod(a,b) => s"(${show(a)}) * (${show(b)})"
  }

  def showNew(exp: Expr): String = exp match {
    case Number(n) => s"$n"
    case Sum(a, b) => s"${showNew(a)} + ${showNew(b)}"
    case Prod(a, b) =>
      def showParenthesis(ex: Expr) = ex match {
        case Sum(_, _) => s"(${showNew(ex)})"
        case _ => showNew(ex)
      }
      showParenthesis(a) + " * " + showParenthesis(b)
  }

  println(show(Sum(Number(3), Number(2))))
  println(showNew(Sum(Number(3), Number(2))))
  println(show(Prod(Sum(Number(1), Number(2)), Number(3))))
  println(showNew(Prod(Sum(Number(1), Number(2)), Number(3))))
  println(show(Prod(Sum(Number(1), Number(2)), Sum(Number(3), Number(10)))))
  println(showNew(Prod(Sum(Number(1), Number(2)), Sum(Number(3), Number(10)))))
  println(show(Sum(Prod(Number(2), Number(1)), Number(3))))
  println(showNew(Sum(Prod(Number(2), Number(1)), Number(3))))
}
