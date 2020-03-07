package lecture.advscalap2

object Monads_05 extends App {

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

  // println(attempt)

  /* Exercise
  1. Implement a Lazy[T] monad. Computation will be executed only when its needed.
    - create unit/apply
    - create flatMap
  */
  class Lazy[+A](value: => A) {
    lazy val result = value
    def flatMap[B](fn: (=> A) => Lazy[B]): Lazy[B] = fn(result)

  }
  object Lazy {
    def apply[A](value: => A): Lazy[A] = new Lazy(value)
  }

  val lazzy = Lazy {
    println("long processing!")
    24
  }
  val flatMapped = lazzy.flatMap(v => Lazy{
    v * 10
  })
  val anotherFlatMapped = lazzy.flatMap(v => Lazy{
    v * 10
  })

  flatMapped.result
  flatMapped.result // using lazy evaluation by previous eval
  anotherFlatMapped.result // using lazy evaluation by previous eval

  /* Exercise
  2. Monads = unit + flatMap === unit + map + flatten
    Monad[T] {
      def flatMap[B](fn: A => Monad[B]): Monad[B] // assume its implemented.
      def map[B](fn: A => B): Monad[B] = flatMap( x => ???
      def flatten(monad: Monad[Monad[A]]): Monad[A] = ???
    }
    Think it as a List

    map & flatten can be defined in terms of flatMap and its tru for all monads
      def map[B](fn: A => B): Monad[B] = flatMap( x => apply(f(x)))
      def flatten(monad: Monad[Monad[A]]): Monad[A] = monad.flatMap()

   Example:
  */
  /*  Flattening using flatMap   */
  //  val s1: Option[Option[Int]] = Some(Some(24))
  //  println(s1)
  //  println(s1.flatMap(x => x))

  /*  Mapping using flatMap   */
  val fn: Int => Int = x => x * 10
  val s1: Option[Int] = Some(24)
  println(s1.flatMap(x => Some(fn(x))))

}
