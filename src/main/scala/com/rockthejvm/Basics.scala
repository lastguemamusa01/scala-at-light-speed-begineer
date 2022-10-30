package com.rockthejvm

object Basics extends App {

  // defining a value
  val meaningOfLife: Int = 42 // const int meaningOfLife = 42;

  // Int, Boolean, Char, Double, Float, String
  // type inference
  val aBoolean = false // type is optional

  // string and string operations
  val aString = "I love Scala"
  val aComposedString = "I" + " " + "love" + " " + "Scala"
  val anInterpolatedString = s"The meaning of life is $meaningOfLife"

  // value - expressions, expressions = structures that can be reduced to a value
  val anExpression = 2 + 3

  // in java or c are instructions, but in scala is expressions instructions
  // scala all is expressions, even if, this means all expression can be reduced to a value

  val ifExpression = if (meaningOfLife > 43) 56 else 999 // ternary operator - meaningOfLife > 43 ? 56 : 999
  val chainedIfExpression =
    if (meaningOfLife > 43) 56
    else if (meaningOfLife < 0) -2
    else if (meaningOfLife > 999) 78
    else 0

  // code blocks
  val aCodeBlock = {
    // definitions
    val aLocalValue = 67
    // functions,classes, local values, inner code block or whenever you want
    // but need to return something

    // value of block is the value of the last expression
    aLocalValue + 3
  }

  // define a function
  def myFunction(x: Int, y: String): String = {
    y + " " + x
  }

  // recursive functions
  def factorial(n: Int): Int =
    if (n <= 1) 1
    else n * factorial(n-1)

  // In scala we don't use loops or iteration, we use RECURSION!
  // In scala have loops -> don't use loops in production or ever never

  //the Unit type = no meaningful value === "void" in other languages
  // type of SIDE EFFECTS -> Unit -> print something screen, sending socket, store file - not for computing meaningful information
  println("I love Scala") // System.out.println, printf, print, console.log, void

  def myUnitReturningFunction(): Unit = {
    println("i don't love returning Unit")
  }

  val theUnit = ()

}
