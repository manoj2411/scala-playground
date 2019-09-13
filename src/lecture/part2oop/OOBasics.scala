package lecture.part2oop

object OOBasics {
  val person = new Person("Tom", 22)

  /* class params are not fields, ex: person.name , this gives compiler error */

}


class Person(name: String, age: Int) { // constructor
   /*
    * - class params are not fields
    * - we can make param as field by specifying "var" or "val" like: Person(val age: Int)
    */

  val x = 3 // this is a fiels of this class

  def greeting(name: String) = s"hey from $name to $name" // here both $name are referring to local name
    // to solve this ambiguity use "this" keyword to resolve it to class param.
  def greeting = s"hey from $name"

  // multiple constructor
  def this(name: String) = this(name, 0) // calls the primary constructor
  // These are not used mostly because they only calls primary constructor with default values and
  // this can be easily achieved by having default values of class params.

}