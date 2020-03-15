package lecture.advscalap3concurrency

import scala.concurrent.Future
import scala.util.{Failure, Random, Success}
import scala.concurrent.ExecutionContext.Implicits.global

object FuturesAndPromises_03 extends App {

  /*   Design mini social network
        - everything needs to be async
  */

  case class Profile(id: String, name: String) {
    def poke(anotherProfile: Profile) = println(s"$name is poking ${anotherProfile.name}")
  }

  object SocialNetwork {
    // database
    val names = Map(
      "fb.id.1.m" -> "Mark",
      "fb.id.2.b" -> "Bill",
      "fb.id.0.d" -> "Dummy"
    )
    val friends = Map(
      "fb.id.1.m" -> "fb.id.2.b"
    )
    val random = new Random()

    // API
    def fetchProfile(id: String): Future[Profile] = Future {
      Thread.sleep(random.nextInt(500))
      Profile(id, names(id))
    }

    def fetchBestFriend(friend: Profile): Future[Profile] = Future {
      Thread.sleep(random.nextInt(400))
      val friendId = friends(friend.id)
      Profile(friendId, names(friendId))
    }
  }
  // On client side - we want Mark to Poke Bill
  //  val markF = SocialNetwork.fetchProfile("fb.id.1.m")
  //  markF.onComplete {
  //    case Success(mark) =>
  //      val bill = SocialNetwork.fetchBestFriend(mark)
  //      bill.onComplete {
  //        case Success(marksFriend) => mark.poke(marksFriend)
  //        case Failure(ex) => println("Error occurred fetching best friend " + ex)
  //      }
  //    case Failure(ex) =>
  //      println("Error occurred fetching profile " + ex)
  //  }
  /*    There are problems in the above code, its "ugly and unreadable"
        -  Only mark.poke(marksFriend) doing the real things
        - This is adding tech debt, complex code, deep nesting of onComplete
        - val bill is nested inside, not available outside

       There are better ways to do it - Functional composition of Future
       - map
       - flatMap
       - filter
       USE for-comprehension for nested async code to simplify it.
  */
  for {
    mark <- SocialNetwork.fetchProfile("fb.id.1.m")
    bill <- SocialNetwork.fetchBestFriend(mark)
  } mark.poke(bill)


  /*    Add fallback to future to handle failures like key not found exception  */
  val aFallbackProfile: Future[Profile] = SocialNetwork.fetchProfile("fb.id.1.a").recover {
    case ex: Throwable =>
      println("Falling back because of Error - " + ex)
      Profile("fb.id.0.d", "Dummy") // recover Returns Profile here, not future. Its like map
  }
  val bFallbackProfile: Future[Profile] = SocialNetwork.fetchProfile("fb.id.1.a").recoverWith {
    case ex: Throwable =>
      println("Falling back because of Error - " + ex)
      SocialNetwork.fetchProfile("fb.id.0.d") // recoverWith Returns Future[Profile] here, not future. Its like flatMap
  }
  val cFallbackProfile: Future[Profile] = SocialNetwork.fetchProfile("fb.id.1.a").fallbackTo {
      SocialNetwork.fetchProfile("fb.id.0.d") // recoverWith Returns Future[Profile] here, not future. Its like flatMap
  }

  Thread.sleep(2000)
}
