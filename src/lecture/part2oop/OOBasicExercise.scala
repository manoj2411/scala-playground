package lecture.part2oop

import java.time.Year

object OOBasicExercise extends App {

  val counter = new Counter(0)
  counter.incr.incr.incr.print
  counter.incr(10).print
  counter.decr.decr.print
  counter.decr(5).print

}

/*  Novel and a Writer
*
*     Writer: first name, last name, year
*      - fullname
*
*     Novel: name, auther, year of release
*      - autherAge
*      - isWrittenBy
*      - copy(new year of relase) = new
*
* */


class Writer(val firstName: String, val lastName: String, val year: Int) {
  def fullname = firstName + " " + lastName
  def ==(writer: Writer) : Boolean =
    writer.firstName == firstName && writer.lastName == lastName && writer.year == year
}

class Novel(name: String, auther: Writer, releaseYear: Int) {
  def autherAge= releaseYear - auther.year
  def isWrittenBy(auther: Writer) = auther == this.auther
  def copy(year: Int) = new Novel(name, auther, year)
}

/*
* Counter class
*       - receives an int value
*       - method current count
*       - method incr/decr returns new counter
*       - overload incr/decr to receive an amount
* */

class Counter(value: Int) {
  def current_count = value
  def incr = {
    println("incr")
    new Counter(value + 1)
  }
  def decr = {
    println("decr")
    new Counter(value - 1)
  }

  // for simple cases its fine but lets say we are logging inside our incr and decr methods
  // now we want to reuse them
  //  def incr(x: Int) = new Counter(value + x)
  //  def decr(x: Int) = new Counter(value - x)

  def incr(x: Int): Counter =
    if (x == 0) this
    else incr.incr(x - 1)

  def decr(x: Int): Counter =
    if (x == 0) this
    else decr.decr(x - 1)

  def print = println(value)
}
