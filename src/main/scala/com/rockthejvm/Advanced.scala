package com.rockthejvm

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global

object Advanced extends App {

  // lazy evaluation - means that an expression is not evaluated until it is first use.
  lazy val aLazyValue = 2
  // code block -> return the last element
  // if the code block remove lazy, this value will be printed
  // because this value was evaluated
  lazy val lazyValueWithSideEffect = {
    println("I am so very lazy!")
    43
  }
  // here we are using first time the lazy value , so this going to print I am so lazy!
  val eagerValue = lazyValueWithSideEffect+1

  // useful in infinite collections and some rare use cases
  // pseudo-collections: Option, Try - they are own types, not collection themselves
  // Option try - used for unsafe method
  def methodWhichCanReturnNull(): String = "hello, scala"
  // guard your self for nulls
  /*
  defensive code
  if(methodWhichCanReturnNull() == null) {
     defensive code against null
  }
  */

  val anOption = Option(methodWhichCanReturnNull()) // if Option cotains element, is Some("hello, scala")
  // if is null, this will be None(Singleton object)
  // Some is subtype of the option abstract type
  // Option -> Some or None, None equivalent null, except None is regular value
  // None -> There is no risk in accessing illegal members or methods
  // option = "a collection of one element at most", collect witch contains at most one element

  // there are no null checks to add for defensive code like java
  val stringProcessing =anOption match {
    case Some(string) => s"i have obtained a valid string:$string"
    case None => "I obtained nothing"
  }

  // try -> guards against methods which can throw expections

  def methodWhichCanThrowException(): String = throw new RuntimeException
  // this could be complex in huge code base
  try {
    methodWhichCanThrowException()
  } catch {
    case e: Exception => "defend against this evil exception"
  }

  // try can wrap something that can throw an exception
  val aTry = Try(methodWhichCanThrowException()) // Try object containing either String or Exception
  // a try = "collection" with eaither a value if the code wen well, or an exception if the code threw one

  // pattern matching
  val anotherStringProcessing = aTry match {
    case Success(validValue) => s"I have obtained a valid string: $validValue"
    case Failure(exception) => s"I have obtained an exception: $exception"
  }

  // map, flatMap, filter -> try or Option have map ,flatmap ,filter composition functions

  /**
   *  Evaluate something on another thread
   *  (Asynchronous programming)
   */

  // pseud-collection - Future
  // global value is the equivalent of a thread pool that is a collection of threads on which we can
  // schedule the valuation of this expression
  /*
   val aFuture = Future({  // Future.apply   the parenthesis () can be ommited
    // this expression acutally evaluated on another thread
    println("loading...")
    Thread.sleep(1000) // block the running by 1 seconds
    println("i have computed a value.")
    67
  })
   */
  // future is a collection which contains a value when it is evaluated
  // future is composable with map, flatmap and filter

  val aFuture = Future{  // Future.apply   the parenthesis () can be ommited
    // this expression acutally evaluated on another thread
    println("loading...")
    Thread.sleep(1000) // block the running by 1 seconds
    println("i have computed a value.")
    67
  }

  Thread.sleep(2000) // block the running by 1 seconds

  // monads - abstracts -> futre, try, option -> just call pseudo-collection

  /**
   * Implicits
   *
   */

  // use 1 - implicit arguments
  def aMethodWithImplicitArgs(implicit arg: Int) = arg+ 1
  implicit val myImplicityInt = 46
  println(aMethodWithImplicitArgs) // aMethodWithImplicitArgs(myImplicityInt)
  // i can print my method without passing any arguments
  // compiler figures out that the method takes an implicit argument and tries to find
  // a value of type int that it can inject here as an argument

  // use 2 - implicit conversions
  // implicit conversions - to add methods to existing types over which we don't have any control over the code

  implicit class MyRichInteger(n: Int) {
    def isEven() = n %2 == 0
  }

  println(23.isEven()) // even though the is even method does not belong to Int Class
  // find the implicit wrapper over 23, maybe i can find that class have even method
  // new MyRichInteger(23).isEven()
  // use implicit very very carefully



}
