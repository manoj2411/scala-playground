package lecture.advscalap4implicits

object PimpTheLibrary extends App {

  /* type enrichment === pimping
      2.isEven : how can we do it?
        Add new functionality to the existing class
        We can do it with implicits

    So enrichment allow us to decorate existing classes that we may not have access to, with additional
      methods and properties.
    We define implicit class which TAKE 1 AND ONLY 1 ARGUMENT.

    Other examples: 1 to 10, or 2.seconds etc
  */

  implicit class RichInt(value: Int) {
    def isEven : Boolean = value % 2 == 0
    def isOdd : Boolean = !isEven
  }
  println(24.isEven, 24.isOdd)

  /* How it works

    when I type 42.even the compiler interprets that as an error, but it doesn't give up.
    It searches for all the implicit classes or the implicit conversions that can wrap a type int into
      something that contains the method isEven.
    The compiler then inserts an invisible construct call. It rewrites it to :
    24.isEven => new RichInt(24).isEven

   NOTE: complier doesn't do multiple implicit searches. Ex:
    if we a class => implicit class RicherInt(value: RichInt) { def sqrt = ??? } then
    42.sqrt DOESN'T WORK
  */
}
