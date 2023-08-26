package advacedlectures.part2afp

object CurriesPartiallyAppliedFunctions extends App {

  // curried functions
  val superAdder: Int => Int => Int = // functions that other function as result
    x => y => x + y // curried functions

  val add3 =  superAdder(3) // Int => Int = y => 3 + y
  println(add3(5)) // 3 + 5 = 8
  println(superAdder(3)(5)) // call with multiple parameter list -> 8 ->  curried function

  // method -> when you call method you pass the complete parameters
  def curriedAdder(x: Int)(y: Int): Int = x + y // curried method

  // convert a method into a function value of type Int => Int
  val add4: Int => Int = curriedAdder(4) // function  type Int => Int -> i want remainder functions after a curriedAdder
  // this call lifting -> transforming amethod to function -> more technical term -> ETA-Expansion
  // eta expansion -> simple technique for wrapping functions into this extra layer while preserving
  // identical functionallity -> compiler perform it

  // functions != methods (JVM limitation)

  def inc(x: Int) = x + 1
  List(1,2,3).map(inc) // ETA-expasion, compiler(convert the method to lambda) rewrite .map(inc) to .map(x => inc(x))

  // Partial Function Applications -> explicitly use ETA-expansion
  // _ -> is used in scala2 and early scala3, in scala 3 recent version this is deprecated and not longer need it
  val add5 = curriedAdder(5) _ // do ETA-expasion , convert expression into Int => Int, not need to add the type of : Int => Int Function

  // exercise
  val simpleAddFunction = (x: Int, y: Int) => x + y // simple lambda
  def simpleAddMethod(x: Int, y: Int) = x + y
  def curriedAddMethod(x: Int)(y: Int) = x + y

  // define add7: Int => Int = y => 7 + y
  // as many different implementations of add 7 using the above
  // be creative !
  val add7 = (x: Int) => simpleAddFunction(7, x) // simplest
  val add7_2 = simpleAddFunction.curried(7) // curried method convert to curried function -> y => 7 + y
  val add7_6 = simpleAddFunction(7, _: Int) // this work as well
  val add7_3 = curriedAddMethod(7) _ // (PAF)partially applied function -> applied explicitly ETA-expansion applying lifting
  val add7_4 = curriedAddMethod(7)(_) // PAF -> alternative syntax
  // y => simpleAddMethod(7, y)
  val add7_5 = simpleAddMethod(7, _: Int) // alternative syntax for turning methods into function values -> call ETA-expansion
  // _ => this force the compiler to execute ETA-expansion converting method either functions to another function value
  // you can created more combinations using 3 above functions or methods
  // functions and methods can be partially applied with underscores(_),

  def concatenator(a: String, b: String, c: String): String = a + b + c
  val insertName = concatenator("Hello, I'm ", _: String, ", how are you ?") // x: String => concatenator(hello, x, how are you)
  println(insertName("Min Ku"))

  val fillInTheBlanks = concatenator("Helo, ", _: String, _: String) // (x , y) => concatenator("Hello ", x, y)
  println(fillInTheBlanks("unknown ", "people")) // parameter is injected to _

  // exercises
  /*
  1. Process a list of numbers and return their string representations with different formats
     Use the %4.2f, %8.6f and %14.12f with a curried formatter function
   */

  def curriedFormatter(s: String)(number: Double): String = s.format(number)
  val numbers = List(Math.PI, Math.E, 1, 9.8, 1.3e-12)

  val simpleFormat = curriedFormatter("%4.2f") _ // lift
  val seriousFormat = curriedFormatter("%8.9f") _
  val preciseFormat = curriedFormatter("%14.12f") _

  println(numbers.map(preciseFormat))

  // println(numbers.map(curriedFormatter("%14.12f")))  // compiler does weet ETA-expansion for us
  println("%4.2f".format(Math.PI))

  /*
  2 difference between
    - functions vs methods
    - parameters : by-name vs 0-lambda
   */

  def byName(n: => Int) = n + 1
  def byFunction(f: () => Int) = f() + 1 // f() -> zero lambda(nothing) to Int

  def method: Int = 42
  def parentMethod(): Int = 42 // no parameter

  /*
  calling byName and byFunction
  - int
  - method
  - parentMethod
  - lambda
  - PAF
   */

  byName(23) // ok
  byName(method) // ok -> because method will be evaluate with the call that method is return 42 that is Int
  byName(parentMethod())
  byName(parentMethod) // ok but beware => this convert to byName(paranMethod()) // this is not work in Scala 3
  // byName and byFunction are completely different
  // byName(() => 42) // not ok
  byName((() => 42)())  // ok -> this is ok because you are supplying function here and calling it, turn the whole expression into the value
  // byName(parentMethod _) // not ok - because this is an expression which is a function of value -> PAF

  // byFunction(45) // not ok, function expect lambda not a value
  // byFunction(method) // not ok , you expected method to be converted to a function value, but method evaluted value 42
  // compiler not allowed such call, parameterless like accessor -> without parenthesys and proper methods with parentheses
  // they cannot be passed to higher order function -> compiler does not do ETA expasion
  byFunction(parentMethod) // compiler does ETA-expansion -> work better in scala 3
  // this is difference between access or methods without parentheses and proper methods with parentheses -> method and parentMethod()
  byFunction(() => 46) // lambda works fine -> function value
  byFunction(parentMethod _) // this also works , the underscore - unnecesary -> warning


}
