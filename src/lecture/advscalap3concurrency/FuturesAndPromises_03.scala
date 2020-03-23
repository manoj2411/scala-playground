package lecture.advscalap3concurrency

import scala.concurrent.{Await, Future, Promise}
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

  /*    Block on Future and control them using Promise pattern    */
  // sample banking App

  case class User(name: String)
  case class Transaction(sender: String, receiver: String, amount: Double, status: String)
  import scala.concurrent.duration._
  object MyBankingApp {
    val name = "MyBankingApp"

    def fetchUser(name: String): Future[User] = Future {
      // simulating DB
      Thread.sleep(300)
      User(name)
    }

    def createTransaction(user: User, merchantName: String, amount: Double): Future[Transaction] = Future {
      // some process and checks on merchant
      Thread.sleep(700)
      Transaction(user.name, merchantName, amount, "Success")
    }

    def purchage(userName: String, item: String, merchantName: String, amount: Double): String = {
      // here we need resolve future and block
      // fetch user, validate and check balance
      // create transaction and wait for Transaction to finish
      val transactionF = for {
        user <- fetchUser(userName)
        transaction <- createTransaction(user, merchantName, amount)
      } yield transaction.status

      Await.result(transactionF, 2 second)
    }
  }
  val purchaseResponse = MyBankingApp.purchage("Manoj", "a Book", "Safari", 10)
  // println("Purchase status - " + purchaseResponse)

  /*    Promise - A way to manage future
  *       - separates the reading part of Future from Writing part
  */

  val promise = Promise[Int]()
  val future = promise.future
  // reading part
  future.onComplete {
    case Success(n) => println(s"[consumer] consuming $n")
  }

  // writing part
  val producer = new Thread(() => {
    println("[producer] processing heavy things ...")
    Thread.sleep(2000)
    println("[producer] pushing value  ...")
    promise.success(24)
  })

  producer.start()
}
