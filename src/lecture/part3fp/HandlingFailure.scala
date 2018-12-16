package lecture.part3fp

import scala.util.{Failure, Success, Try}

object HandlingFailure extends App {
  val _success = Success(24)
  val _failure = Failure(new RuntimeException("My exception!"))
  println(_success)
  println(_failure)

  def unsafeFun = throw new RuntimeException("Another failure!")
  // Try via apply method
  val potentialFailure = Try(unsafeFun)
  println(potentialFailure)
  val anotherFailure = Try {
    // code here
  }

  // utilities
  println(potentialFailure.isSuccess)
  println(potentialFailure.isFailure)
  // orElse, map, flatMap, filter
  println(_success.map(_ / 2))
  println(_success.flatMap(x => Success(x * 5)))
  println(_success.filter(_ > 100))

}
