package lecture.part4pm

object PatternsEverywhere extends App {
  // big idea #1
  try {
    //code
  } catch {
    case ex: RuntimeException => "runtime"
    case npe: NullPointerException => "null pointer"
    case _ => "something else"
  }
  // catches are actually MATCHES

  // big idea #2 - generators are based on PM
  val aList = List(1,2,3,6)
  val evenOnes = for {
    x <- aList if x % 2 == 0
  } yield x * 10
  println(evenOnes)

  val tupleList = List((2,3), (4,2))
  val fileredTuples = for {
    (a,b) <- tupleList
  } yield a + b
  println(fileredTuples)

  // big idea #3 multiple value definitions based on PM
  val aTuple = (2,3,4)
  val (a,b,c) = aTuple
  val head :: tail = aList
  println(s"b: $b\nhead: $head\ntail: $tail")

  // big idea #4 - partial function are based on PM
  // might see this in weired syntax at many places
  val mappedList = aList.map {
    case v if v %2 == 0 => "Even"
    case 1 => "The one"
    case _ => "Rest of the world"
  }
  println(mappedList)
  // above expression is equivalent to this:
  val mappedList2 = aList.map { x => x match {
      case v if v %2 == 0 => "Even"
      case 1 => "The one"
      case _ => "Rest of the world"
    }
  }
  println(mappedList2)
}
