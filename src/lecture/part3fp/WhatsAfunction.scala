package lecture.part3fp

object WhatsAfunction extends App {
  // user and work with Fn as First Class elements
  // Problem: OOP world

  val stringToIntConverter = new Function1[String, Int] {
    override def apply(str: String): Int = str.toInt
  }

//    println(stringToIntConverter("343") * 10)

  val adder = new Function2[Int, Int, Int] {
    override def apply(x: Int, y: Int): Int = x + y
  }
  val multiplier: ((Int, Int) => Int) = new Function2[Int, Int, Int] {
    override def apply(x: Int, y: Int): Int = x * y
  }

//  println(adder(3,5))

  /*
  * 1. Fn (str1, str2) => concat
  * 2. transform MyPredicate and MyTransformer into FunctionTypes
  * 3. define a function which takes an int and return another function which takes an int and return int
  *   - what's the type of this function
  *   - how to do it
  * */

  // curried function

  val superAdder: Function1[Int, Function1[Int, Int]] = new Function[Int, Function1[Int, Int]] {
    override def apply(x: Int): Function1[Int, Int] = new Function[Int, Int] {
      override def apply(y: Int): Int = x + y
    }
  }

//  val superAdderLambda: (Int => (Int => Int)) = (x) => (y) => x + y
  val superAdderLambda = (x: Int) => (y: Int) => x + y

  println(superAdder(2)(4))
  println(superAdderLambda(2)(4))
}
