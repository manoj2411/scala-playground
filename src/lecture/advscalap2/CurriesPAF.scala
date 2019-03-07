package lecture.advscalap2

object CurriesPAF extends App {

  // curried functions: functions returning other function as results.
  val superAdder: Int => Int => Int =
    x => y => x + y

  val adder3 = superAdder(3) // Int => Int = 3 + y
  println(adder3(4))

  def curriedAdder(x: Int)(y: Int) = x + y
  /* val adder7 = curriedAdder(7) // throws error
  - we are attempting to call this method with fewer params list then the compiler expects.
  - Compiler is confused here, are we creating a function here or trying to get the value of adder.
  - If make the type annotation then it'll be ok like:
  */
  val adder7: Int => Int = curriedAdder(7)

  /* Lifting: transforming a method to function/function value is called lifting, also called ETA-EXPANSION
      - functions != methods (JVM limitation)
  */
  def inc(s: Int) = s + 1
  List(0,1,2,3).map(inc) // compiler does ETA-EXPANSION for us, change the method to function

  // Partial function applications
  val add5 = curriedAdder(5) _ // telling compiler to do ETA-EXPANSION for me and convert it to a function and it'll not throw any error now.
}
