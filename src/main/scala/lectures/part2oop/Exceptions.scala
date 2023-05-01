package lectures.part2oop

object Exceptions extends App {

  //Exceptions - when your program crashes

  val x: String = null
//  println(x.length) // throw nullpointerexception, crash

  // 1. throwing and catching exceptions

  // throw the expection intencionally
  // throwing exception in scala is expressions

  // val aWeirdValue: String = throw new NullPointerException  // Nothing, nothing can subsititute any value

   // throwable classes extends the Throwable class
  // Exception(went wrong with the program) and Error(something that happened wrong with the system) are the major Throwable subtypes
  // Error example - stackvoerflow error

  // 2. how to catch exceptions

  def getInt(withExceptions: Boolean) : Int =
    if(withExceptions) throw new RuntimeException("no int for you!")
    else 42

  // wat to guard against an exception from getInt function is using try catch expression

  val potentialFail = try {  // AnyVal(Int or Unit)
    // code that might fail
    getInt(true)
  } catch {
    case e: RuntimeException => 43
  } finally {
    // code that will get executed no matter what, optional, does not influence the return type of this expression
    // use finally only for side effects. login something to file
    println("finally")
  }

  println(potentialFail)

  // 3. how to define your own exceptions
  class MyException extends Exception
  val exception = new MyException

  // throw exception

  /*
  1. crash your program with an OutOfMemoryError
  2. Crash with stackoverflow error
  3. PocketCalculator
     - add(x,y)
     - subtract(x,y)
     - multiply(x,y)
     - divide(x,y)

     Throw custom exception
        - OverflowException if add(x,y) exceeds Int.MAX_VALUE
        - underflowException if subtract(x,y) exceeds Int.MIN_VALUE
        - MathCalculationException for division by 0
   */

  // 1 crash you program with an out of memory error
  // val array = Array.ofDim(Int.MaxValue) // Requested array size exceeds VM limit

  // 2 - stackoverflow
  // def infinite: Int = 1+ infinite
  // val noLimit = infinite

  // 3 pocket calculator

  class OverFlowException extends RuntimeException
  class UnderFlowException extends RuntimeException
  class MathCalculationException extends RuntimeException("Division by 0")  // RuntimeException has string as parameter to throw custom message

  object PocketCalculator {
    def add(x: Int, y: Int): Int = {
      val result = x+y

      if(x > 0 && y > 0 && result < 0) throw new OverFlowException
      else if(x < 0 && y <0 && result > 0) throw new UnderFlowException
      else result
    }

    def subtract(x: Int, y: Int): Int = {
      val result = x - y

      if(x > 0  && y < 0 && result < 0 && result < 0 )  throw new OverFlowException    // 5 - -5  -> 5+5
      else if(x < 0 && y > 0 && result > 0 ) throw new UnderFlowException  // -5 -5
      else result
    }

    def multiply(x: Int, y: Int): Int = {
      val result = x * y

      if(x > 0 && y > 0 && result < 0) throw new OverFlowException   // 2*2 = 4
      else if(x < 0 && y < 0 && result < 0) throw new OverFlowException  // -2*-2 = 4
      else if(x > 0 && y < 0 && result > 0) throw new UnderFlowException  // -2*2 = -4
      else if(x < 0 && y > 0 && result > 0) throw new UnderFlowException // 2*-2 = -4
      else result
    }

    def divide(x: Int, y: Int) = {
      if(y == 0) throw new MathCalculationException
      else x/y
    }



  }

  println(PocketCalculator.add(Int.MaxValue, 10))  // -2147483639    negative value, this mean overflow intenger
  println(PocketCalculator.divide(2,0))
}
