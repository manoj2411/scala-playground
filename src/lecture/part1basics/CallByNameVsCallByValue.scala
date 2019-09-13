package lecture.part1basics

object CallByNameVsCallByValue extends App {

  def callByValue(x: Long): Unit = {
    println("by value: " + x)
    println("by value: " + x)
  }

  /* By Name: The expression is passed as it is and it'll be evaluated every time
   *  like labmda passed in ruby :)
   *
   *  */
  def callByName(x : => Long): Unit = {
    println("by name: " + x)
    println("by name: " + x)
  }

  callByValue(System.nanoTime())
  callByName(System.nanoTime())

  /* Note: It delays the evaluation until it is being used.
   *  */

  def infinite: Int = 1 + infinite
  def printFirst(first: Int, second: => Int) = println(first)

//  printFirst(infinite, 24) // IT WILL BREAK
  printFirst(24, infinite) // It'll work fine

}
