package playground

/*
  An extractor in Scala is an object that has a method called unapply as one of its members. The purpose of that unapply method is to match a value and take it apart. Often, the extractor object also defines a dual method apply for building values, but this is not required.

  An extractor object is an object with an unapply method. Whereas the apply method is like a constructor which takes arguments and creates an object, the unapply takes an object and tries to give back the arguments. This is most often used in pattern matching and partial functions

*/
import scala.util.Random

object CustomerID {

  def apply(name: String) = s"$name--${Random.nextLong}"

  def unapply(customerID: String): Option[String] = {
    val stringArray: Array[String] = customerID.split("--")
    if (stringArray.tail.nonEmpty) Some(stringArray.head) else None
  }
}

object ExtractorInScala {
  //  def main(args: Array[String]) {
  //    println ("Apply method : " + apply("Zara", "gmail.com"));
  //    println ("Unapply method : " + unapply("Zara@gmail.com"));
  //    println ("Unapply method : " + unapply("Zara Ali"));
  //      val customer1ID = CustomerID("Sukyoung")  // Sukyoung--23098234908
  //      customer1ID match {
  //        case CustomerID(name) => println(name)  // prints Sukyoung
  //        case _ => println("Could not extract a CustomerID")
  //      }
  //  }

  // The injection method (optional)
  def apply(user: String, domain: String) =  user +"@"+ domain


  // The extraction method (mandatory)
  def unapply(str: String): Option[(String, String)] = {
    val parts = str split "@"

    if (parts.length == 2) Some(parts(0), parts(1))
    else None
  }
  // the purpose of the unapply method is to extract a specific value we are looking for. It does the opposite operation apply does. When comparing an extractor object using the match statement the unapply method will be automatically executed
  def main(args: Array[String]): Unit = {
    val x = ExtractorInScala(5)
    println(x)

    x match {
      //unapply is invoked
      case ExtractorInScala(num) => println(x + " is bigger two times than " + num)
      case _ => println("i cannot calculate")
    }
  }
  def apply(x: Int) = x * 2
  def unapply(x: Int): Option[Int] = if (x % 2 == 0) Some(x / 2) else None
}
