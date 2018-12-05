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
  println((phoneBook ++ Map("Tom" -> 112233)) .map(pair => pair._1.toUpperCase -> pair._2))
  println(phoneBook.filter(pair => pair._2 % 2 == 0))
  println(phoneBook.filterKeys(key => key.contains('m')))
  println(phoneBook.mapValues(v => v + "-**"))
  println(phoneBook.keys.groupBy(name => name.charAt(0)))

  /* Exercise:
      1. Define overly simplified social network based on Maps
        Person - have name
        Network - will keep an association b/w each name and list of friends
          - Add a person to the network
          - Remove a person
          - Make friend (mutual)
          - Unfriend
        Stats
          - No. of friends of a Person
          - Person with most friends
          - How many people with no friends
          - If there is a social connect b/w 2 people (direct or not)
  */


    def add(network: Map[String, Set[String]], name: String): Map[String, Set[String]] =
      network + (name -> Set())
    def friend(network: Map[String, Set[String]], person1: String, person2: String): Map[String, Set[String]] = {
      val friendsOfA = network(person1) + person2
      val friendsOfB = network(person2) + person1
      network + (person1 -> friendsOfA) + (person2 -> friendsOfB)
    }
    def unfriend(network: Map[String, Set[String]], person1: String, person2: String): Map[String, Set[String]] = {
      val friendsOfA = network(person1) - person2
      val friendsOfB = network(person2) - person1
      network + (person1 -> friendsOfA) + (person2 -> friendsOfB)
    }
    def remove(network: Map[String, Set[String]], person: String): Map[String, Set[String]] = {
      def removeHelper(list: Set[String], accNetwork: Map[String, Set[String]]): Map[String, Set[String]] =
        if (list.isEmpty) accNetwork
        else removeHelper(list.tail, unfriend(accNetwork, person, list.head))

      val unfriended = removeHelper(network(person), network)
      unfriended - person
    }

    val emptyNetwork: Map[String, Set[String]] = Map()
    var network = add(add(add(emptyNetwork, "tom"), "jerry"), "spike")
    network = friend(network, "tom", "jerry")
    network = friend(network, "spike", "jerry")
    println(network)
    println(unfriend(network, "tom", "jerry"))

    def friendsCount(network: Map[String, Set[String]], name: String): Int = network(name).size
    println("jerry's friends: " + friendsCount(network, "jerry"))

    def havingMostFriends(network: Map[String, Set[String]]): String =
      network.maxBy(_._2.size)._1
    println(havingMostFriends(network) + " is having most friends")

    def havingNoFriends(network: Map[String, Set[String]]): Int =
      //network.filter(_._2.isEmpty).size
      network.count(_._2.isEmpty)
    println("Nnumber of people having no friends: " +
      havingNoFriends(unfriend(network, "jerry", "spike")))

}
