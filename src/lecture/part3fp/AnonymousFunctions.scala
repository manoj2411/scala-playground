package lecture.part3fp

object AnonymousFunctions extends App {

  val doubler = new Function1[Int, Int] {
    override def apply(x: Int) = x * 2
  }
  // This is a problem since we have instantiate and dp override def apply everytime
  // Its shothand method is:
  val newDoubler = (x: Int) => x * 2
  // Its also called LABMDA
  val newShortDoubler: Int => Int = x => x * 2

  // No params
  val something = () => 24

  // curly braces with lambda
  val stringToInt = { (str: String) =>
    str.toInt
  }

  // MOAR syntactic sugar
  val myIncrementer: Int => Int = (x: Int) => x + 1
  val myShprtIncrementer: Int => Int = _ + 1
  val shortAdder: (Int, Int) => Int = _ + _ // equivalent to = (a, b) => a + b

  /* Exercises
    1. MyList: replace FunctionX calls with lambdas
    2. Rewrite specialAdder (curried) as anonymous function
    3.

  * */
}
