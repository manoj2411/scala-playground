package lecture.part3fp

import scala.util.{Failure, Random, Success, Try}

object HandlingFailure extends App {
  val _success = Success(24)
  val _failure = Failure(new RuntimeException("My exception!"))
  println(_success)
  println(_failure)

  def unsafeFun = throw new RuntimeException("Another failure!")
  // Try objects via apply method. The program will not crash here, it'll be success or failure.

  val potentialFailure = Try(unsafeFun)
  println(potentialFailure)
  val anotherFailure = Try {
    // code here
  }

  // utilities
  println(potentialFailure.isSuccess)
  println(potentialFailure.isFailure)
  // orElse, map, flatMap, filter
  println(_success.map(_ / 2))
  println(_success.flatMap(x => Success(x * 5)))
  println(_success.filter(_ > 100))


  // orElse : lets say you have an unsafe api and a backup method for that, you can use orElse
  def backupMethod = "a valid result"
  val fallbackTry = Try(unsafeFun).orElse(Try(backupMethod))
  println(backupMethod)


  // Exercise
  val host = "localhost"
  val port = "3000"
  def renderHtml(html: String) = println(html)

  class Connection {
    def get(url: String): String = {
      val random = new Random(System.nanoTime)
      if (random.nextBoolean) "<doctype abc/><html>...</html>"
      else throw new RuntimeException("connection broken!")
    }
    def getSafe(url: String): Try[String] = Try(get(url))
  }
  object HttpService {
    val random = new Random(System.nanoTime)

    def getConnection(host: String, port: String): Connection =
      if (random.nextBoolean) new Connection
      else throw new RuntimeException("port already in use!")
    def getConnectionSafe(host: String, port: String): Try[Connection] = Try(getConnection(host, port))
  }
  // if you get the html page from the connection , print it i.e. call renderHtml
  HttpService.getConnectionSafe(host, port)
    .flatMap(c => c.getSafe("/"))
    .foreach(renderHtml)

}
