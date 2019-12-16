package lecture.advscalap1

import scala.util.Try

// 01
object DarkSugars_01 extends App {
    // 1. Methods with single param: We can call single ARG method with curly braces instead of calling method with a argument in parentheses.

    val tryInstance = Try {
        // ...
        throw new NoSuchElementException
    }
    // Thats actually the apply() method from Try and applied with the argument returned from the block

    List(2,4,5).map { x =>
      x * 10
    }


    // 2. Single abstract method pattern: Instances of trait with a single method can be reduced to lambdas.
    trait Action {
      def act(x: Int): Int
    }
    val funkyInstance: Action = (x: Int) => x * 10
    // This pattern also works for classes that have some methods implemented but only have one method unimplemented
    // another example: Runnable
    val aThread = new Thread(new Runnable {
        override def run(): Unit = println("inside a thread!") // Only unimplemented method
    }) // This is Java way
    val anotherThread = new Thread(() => println("another thread!")) // This is Scala way

    // 3. :: & #:: methods are special bcz ends with colan(:)
    val prependedList = 2 :: List(8, 6) // => List(8, 6).::(2) .
    // Scala spec: last character decides associativity of the method. Ex: method ends with : will be right associative
    class MyStream[T] {
      def -->:(value: T): MyStream[T] = this
    }
    val myStream = 1 -->: 2 -->: 4-->: new MyStream[Int] // The -->: operator here is right associative, ends with :


    // 4. Multi word method naming.
    class AGirl(name: String) {
      def `and then said`(gossip: String) = s"$name said $gossip"
    }
    val lilly = new AGirl("lilly")
    lilly `and then said` "Scala is awesome"


    // 5. Infix types
    class Composite[A, B]
    val composite: Int Composite String = ???

    class -->[A, B]
    val towards: Int --> String = ???

    // 6. update() is a very special method, much like apply()
    val anArray = Array(2,4,5)
    anArray(2) = 8 // => anArray.update(2, 8)
    // Used in mutable collections. REMEMBER: Its special like apply()

    // 7. Setters for mutable containers
    class MyMutable {
      private var myMember = 0
      def member = myMember // getter
      def member_=(value: Int) =
        myMember = value // setter
    }
    val myMutable = new MyMutable
    myMutable.member = 10 // => myMutable.member_=(10)
}

