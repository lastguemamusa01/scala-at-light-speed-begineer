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

  /*
  1 - exapand MyList  -> HOFCurriesMyList.scala
    - foreach method - A => Unit    -> for each element have side effect. apply this function of every element of myList
        [1,2,3].foreach(x => println(x))
    - sort function - compare 2 functions, ((A,A) => Int) => MyList
        [1,2,3].sort((x,y) => y - x) => [3,2,1]  descending sort
    - zipWith (list, (A,A) => b) => MyList[B]
        [1,2,3].zipWith([4,5,6], x * y) => [1*4, 2*5, 3*6] = [4,10,18]
    - fold - curried function -
        fold(start value)(function) => a value
        [1,2,3].fold(0)(x+y) => 6 - sum of the numbers in the list

  2 - defining methods to turn functions into curried and uncurried version

  toCurry(f: (Int, Int) => Int) => (Int => Int => Int)
  fromCurry(Int => Int => Int) =>  (Int, Int) => Int

  3 - abstract math - methods which compose 2 functions

  compose(f,g) => x => f(g(x))   - rambda - function composition
  andThen(f,g) => x => g(f(x))  - applied f first then g
  */


  def toCurry(f: (Int, Int) => Int): (Int => Int => Int) =
    x => y => f(x, y)

  def fromCurry(f: (Int => Int => Int)): (Int, Int) => Int =
    (x, y) => f(x)(y)

  // in the library FunctionX compose and andthen is available
  def compose(f: Int => Int, g: Int => Int): Int => Int =
    x => f(g(x))

  def compose2[A, B, T](f: A => B, g: T => A): T => B =
    x => f(g(x))

  def andThen(f: Int => Int, g: Int => Int): Int => Int =
    x => g(f(x))

  def andThen2[A, B, C](f: A => B, g: B => C): A => C =
    x => g(f(x))

  def supperAdder2: (Int => Int => Int) = toCurry(_ + _)
  def add4 = supperAdder2(4)
  println(add4(17))

  val simpleAdder = fromCurry(superAdder)
  println(simpleAdder(4, 17))

  val add2 = (x: Int) => x+2
  val times3 = (x: Int) => x * 3

  val composed = compose2(add2, times3)
  val ordered = andThen2(add2, times3)

  println(composed(4))
  println(ordered(4))

}
