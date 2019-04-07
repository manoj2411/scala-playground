package lecture.advscalap2

object Monads extends App {

  // a custom Try monad implementation
  trait Attempt[+A] {
    def flatMap[B](fn: A => Attempt[B]): Attempt[B]
  }
  object Attempt {
    def apply[A](element: => A): Attempt[A] =
      try {
        SuccessEval(element)
      } catch {
        case ex: Throwable => FailureEval(ex)
      }
  }

  case class SuccessEval[A](value: A) extends Attempt[A] {
    def flatMap[B](fn: A => Attempt[B]): Attempt[B] =
      try {
        fn(value)
      } catch {
        case ex: Throwable => FailureEval(ex)
      }

  }
  case class FailureEval(ex: Throwable) extends Attempt[Nothing] {
    def flatMap[B](fn: Nothing => Attempt[B]):Attempt[B] = this
  }

  val attempt = Attempt {
    throw new RuntimeException("testing monad!")
  }

  println(attempt)
}
