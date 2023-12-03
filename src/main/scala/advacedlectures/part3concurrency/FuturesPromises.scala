package advacedlectures.part3concurrency

import scala.concurrent.{Await, Future, Promise}
import scala.util.{Failure, Random, Success, Try}
// important for futures, for ExecutionContext error
// this implicitly inject, ExecutionContext handles thread allocation of Futures
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object FuturesPromises extends App {

  // asynchronous functional programming
  // futures are a functional way of computing something in parallel or on another thread

  def calculateMeaningOfLife: Int = {
    Thread.sleep(2000)
    42
  }

  // future object
  val aFuture = Future {
    // create a future object, a future instance by calling the apply method
    // from the companion object of the future trait
    calculateMeaningOfLife // calculates the meaning of life on another thread
  } // (global) which is passed by the compiler

  // java Future
  // println(aFuture.value) // return None -> Option[Try[Int]]   None is Option

  println("Waiting on the future")
  aFuture.onComplete {  // t is try  t => t match this could be partial function so we could remove it
    case Success(meaningOfLife) => println(s"the meaning of life is $meaningOfLife")
    case Failure(e) => println(s"I have failed with $e") // e is exception
  } // callback is done by some thread

  Thread.sleep(3000)

  // mini social network

  case class Profile(id: String, name: String) {
    def poke(anotherProfile: Profile) =
      println(s"${this.name} poking ${anotherProfile.name}")
  }

  object SocialNetwork { // singleton object, no need to instantiate
    // database
    val names = Map(
      "fb.id.1-zuck" -> "Mark",
      "fb.id.2-bill" -> "Bill",
      "fb.id.0-dummy" -> "Dummy"
    )

    val friends = Map(
      "fb.id.1-zuck" -> "fb.id.2-bill"
    )

    // computation simulate
    val random = new Random()

    // API
    def fetchProfile(id: String): Future[Profile] = Future {
      // simulate fetching from the DB
      Thread.sleep(random.nextInt(300)) // 300 milliseconds max
      Profile(id, names(id))
    }

    def fetchBestFriend(profile: Profile): Future[Profile] = Future {
      Thread.sleep(random.nextInt(400))
      val bfId = friends(profile.id)
      Profile(bfId, names(bfId))
    }
  }

  // client app : mark to poke bill
  // nested Futures - ugly and unreadable
  // banking sysmte or online shopping will be very complicated using nested Futures
  // this causa incredible technical debt
  val mark = SocialNetwork.fetchProfile("fb.id.1-zuck")
//  mark.onComplete{
//    case Success(markProfile) => {
//      val bill = SocialNetwork.fetchBestFriend(markProfile)
//      bill.onComplete{
//        case Success(billProfile) => markProfile.poke(billProfile)
//        case Failure(e) => e.printStackTrace()
//      }
//    }
//    case Failure(ex) => ex.printStackTrace()
//  }



  // funcional compisition of futures -> better way to do instead of using nested futures
  // map, flatMap, filter
  // map transforms a feature of a given type into a feature of a different type
  val nameOnTheWall = mark.map(profile => profile.name) // turn future of profile to future of string

  // flatMap -> you can obtain from the Success of bill another future
  // flatmap transform from the first profile Success to convert to next another profile
  val marksBestFried = mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile))

  // filter , exception will be NoSuchElement Exception, convert from future of profile filtering and returning
  // another profile filtered
  val zucksBestFriendRestricted = marksBestFried.filter(profile => profile.name.startsWith("Z"))

  // for-comprehensions
  // this is so much cleaner
  for {
    mark <- SocialNetwork.fetchProfile("fb.id.1-zuck")
    bill <- SocialNetwork.fetchBestFriend(mark)
  } mark.poke(bill)

  Thread.sleep(1000)

  // fallbacks - one fallback path is called recovery
  // we would like to recover our future with a dummy profile in case there is an exception or
  // a throwable being thrown inside in original future
  val aProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown id").recover { // partial function
    case e: Throwable => Profile("fb.id.0-dummy","Forever alone")
  }

  // recover with feching to Profile with existing dummy profile
  val aFetchedProfileNoMatterWhat =SocialNetwork.fetchProfile("unknown id").recoverWith {
    case e: Throwable => SocialNetwork.fetchProfile("fb.id.0-dummy")
  }

  // falling back -> fallback to another future
  // if the first future success then it return the value
  // if the first future fails, second future return the value
  // if the second future also fails, the exception of the first future will be returned
  val fallbackResult = SocialNetwork.fetchProfile("unknown id").fallbackTo(SocialNetwork.fetchProfile("fb.id.0-dummy"))

  // Use blocks for like bank transactions, that need to be completed something before to proceed

  // online banking app -> api
  case class User(name: String)
  case class Transaction(sender: String, receiver: String, amount: Double, status: String)

  object BankingApp {
    val name = "Rock the JVM banking"

    def fetchUser(name: String): Future[User] = Future {
      // simulate fetching from the DB
      Thread.sleep(500) // half of seconds
      User(name)
    }

    def createTransaction(user: User, merchantName: String, amount: Double): Future[Transaction] = Future {
      // using Future for asynchronous computations
      // simulate some processes
      Thread.sleep(1000)
      Transaction(user.name, merchantName, amount, "SUCCESS")
    }

    def purchase(username: String, item: String, merchantName: String, cost: Double): String = {
      // fetch the user from the DB
      // create a transaction
      // WAIT for the transaction to finish
      val transactionStatusFuture = for {
        user <- fetchUser(username)
        transaction <- createTransaction(user, merchantName, cost)
      } yield  transaction.status

      Await.result(transactionStatusFuture, 2.seconds) // 2.seconds added in class this import scala.concurrent.duration._
      // implicit conversions -> pimp my library -> 2.seconds

    }
  }

  println(BankingApp.purchase("Daniel","iphone 12", "rock the jvm store", 3000)) // SUCCESS

  // promises -> manual manipulation of futures with promises

  // Futures - are the functional way of composing non-blocking computations,  which will return at some point

  val promise = Promise[Int]() // promise is some sort of controller over a future
  val future = promise.future

  // thread 1 - "consumer" - the consumer knows how to handle the future
  future.onComplete {
    case Success(r) => println("[consumer] I've received " + r)
  }

  // thread 2 - "producer"
  val producer = new Thread(() => {
    println("[producer] crching numbers...")
    Thread.sleep(500)
    // "fullfilling" the promise
    promise.success(42) // basically manuplates the internal future to complet with a successful value 42]

    // promise.failure()
    println("[producer] done")
  })


  producer.start()
  Thread.sleep(1000)

  /*
  1 ) fulfill a future Immediately with a value
  2 ) inSequence(fa, fb) - fa - future A need to finish after, to sequently continue with fb - future b
  3 ) first(fa, fb) => new future with the first value of the two futures
  4 ) last(fa, fb) => new future with the last value
  5 ) retryUntil[T](action: () => Future[T], condition: T => Boolean): Future[T] - repeat the Future[T], until the
  first condition met, then return Future[T]
   */


  // 1 - fulfill immediately

  def fulfillImmendiately[T](value: T): Future[T] = Future(value)

  // 2 - inSequence

  def inSequence[A, B](first: Future[A], second: Future[B]): Future[B] =
    first.flatMap(_ => second) // first is finished with flatMap, run the second future, sequence

  // 3 - first out of 3 futures

  def first[A](fa: Future[A], fb: Future[A]): Future[A] = {
    val promise = Promise[A] // promise is the controller of a future of type A

    def tryComplete(promise: Promise[A], result: Try[A]) = result match {
      case Success(r) => try {
        promise.success(r)
      } catch {
        case _ =>
      }
      case Failure(t) => try {
        promise.failure(t)
      } catch {
        case _ =>
      }
    }

    //fa.onComplete(result => tryComplete(promise, result)) // another way to more concise (tryComplete(promise, _))
    fa.onComplete(promise.tryComplete)
    fb.onComplete(promise.tryComplete(_)) // more concise promise.tryComplete, this is api method tryComplete

    promise.future
  }

  // 4 - last out of the 2 futures

  def last[A](fa: Future[A], fb: Future[A]): Future[A] = {
    // 1 promise which both futures will try to complete
    // 2 promise which the last future will complete

    val bothPromise = Promise[A]
    val lastPromise = Promise[A]
    val checkAndComplete = (result: Try[A]) =>
      if (!bothPromise.tryComplete(result))
        lastPromise.complete(result)

    fa.onComplete(checkAndComplete)
    fb.onComplete(checkAndComplete)

    lastPromise.future

  }


  val fast = Future {
    Thread.sleep(100)
    42
  }

  val slow = Future {
    Thread.sleep(200)
    45
  }

  first(fast, slow).foreach(f => println("first: " + f)) // 42
  last(fast, slow).foreach(l => println("last: " + l))  // 45

  Thread.sleep(1000)

  // 5 - retry until

  def retryUntil[A](action: () => Future[A], condition: A => Boolean): Future[A] =
    action()
    .filter(condition)
    .recoverWith {
      case _ => retryUntil(action, condition)
    }

  val random = new Random()
  val action = () => Future {
    Thread.sleep(100)
    val nextValue = random.nextInt(100)
    println("generated " +nextValue)
    nextValue
  }

  retryUntil(action, (x: Int) => x < 10).foreach(result => println("settled at " + result))

  Thread.sleep(10000)

}
