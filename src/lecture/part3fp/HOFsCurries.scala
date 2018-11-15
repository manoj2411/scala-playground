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

}
