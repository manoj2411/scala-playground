package lecture.part3fp

object TuplesAndMaps extends App {
  // Tuples - finite ordered "lists"
  val aTuple = new Tuple2(24, "Hello world!") // Tuple2[Int, String] == (Int, String)
  val anotherTuple = ("Hey", 24) // shorthand

  // NOTE: Its can group atmost 22 elements of different types
  println(aTuple._2)
  println(aTuple.copy(_2 = "Tada!"))
  println(anotherTuple.swap)
}
