package lecture.advscalap4implicits

object OrganisingImplicits extends App {
  // We'll focus on how compiler looks for implicits and where, and its priorities
  // ex: sort method
  implicit val decrOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _ )
//  print(List(2,4,1,5,3).sorted) // takes implicit ordering form scala.Predef

  /* Potential implicits:
      - val/var
      - object
      - accessor methods = defs with no parentheses[Its very important]
        - if we change the above code to below, it'll work, but
            implicit def decrOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _ )
        - if we change it to this, then it'll not work. So no parentheses is important
            implicit def decrOrdering(): Ordering[Int] = Ordering.fromLessThan(_ > _ )
  */

  // Exercise
  case class Person(name: String, age: Int)
  val people = List (
    Person("Tom", 24),
    Person("Dik", 32),
    Person("Harry", 42)
  )
  implicit val peopleByName: Ordering[Person] = Ordering.fromLessThan((x, y) => x.name.compareTo(y.name) < 0)
  println(people.sorted)

  /* Implicit scope: places where compiler searches for implicit values
      - Local scope
      - imported scope
      - companions of all types involved = A, T or any supertype, ex:
          def sorted[B >: A](implicit ord: Ordering[B]): List
        If we define the above implicit inside object Abc { } it'll not be available but
        if we define it inside object Person it'll be a available since Person type in involved in Ordering.
  */
}
