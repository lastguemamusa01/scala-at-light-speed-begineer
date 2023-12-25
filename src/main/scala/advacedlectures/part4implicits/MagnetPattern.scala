package advacedlectures.part4implicits

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object MagnetPattern extends App {

  // magnet pattern - is a use case of type classes, which aims at solving some of the problems created
  // by method overloading


  // method overloading

  class P2PRequest
  class P2PResponse
  class Serializer[T]
  trait Actor {
    def receive(statusCode: Int): Int
    def receive(request: P2PRequest): Int
    def receive(response: P2PResponse): Int
    def receive[T: Serializer](message: T): Int // context bound
    def receive[T: Serializer](message: T, statusCode: Int): Int
    def receive(future: Future[P2PRequest]): Int // asyncronously

    // def receive(future: Future[P2PResponse]): Int //  type erasure -> this cannot compiled

    // lots of overloads
  }

  /*
  problems
  1 - type erasure
  2 - lifting doesn't work for all overloads -> if we want some higher order functions
  3- code duplication - implimenting all them, cause some duplication. because they are very similar
  4 - some limitations with default arguments and type inference on arguments -> type inference and default args
   */

  // val receiveFV = receive _ // _ is waht -> statusCode or request or response, compiler is confuse ?! -> lifting doesn't work

  // actor.receive() // -> receiver(!?) -> what will be the default arguments -> status code or request or response

  // this api can be written

  // magnet type class
  trait MessageMagnet[Result] {
    def apply(): Result
  }

  def receive[R](magnet: MessageMagnet[R]): R = magnet()

  implicit class FromP2PRequest(request: P2PRequest) extends MessageMagnet[Int] {
    def apply(): Int = {
      // logic for handling a P2PRequest
      println("Handling P2P request")
      42
    }
  }

  implicit class FromP2PResponse(request: P2PResponse) extends MessageMagnet[Int] {
    def apply(): Int = {
      // logic for handling a P2PResponse
      println("Handling P2P response")
      24
    }
  }

  receive(new P2PRequest)
  receive(new P2PResponse) // implicit conversion is applied

  /*
  benefits
  1 - no more type erasure problems - this fixed because the compiler looks for implicit conversions before the types are erased
  */

  implicit class FromResponseFuture(future: Future[P2PResponse]) extends MessageMagnet[Int] {
    override def apply(): Int = 2
  }

  implicit class FromRequestFuture(future: Future[P2PRequest]) extends  MessageMagnet[Int] {
    override def apply(): Int = 3
  }

  println(receive(Future(new P2PRequest)))
  println(receive(Future(new P2PResponse)))

 // 2 - lifting works

  trait MathLib {
    def add1(x: Int): Int = x + 1
    def add1(s: String): Int = s.toInt + 1
    // add1 overloads
  }

  // magnetize

  trait AddMagnet {  // need to omit [type] - to use lifting functions
    def apply(): Int
  }
  def add1(magnet: AddMagnet): Int = magnet()

  // implicit conversions

  implicit class AddInt(x: Int) extends AddMagnet {
    override def apply(): Int = x + 1
  }

  implicit class AddString(s: String) extends  AddMagnet {
    override def apply(): Int = s.toInt + 1
  }

  val addFV = add1 _  // lift high order functions
  println(addFV(1))
  println(addFV("3"))

 /*
 Drawbacks of using magnet
 1 - verbose
 2- harder to read
 3- you can't name or place default arguments
 4 - call by name doesn't work correctly - for debugging purpose
 (exercise: prove it) - side effects
  */

  // receive() // 3  - error

  class Handler {
    def handle(s: => String) = { // => call by name
      println(s)
      println(s)
    }
    // other overloads
  }

  // magnetize this api
  trait HandleMagnet {
    def apply(): Unit
  }

  def handle(magnet: HandleMagnet) = magnet()

  implicit class StringHandle(s: => String) extends HandleMagnet {
    override def apply(): Unit = {
      println(s)
      println(s)
    }
  }

  def sideEffectMethod(): String = {
    println("Hello, Scala")
    "hahaha "
  }

  // handle(sideEffectMethod())
  handle {
    println("Hello, Scala")
    "hahaha " // only this is converted with magnet -> compiler convert to new StringHandle("magent")
  }

}
