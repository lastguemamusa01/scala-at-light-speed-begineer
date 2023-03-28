package lectures.part1basics

object CBNvsCBV extends App {

  // call by name vs call by value
  def calledByValue(x: Long): Unit = {
    println("by value: " + x)
    println("by value: " + x)
  }

  def  calledByName(x: => Long): Unit = {
    println("by name: " + x)
    println("by name: " + x)
  }

  calledByValue(System.nanoTime()) // same values
  calledByName(System.nanoTime())  // two different values

  // by value call - the exact value of this expression is computed before the
  // function evaluates in the same value is used in the function definition
  // calling parameters used standard language like java
  // call by value, get the nanotime and use it in function
  // by name call - expression is passed literally as is
  // expression is evaluated every time
  // System.nano time is evaluated 2 times
  // we get 2 different values
  // => it delays the evaluation of the expression passed as a parameter
  // and it is used literally every time in function definition
  // extremely useful in lazy streams and in things that might fail

  def infinite(): Int = 1 + infinite()

  def printFirst(x: Int, y: => Int) = println(x)

  // This cause stack over flow error
  // printFirst(infinite(), 34)

  printFirst(34, infinite())  // 34 printed, call by name is evaluated when is used, but y is never used
  // the function is never use y that is infinite()

  // call by value - value is computed before call and same value used everywhere
  // call by name - expression is passed literally, expression is evaluated at every use within


}
