package lecture.part3fp

object HOFsCurries extends App {

  // a function applies another fn n times on k

  // HOF example - nTimes
  def nTimes(fn: Int => Int, n: Int, k: Int): Int =
    if (n <= 0) k
    else nTimes(fn, n - 1, fn(k))

  val incrementer: Int => Int = _ + 1
  println(nTimes(incrementer, 10, 0))

  // Better way to implement this nTimes function

  // fn nTimes take a fn and take number and returns another fn which is application
  // of this fn ntimes which we can then use it for any value we want.
  val incrOne = (x: Int) => x + 1
  
  def nTimesNew(fn: Int => Int, n: Int): Int => Int =
    if (n <= 0) (x: Int) => x
    else (x: Int) => nTimesNew(fn, n - 1)(fn(x))

  val plusSix = nTimesNew(incrOne, 6)
  println(plusSix(26))

}
