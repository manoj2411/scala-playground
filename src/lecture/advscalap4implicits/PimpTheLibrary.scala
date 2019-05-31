package lecture.advscalap4implicits

object PimpTheLibrary extends App {

  /* type enrichment === pimping
      2.isEven : how can we do it?
        Add new functionality to the existing class
        We can do it with implicits
  */

  implicit class RichInt(value: Int) {
    def isEven : Boolean = value % 2 == 0
    def isOdd : Boolean = !isEven
  }
  println(12.isEven, 12.isOdd)

}
