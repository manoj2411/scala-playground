package lecture.part3fp

object WhatsAfunction extends App {
  /*
      Use and work with Fn as First Class elements, means we work with functions
        like we worked with plain values
   */

  // Problem: OOP world, jvm is designed for OOP means we create class,
  //    instance of those classes then these objects has functions

  val doubler = new Function1[Int, Int] {
    override def apply(x: Int): Int = x * 2
  }
  println(doubler(24))

  /* ALL SCALA FUNCTIONS ARE OBJECTS */

  // FUNCTIONS TYPES: Function1, Function2...Function22
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

  // println(adder(3,5))

  /*
  * 1. Fn (str1, str2) => concat
  * 2. transform MyPredicate and MyTransformer into FunctionTypes
  * 3. define a function which takes an int and return another function which takes an int and return int
  *   - what's the type of this function
  *   - how to do it
  * */


  /*
    Higher Order Functions (HOF)
      - either receive functions a parameters or return other functions a result
      - ex: map, flatMap, filter and superAdder
      - HOFs are critical concept to FP because it uses functions as first class values
  */

  // curried functions has the property that called with multiple parameter list
  val superAdder: Function1[Int, Function1[Int, Int]] = new Function[Int, Function1[Int, Int]] {
    override def apply(x: Int): Function1[Int, Int] = new Function[Int, Int] {
      override def apply(y: Int): Int = x + y
    }
  }

//  val superAdderLambda: (Int => (Int => Int)) = (x) => (y) => x + y
  val superAdderLambda = (x: Int) => (y: Int) => x + y

  println(superAdder(2)(4))
  println(superAdderLambda(2)(4)) // curried functions
}
