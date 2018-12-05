package lecture.part3fp

object TuplesAndMaps extends App {
  // Tuples - finite ordered "lists"
  val aTuple = new Tuple2(24, "Hello world!") // Tuple2[Int, String] == (Int, String)
  val anotherTuple = ("Hey", 24) // shorthand

  // NOTE: Its can group atmost 22 elements of different types
  println(aTuple._2)
  println(aTuple.copy(_2 = "Tada!"))
  println(anotherTuple.swap)

  // Maps - key -> value
  val aMap: Map[String, Int] = Map()
  val phoneBook = Map(("tom", 241), "jerry" -> 1124) // "jerry" -> 1124 is syntactic sugar
  println(phoneBook)
  println(phoneBook.map(pair => pair._1.toUpperCase -> pair._2))
  println(phoneBook.filter(pair => pair._2 % 2 == 0))
  println(phoneBook.filterKeys(key => key.contains('m')))
  println(phoneBook.mapValues(v => v + "-**"))
  println(phoneBook.keys.groupBy(name => name.charAt(0)))
}
