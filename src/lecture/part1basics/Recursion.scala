package lecture.part1basics

import scala.annotation.tailrec

object Recursion extends App {

  /* Tail Recursion: last expression should be the recursive call
      When you need loop, user TAIL RECURSION
  */
  def factorial(number: Int): BigInt = {
    def helper(n: Int, acc: BigInt) : BigInt =
    if (n <= 1) acc
    else helper(n - 1, n * acc)

    helper(number, 1)
  }

//  println(factorial(5000))

  /* Exercise, write tail recursive
      1. concat a string n times
      2. isPrime
      3. fibonacci
  */

  def concat(str: String, number: Int): String = {
    @tailrec
    def tailRecHelper(n: Int, acc: String): String =
      if (n <= 1) acc
      else tailRecHelper(n - 1, acc + str)

    tailRecHelper(number, str)
  }

//  println(concat("helo", 3))


  def isPrime(number: Int): Boolean = {
    @tailrec
    def tailRecHelper(n: Int, isStillPrime: Boolean): Boolean =
      if (n <= 2 || !isStillPrime) isStillPrime
      else tailRecHelper(n - 1, number % n  != 0 && isStillPrime )

    tailRecHelper(number / 2, true)
  }

//  println(isPrime(37))
//  println(isPrime(13))
//  println(isPrime(17))
//  println(isPrime(27))


//  ===================

  def fibonacci(n: Int): Int = {
    @tailrec
    def tailRecHelper(i: Int, prev: Int, curr: Int): Int =
      if (i >= n) curr
      else tailRecHelper(i + 1, curr, curr + prev)

    tailRecHelper(2, 1, 1)
  }

  println(fibonacci(8))
}
