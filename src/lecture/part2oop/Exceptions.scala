package lecture.part2oop

object Exceptions extends App {

  def failFirst(throwException: Boolean): Int =
    if (throwException) throw new NullPointerException
    else 24

  // The type of result depends on value returned by try block and catch block, finally will not be considered
  val result = try {
    failFirst(true)
  } catch {
    case NullPointerException => 0
  } finally {
    "closing"
  }


}
