package lecture.part1basics

object Functions extends App {

  /* Exercise
      1. factorial function
      2. A fibonacci function
      3. If a number is prime
  */

  def factorial(number: Int): Int =
    if (number <= 1) 1
    else number * factorial(number - 1)

//  for (i <- 1 to 5) {
//    println(s"factorial of $i : " + factorial(i))
//  }

  /*
  f(1) - 1
  f(2) - 1
  f(3) - 2
  f(4) - 3
  */
  def fibbonacci(number: Int): Int = {
    if (number <= 2) 1
    else fibbonacci(number - 1) + fibbonacci(number - 2)
  }

//  for (i <- 1 to 10) {
//    println(s"fibonacci of $i : ${fibbonacci(i)}")
//  }

  def isPrime(number: Int): Boolean = {
    def isPrimeUntil(i: Int): Boolean =
      if (i <= 1) true
      else (number % i != 0) && isPrimeUntil(i - 1)

    isPrimeUntil(number / 2)
  }

//  for (i <- 2 to 25) {
//    println(s"$i isPrime? : ${isPrime(i)}")
//  }
  println(isPrime(2003))
  println(isPrime(37))
  println(isPrime(17 * 13))

}
