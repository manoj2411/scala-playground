package lecture.advscalap2

// PAF - Partially Applied Function
object CurriesPAF_03 extends App {

  // curried functions: functions returning other function as results.
  val superAdder: Int => Int => Int =
    x => y => x + y

  val adder3 = superAdder(3) // Int => Int = 3 + y
  println(adder3(4))

  def curriedAdder(x: Int)(y: Int) = x + y
  /* val adder7 = curriedAdder(7) // throws error
  - curriedAdder is a method bcz its defined with `def`, not a Function.
    - method is always called with all parameters
    - we are attempting to call this method with fewer params list then the compiler expects.
    - Compiler is confused here, are we creating a Function here or trying to get the value of adder.
  - We want to use Function values in HOF, this is whole purpose of Functional Programming.
     - we can use methods to HOF unless they are transformed to Function value (Lifted)
     - JVM limitation - methods are part of instances of class (or object i.e. singleton object)
     - methods are not instances of functions.
  - If make the type annotation then it'll be ok like (it does the lifting):
  */
  val adder7: Int => Int = curriedAdder(7)

  /* Lifting: transforming a method to Function/Function value is called lifting, also called ETA-EXPANSION
      - functions != methods (JVM limitation)
  */
  def inc(s: Int) = s + 1
  List(0,1,2,3).map(inc) // compiler does ETA-EXPANSION for us, change the method to function

  // Partial function applications
  val add5 = curriedAdder(5) _ // telling compiler to do ETA-EXPANSION for me and convert it to a function and it'll not throw any error now.

  // Exercise:
  val simpleAddFn = (a: Int, b: Int) => a + b
  def simpleAddMethod(a: Int, b: Int) = a + b
  def curriedAddMethod(a: Int)(b: Int) = a + b
  // add7: Int => Int = y => 7 + y
  // add as many implementations of add9 using the above

  val add7_1 = (x: Int) => simpleAddFn(7, x)
  val add7_2 = simpleAddFn.curried(7)

  val add7_3 = curriedAddMethod(7) _   // PAF
  val add7_4 = curriedAddMethod(7)(_) // PAF - alternative syntax

  val add7_5 = simpleAddMethod(7, _: Int) // alternative syntax for turning method into function values
  val add7_6 = simpleAddFn(7, _: Int)

  /*      Underscore is very powerful     */
  def concat(x: String, y: String, z: String) = x + y + z
  val insertName = concat("Hello, I am ", _: String, ". How are you?") // x: String => concat(hey, x, howAreYou)
  println(insertName("Manoj"))

  val fillTheBlanks = concat("Hello, ", _: String, _: String) // (x: String, y: String) => concat(hey, x, y)
  println(fillTheBlanks("Tom", " I am awesome!"))

  /* Exercise:
  1. Process list of number and return string representation with different formats
      Use %4.2f, %8.6f and %14.12f with a curried formatter Fn. ex: println("%8.6f".format(22/7))
        println("PI: [%4.2f]".format(22/7f))
        println("PI: [%8.6f]".format(22/7f))
        println("PI: [%14.12f]".format(22/7f))
  */

  // 1. %4.2f, %8.6f and %14.12f
  def formatter(formatType: String)(x: Float) = formatType.format(x)

  val smallFormat = formatter("%4.2f") _ // LIFT this to a Fn value
  val midFormat = formatter(" %8.6f ")(_)
  val bigFormat = formatter(" %8.6f ")(_)
  val list = List(22/7f, 33/7f, 11/3f, 13/3f)
  println(list.map(smallFormat))
  println(list.map(midFormat))
  println(list.map(bigFormat))

  /*    Difference between parameters: by-name vs 0-lambda     */
  def byName(num: => Int) = num + 1
  def byFunction(fn: () => Int) = fn() + 1

  def method: Int = 42
  def methodWithParen(): Int  = 42
  /*
      Calling byName and byFunction
        - int
        - method
        - methodWithParen
        - lambda
        - PAF
  */
  byName(24)
  byName(method) // method is evaluated and result is returned
  byName(methodWithParen()) // method is evaluated and result is returned
  val lam1 = () => 24
  // byName(lam1) // it expects a value but its a Function
  // byName(methodWithParen _)

  // byFunction(24) // expects a Function value as param
  // byFunction(method) // method is not Function value
  // byFunction(methodWithParen()) // method is not Function value - need to lift
  byFunction(methodWithParen _) // works with lifting
  byFunction(methodWithParen)  // this is same as above
  byFunction(lam1)
  byFunction(methodWithParen _)  // this is same as above




}
