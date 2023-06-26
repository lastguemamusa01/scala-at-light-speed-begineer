package lectures.part3fp

import scala.util.Random

object Options extends App {

  /*
  Tony Hoare -> The Billion Dollar Mistake -> invent of null pointer
  if the string is null, string.length -> this will crash with null pointer exceptions
  // working with null values leads to boiler plate code to guard
  if(string != null) {
    println(string.length)
  }

  Option is a wrapper for a value that might be present or not
  Some wraps a concrete value
  None is a singlethon for absent values

  map.get -> Some(value) or None - map uses Option
  list.find -> Some(1) -> list also use Option.
  lots of functions on all collections work with options

   */

  val myFirstOption: Option[Int] = Some(4)
  val noOption: Option[Int] = None
  println(myFirstOption)
  println(noOption)

  // option invented for manage unsafe APIs
  // work or use unsafe APIs
  def unsafeMethod(): String = null  // write method return string, but something happen and the issue is return null

  // val result = Some(unsafeMethod()) // wrong -> Some(null) x -> Some always need valid value

  // instead use option companions apply method,
  val result = Option(unsafeMethod()) // apply method from the companion object Option would take care to build Some or None
  // depending the value of unsafeMethod method is null or not
  println(result) // None

  // we use Option - we should never do null checks ourselves, option type will do this for us

  // use options in chained methods
  def backupMethod(): String = "A valid result" // safe Api
  val chainedResult = Option(unsafeMethod()).orElse(Option(backupMethod())) // case when unsafeMethod return null,
  // use backupMethod instead of unsafeMethod

  // Design unsafe APIs -> better design of API
  // make your methods return option of something in case of returning nulls

  def betterUnsafeMethod(): Option[String] = None
  def betterBackupMethod(): Option[String] = Some("A valid result")
  val betterChainedResult = betterUnsafeMethod() orElse betterBackupMethod()

  // functions on Options
  println(myFirstOption.isEmpty)
  // println(myFirstOption.get) // unsafe ->  trying to access to null pointer, do not use this

  // map, flatMap, filter

  println(myFirstOption.map(_ * 2)) // Some(8)
  println(myFirstOption.filter(x => x > 10)) // None, because 4 not match the predicate or is not > 10
  println(myFirstOption.flatMap(x => Option(x * 10 ))) // 40

  // for-comprehensions
  /*

   */

  // this is written someone else, this is fetched from elsewhere
  val config: Map[String, String] = Map(
    // fetch from elsewhere -> config file
    "host" -> "176.45.36.1",
    "port" -> "80"
  )

  // written by someone else
  class Connection {
    def connect = "connected" // reality this connect to some server
  }

  // companion object
  // written by someone else
  object Connection {
    val random = new Random(System.nanoTime())
    // simulate fault connection
    def apply(host: String, port: String): Option[Connection]  =
      if(random.nextBoolean()) Some(new Connection)
      else None
  }

  // try to establish a connection, if so - print the connect method

  val host = config.get("host") // Option of string
  val port = config.get("port") // get is unsafe
  // use functionals
  /*
  if(h != null)
    if(p != null)
      return Connection.apply(h, p)

  return null
   */
  val connection = host.flatMap( h => port.flatMap(p => Connection.apply(h, p)))  // Option[Connection]
  /*
  if(c != null)
    return c.connect
  return null
   */
  val connectionStatus = connection.map(c => c.connect) // Option[String]

  // if (connectionStatus == null) println(None) else print(Some(connectionStatus.get))
  println(connectionStatus) // random number - could be None and connected
  /*
   if(status != null)
    println(status)
   */
  connectionStatus.foreach(println)

  // another solution -> chained call

  config.get("host")
    .flatMap(host => config.get("port")
      .flatMap(port => Connection(host,port))
        .map(connection => connection.connect))
    .foreach(println)


  // for-comprehensions - much more readable

  val forConnectionStatus = for {
    host <- config.get("host") // given host from config.get
    port <- config.get("port")
    connection <- Connection(host, port) // given connection from Connection of host and port as parameter
  } yield connection.connect

  forConnectionStatus.foreach(println)


}
