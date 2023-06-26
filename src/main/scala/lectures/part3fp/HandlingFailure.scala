package lectures.part3fp

import scala.util.{Failure, Random, Success, Try}

object HandlingFailure extends App {

  /*
  Exceptions are handled inside try-catch blocks
  try {
    val config: Map[String, String] = loadConfig(path)
  } catch {
    case : IOException => // handle IOException
    case : Exception => // handle other Exception
  }

  multiple/  nested try's make the code hard to follow
  we can't chain multiple operations prone to failure

  A Try is a wrapper for a computation that might fail or not
  sealed abstract class Try[+T]
  case class Failure[+T](t: Throwable) extends Try[T]  // wrap failed computations
  case class Success[+T](value: T) extends Try[T]     // wrap succeeded computations
   */


  // use Try to create success and failure explicitly

  val aSuccess = Success(3)
  val aFailure = Failure(new RuntimeException("SUPER FAILURE"))

  println(aSuccess)
  println(aFailure)

  def unsafeMethod(): String = throw new RuntimeException("No String for you buster")

  // try objects via the apply method
  val potentialFailure = Try(unsafeMethod()) // call the unsafeMethod with Try
  println(potentialFailure)

  // syntax sugar
  val anotherPotentialFailure = Try {
    // code that might throw
  }

  // utilities
  println(potentialFailure.isSuccess) // false

  // orElse
  def backupMethod(): String = "A valid result"
  val fallbackTry = Try(unsafeMethod()).orElse(Try(backupMethod()))  // how to use unsafe APIs
  println(fallbackTry)

  // if you desing the API - if you know that your code might throw an exception, wrap your computation in a try instead
  def betterUnsafeMethod(): Try[String] = Failure(new RuntimeException)
  def betterBackupMethod(): Try[String] = Success("A valid result")
  val betterFallback = betterUnsafeMethod() orElse betterBackupMethod() // more readable

  // if your code might return null, use Option
  // if your code moight return exception , use Try

  // try also have map, flatMap, filter

  println(aSuccess.map(_ * 2))  // Success(6)
  println(aSuccess.flatMap(x => Success(x * 10))) // Success(30)
  println(aSuccess.filter(_ > 10)) // Failure(java.util.NoSuchElementException: Predicate does not hold for 3)

  // => for-comprehensions

  /*
  Exercise
  */

  val host = "localhost"
  val port = "8080"
  def renderHTML(page: String) = println(page)

  class Connection {
    def get(url:String): String = {
      val random = new Random(System.nanoTime())
      if (random.nextBoolean()) "<html>...</html>"
      else throw new RuntimeException("Connection interrupted")
    }

    def getSafe(url: String): Try[String] = Try(get(url))

  }

  object HttpService {
    val random = new Random(System.nanoTime())

    def getConnection(host: String, port: String): Connection = {
      // someone else written
      if(random.nextBoolean()) new Connection
      else  throw new RuntimeException("Someone else took the port")
    }

    def getSafeConnection(host:String, port: String): Try[Connection] = Try(getConnection(host, port))
  }

  // print html code if you obtain the connection
  // if you get the html page from the conneection, print it to the console i.e. call renderHTML

  val possibleConnection = HttpService.getSafeConnection(host, port)
  val possibleHTML = possibleConnection.flatMap( connection => connection.getSafe("/home"))

  possibleHTML.foreach(renderHTML)  // <html>...</html>

  // shorthand version -> chain

  HttpService.getSafeConnection(host,port)
    .flatMap(connection => connection.getSafe("/home"))
    .foreach(renderHTML)

  // for-comprenhension

  for {
    connection <- HttpService.getSafeConnection(host, port)
    html <- connection.getSafe("/home")
  } renderHTML(html)

  /*
  java - imperative language
  try {
    connection = HttpService.getConnection(host, port)
    try {
      page = connection.get("/home")
      renderHTML(page)
    }catch (some other exception) {

    }
  }  catch (exception) {
  }
  */

  /*
  Use Try to handle exceptions gracefully  :
  avoid runtime crashes due to uncaught exceptions
  avoid an endless amount of try-catches (spagetti code)
  Try -> A functional way of dealing with failure

  map, flatMap, filter
  orElse
  other : fold, collect, toList, conversion to Options

  if you desing a method to return a (some type) but may throw an exception,
  return a Try[that type] instead

  */

}
