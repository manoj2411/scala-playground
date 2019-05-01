package lecture.advscalap4implicits

object Intro extends App {

  case class Person(name: String) {
    def greet = s"Greetings, I am $name"
  }

  implicit def stringToPerson(name: String): Person = Person(name)

//  println("Tom".greet) // compiler do the conversion with available implicit

  // Having multiple possible implicit, compiler will error, ex:
  class Tmp {
    def greet = 24
  }
//  implicit def stringToTmp(name: String): Tmp= new Tmp

  /* Implicit parameters : NOT same as default arguments */
  def increment(value: Int)(implicit amount: Int) = amount + value
  implicit val defaultAmount = 42
  println(increment(2))
}
