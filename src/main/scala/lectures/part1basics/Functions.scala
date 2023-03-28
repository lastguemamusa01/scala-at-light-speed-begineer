package lectures.part1basics

object Functions extends App {

  def aFunction(a: String, b: Int): String = { // you can remove : String, compiler can figure out with type inference
    a + " " +b  // string concat
  }

  println(aFunction("hello",3))

  def aParameterlessFunction(): Int = 42

  println(aParameterlessFunction())
  println(aParameterlessFunction)  // parameterless function can call like this also

  // use functions, instead of loops

  // a recursive functions cannot omit the return type
  // compiler cannot figure out with type inference of recursive function
  def aRepeatedFunction(aString: String, n: Int): String = {
    if(n == 1) aString
    else aString + aRepeatedFunction(aString, n-1)  // recursive functions
  }

  println(aRepeatedFunction("hello", 3))

  //functional language use recursion
  // when you need loops, use recursion

  // side effect - nothing to do with computation -> print, display, logging and write to the file
  def aFunctionWithSideEffects(aString: String): Unit = println(aString)

  def aBigFunction(n: Int): Int = { // use code blocks to use auxiliarly functions
    def aSmallerFunction(a: Int, b: Int): Int = a + b // auxiliarly functions

    aSmallerFunction(n, n-1)
  }

  def aGreetingFucntion(name: String, age: Int) = {
    println(s"Hi, my name is $name and I am $age years old")
  }

  aGreetingFucntion("min ku",34)

  def factorial(n: Int): Int = {
    if(n <= 0) 1
    else n*factorial(n-1)
  }

  println(factorial(5)) // 1*2*3*4*5 = 24*5 = 120

  def aFibonaci(n: Int): Int = {
    if(n <= 2) 1
    else aFibonaci(n-1)+aFibonaci(n-2)
  }

  println(aFibonaci(8)) // 1 1 2 3 5 8 13 21

  def isPrime(n: Int): Boolean = {
    // auxiliary functions
    def isPrimeUntil(t: Int): Boolean =
      if (t <= 1) true
      else n % t != 0 && isPrimeUntil(t-1)

    isPrimeUntil(n/2)
  }

  println(isPrime(37))
  println(isPrime(2003))
  println(isPrime(37*17))

}
