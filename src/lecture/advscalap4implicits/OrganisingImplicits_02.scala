package lecture.advscalap4implicits

object OrganisingImplicits_02 extends App {
  /*      How compiler looks for implicits and where, and its priorities   */

  implicit val decrOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _ )
//  print(List(2,4,1,5,3).sorted) // takes implicit ordering form scala.Predef

  /* Potential implicits (used as implicit parameters):
      - val/var
      - object
      - accessor methods: defs with no parentheses[Its very important]
        - if we change the above code to below, it'll work, but
            implicit def decrOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _ )
        - if we change it to this, then it'll not work. So no parentheses is important
            implicit def decrOrdering(): Ordering[Int] = Ordering.fromLessThan(_ > _ )
  */

  // Exercise
  case class Person(name: String, age: Int)
  val people = List (Person("Tom", 24), Person("Don", 32), Person("Harry", 42))
  object Person {
    implicit val peopleByName: Ordering[Person] = Ordering
      .fromLessThan((x, y) => x.name.compareTo(y.name) < 0)
  }
  //  println(people.sorted) // FAILING : we didn't defined any implicit ordering Person

  /* Implicit scope: places where compiler searches for implicit values
      - Local scope
      - imported scope
      - companions of all types involved in method signature
        A, T or any supertype, ex:
          def sorted[B >: A](implicit ord: Ordering[B]): List
        In our case, compiler looks for List, Ordering, Person companion objects.

        If we define the above implicit inside object Abc { } it'll not be available but
        if we define it inside object Person it'll be a available since Person type in involved in Ordering.

   Best practices:
    - If there are many possible values and a GOOD one then define it in companion object, will be picked as default.
    - If multiple good values, then place them in separate containers and let user import it, ex:
  */
  object AlphabeticNameOrdering {
    implicit val alphabeticOrdering: Ordering[Person] = Ordering
      .fromLessThan((x, y) => x.name.compareTo(y.name) < 0)
  }
  object AgeOrdering {
    implicit val ageOrdering: Ordering[Person] = Ordering.fromLessThan((x, y) => x.age < y.age)
  }

  import AgeOrdering._
  //  println(people.sorted)

  /*
    Exercise: define orderings

      - totalPrice (mostly used)
      - by unit count
      - by price
  */
  case class Purchase(nUnits: Int, unitPrice: Double) {
    val totalPrice = nUnits * unitPrice
  }

  object Purchase {
    implicit val totalPriceOrdering: Ordering[Purchase] = Ordering
      .fromLessThan((x, y) => x.totalPrice > y.totalPrice)
  }

  object PriceOrdering {
    implicit val priceOrdering: Ordering[Purchase] = Ordering.fromLessThan((x,y) => x.unitPrice > y.unitPrice)
  }

  object UnitsOrdering {
    implicit val unitOrdering: Ordering[Purchase] = Ordering.fromLessThan((x,y) => x.nUnits > y.nUnits)
  }
  val purchases = List(Purchase(10, 4), Purchase(15, 3), Purchase(7, 10), Purchase(20, 2))

  import PriceOrdering._
  println(purchases.sorted)

}
