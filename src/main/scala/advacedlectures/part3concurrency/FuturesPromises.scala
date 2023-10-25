package advacedlectures.part3concurrency

import scala.concurrent.Future
import scala.util.{Failure, Random, Success}
// important for futures, for ExecutionContext error
// this implicitly inject, ExecutionContext handles thread allocation of Futures
import scala.concurrent.ExecutionContext.Implicits.global
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




}
