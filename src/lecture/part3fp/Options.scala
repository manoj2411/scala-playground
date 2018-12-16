package lecture.part3fp

import scala.util.Random

object Options extends App {
  val unsafeOption = Option(null)

  // functions on Options
  println(unsafeOption.isEmpty)
  println(unsafeOption)
  //println(myOption.get) // UNSAFE - DO NOT USE THIS

  val myOption = Some(24)
  // map, flatMap, filter
  println(myOption.map(x => x * 10))
  println(myOption.filter(x => x < 100))
  println(myOption.flatMap(x => Option(x * 5)))


  /* Exercise:
    - We have a config which fetches their values from unreliable api. We have a Connection class which
    may or may not return connection.
    We need to try to get a connection object and print connection if we get connection
  */

  val config = Map("host" -> "local", "port" -> "1234")
  class Connection {
    def connected = "connected"
  }
  object Connection {
    val random = new Random(System.nanoTime())
    def apply(host: String, port: String): Option[Connection] =
      if (!random.nextBoolean()) Some(new Connection)
      else None
  }

  val host = config.get("host")
  val port = config.get("port")

  val conn = host.flatMap(h => port.flatMap(p => Connection(h, p)))
  println(conn.map(x => x.connected).getOrElse("Not connected!"))
  //conn.map(x => x.connected).foreach(println)

}
