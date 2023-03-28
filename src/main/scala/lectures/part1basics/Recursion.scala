package lectures.part1basics

import scala.annotation.tailrec

object Recursion extends App {


  def factorial(n: Int): Int = {
    if(n <= 1) 1
    else {
      println("computing factorial of " + n + " - I first need factorial of " + (n-1))
      val result = n * factorial(n-1)
      println("Computed factorial of " + n)
      result
    }
  }

  // java jvm use stack
  println(factorial(10))

  /*

  jvm stack frame call ->
  computing factorial of 10 - I first need factorial of 9
  computing factorial of 9 - I first need factorial of 8
  computing factorial of 8 - I first need factorial of 7
  computing factorial of 7 - I first need factorial of 6
  computing factorial of 6 - I first need factorial of 5
  computing factorial of 5 - I first need factorial of 4
  computing factorial of 4 - I first need factorial of 3
  computing factorial of 3 - I first need factorial of 2
  computing factorial of 2 - I first need factorial of 1

  - free the results
  Computed factorial of 2
  Computed factorial of 3
  Computed factorial of 4
  Computed factorial of 5
  Computed factorial of 6
  Computed factorial of 7
  Computed factorial of 8
  Computed factorial of 9
  Computed factorial of 10
  3628800
   */
  // jvm internal stack have limited memory

  // println(factorial(5000))
  // recursive depth is too big -> stop 722 stack
  // this cause -> stackoverflowerror -> Exception in thread "main" java.lang.StackOverflowError

  def anotherFactorial(n: Int): BigInt = {
    @tailrec   // this annotation assure if this is tail recursion or not, if it is not will show where is the error
    def factHelper(x: Int, accumulator: BigInt): BigInt =
      if (x <= 1) accumulator
      else factHelper(x-1, x*accumulator) // tail recursion -> use same stack, not use a lot of memory stack
    // tal recursion -> use recursive call as the LAST expression

    factHelper(n, 1)
  }

  println(anotherFactorial(5000))
  /*

  anotherFactorial(10) = factHelper(10,1)
  = factHelper(9, 10*1)
  = factHelper(8, 9 * 10 * 1)
  = factHelper(7, 8 * 9 * 10 * 1)
  = ...... until
  = factHelper(2, 3 * 4 * ....1)
  = factHelper(1, 2 * 3* 4....1)
  = return the accumulator ->  2 * 3 * 4 * .... * 1

   */

  // when you need loops, use tail recursion
  // for tail recursion use accumulators

  /* string concat
     1 concatenate a string n times that use tail recursive
     2 IsPrime function tail recursive
     3 fibonacci function, tail recursive
   */

  @tailrec
  def concatenateTailRec(aString: String, n: Int, accumulator: String): String =
    if(n <= 0) accumulator
    else concatenateTailRec(aString, n-1, aString+accumulator)


  println(concatenateTailRec("hello", 5000, ""))

  def isPrime(n: Int): Boolean = {
    @tailrec
    def isPrimeTailrec(t: Int, isStillPrime: Boolean): Boolean = {
      if(!isStillPrime) false
      else if(t <= 1) true
      else isPrimeTailrec(t-1, n % t != 0 && isStillPrime)
    }

    isPrimeTailrec(n/2, true)
  }

  println(isPrime(2003))
  println(isPrime(629))
  println(isPrime(53253629))

  def fibonacci(n: Int): Int = {
    @tailrec
    def fiboTailrec(i: Int, last: Int, nextToLast: Int): Int =
      if(i >= n) last
      else fiboTailrec(i+1, last+nextToLast, last)

    if(n <=2) 1
    else fiboTailrec(2, 1, 1)
  }

  println(fibonacci(12422))


}
