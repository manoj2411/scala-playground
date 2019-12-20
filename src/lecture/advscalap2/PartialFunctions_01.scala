package lecture.advscalap2

import scala.io.Source

object PartialFunctions_01 extends App {
  val aFunction = (x: Int) => x * 10
  // Sometimes you may want some functions that only accept certain values, we want our function to not accept any other values other than 1, 2 & 5.
  // Possible solutions: Add if, else if conditions OR add cases using PM.
  val nicerFuzzyFn = (x: Int) => x match {
    case 1 => 100
    case 2 => 120
    case 5 => 500
  }

  // more cleaner way:
  val aPartialFn: PartialFunction[Int, Int] = {
    case 1 => 100
    case 2 => 120
    case 5 => 500
  } // This will work bcz PartialFunctions are actually based on PM
  println(aPartialFn(2))
  // println(aPartialFn(4)) //- This will crash because of MatchError

  // Utilities of PF:
  println(aPartialFn.isDefinedAt(11))
  // PFs can be lifted to total functions to returning Options
  val lifted = aPartialFn.lift // Its a useful method to transform PF to total Fn
  println(lifted(5))
  println(lifted(24))
  // We can add fallback PF using orElse like:
  aPartialFn.orElse[Int, Int] {
    case 24 => 1
  }

  // Partial Functions are subtype of total functions - PF extends normal function, that why...(next line)
  // HOFs excepts PFs as well
  val aMappedList = List(2,4,5).map {
    case 2 => 20
    case 4 => 44
    case 5 => 100
  }
  println(aMappedList)

  // Note: Unlike functions which can have multiple parameters a partial fn can only have one parameter type

  /* Exercise
      1. A small dumb chatbot as PF
  * */
  val smallChatbot : PartialFunction[String, String] = {
    case "hi" => "Hey there, how can I help you?"
    case "bye" => "Why, there is no way to out, hahaha..."
  }
  scala.io.Source.stdin.getLines.map(smallChatbot).foreach(println)

}
