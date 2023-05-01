package lectures.part3fp

object HOFsCurries extends App {

  // how to read this fucking function
  // return type => (Int => Int) is another function which takes int and return int
  // first parameter is Int
  // second parameter is (String, (Int => Boolean)) => Int)
  // second parameter take String, and function(Int => Boolean) return Int
  // first parameter is Int, second parameter is function and return function
  val superFunction: (Int, (String, (Int => Boolean)) => Int) => (Int => Int) = null
  // higher order function(HOF) -> function that either takes function as parameters
  // or return functions as a result.

  // map,flatMap, filter in LambdaMyList.scala we already used HOF -> receiver parameter as function

  // function that applies a function n times over a value x
  // nTimes(f, n ,x)   f - function, n - number of times, value x
  // nTimes(f, 3, x) = 3 times application of f over x -> f(f(f(x))) -> classical example of high order functions
  // nTimes(f, 3, x) = f(f(f(x))) = nTimes(f,2,f(x))
  // nTimes(f, n, x) = f(f(...f(x))) = nTimes(f, n-1, f(x)) -> recursion
  def nTimes(f: Int => Int, n: Int, x: Int): Int =
    if(n <= 0) x
    else nTimes(f, n-1, f(x))

  val plusOne = (x: Int) => x + 1
  println(nTimes(plusOne,10,1))

  // ntb(f,n) = x => f(f(f...(x))) -> actually return lambda, x is applied later
  // increment10 = ntb(plusOne, 10) = x => plusOne(plusOne...(x))
  // val y = increment10(1)
  def nTimesBetter(f: Int => Int, n: Int): (Int => Int) =
    if(n <= 0) (x: Int) => x
    else (x:Int) => nTimesBetter(f, n-1)(f(x))

  val plusTen = nTimesBetter(plusOne, 10)
  println(plusTen(1))

  // curried functions - nTimesBetter(f, n-1)(f(x))
  val superAdder: Int => (Int => Int) = (x: Int) => (y:Int) => x + y
  val add3 = superAdder(3) // lambda -> y => 3 + y
  println(add3(10))

  // curried functions are very useful if you want to define the helper functions that you want
  // to use later on various values.
  println(superAdder(3)(10))  // this is also valid

  // another kind of curried functions - functions with multiple parameter lists
  // you can define function with multiple parameter lists to act like curried functions
  def curriedFormatter(c: String)(x: Double): String = c.format(x)

  val standardFormat: (Double => String) = curriedFormatter("%4.2f")
  val preciseFormat: (Double => String) = curriedFormatter("%10.8f")
  // curried formatter acts like a curried function because i can then later create sub functions
  // that apply curried format with fewer parameter lists, and use them wherever value i want later
  println(standardFormat(Math.PI))
  println(preciseFormat(Math.PI))


}
