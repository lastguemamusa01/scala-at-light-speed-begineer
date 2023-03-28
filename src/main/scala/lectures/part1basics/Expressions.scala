package lectures.part1basics

object Expressions extends App {

  val x = 1 + 2 // expression
  println(x)

  println(2 + 3 * 4)

  // math operators
  // + - * /
  // bitwise operators
  // & | ^ << >> >>> (right shift with zero extension)

  // relational operators
  println(1 == x)
  // == != > >= < <=

  // boolean operatros

  println(!(1 == x))
  // logical negation
  // ! && ||

  // side effects
  // operator for changing variables
  var aVariable = 2
  aVariable += 3  // aLSO WORKS WITH -= *= /=
  println(aVariable)

  // instructions vs expressions

  // instructions -> what to do - java - instruction. do this, do that

  // expressions -> has value or and or a type -> functional programming -> expressions -> compute value

  // if expression  in scala

  val aCondition = true

  val aConditionedValue = if(aCondition) 5 else 3  // if expression, not if instruction
  println(aConditionedValue)
  println(if(aCondition) 5 else 3)  // if in scala is expression
  println(1 + 3)

  // loops -> discorage to use in scala -> loops imperative programming language like java. execute side effects

  var i = 0
  // whle loop is scala -> this is instructions
  while(i < 10) {  // side effects -> val aWhile = while(i < 10) {  -> unit type
    println(i)
    i += 1
  }

  // never write this in scala -> using while

  // everthing in scala is an expression

  val aWeirdValue = (aVariable = 3) // Unit === void  -> reasining values is side effects
  println(aWeirdValue) // print ()

  // side effects : println(), whiles, reassigning -> scala is instructions that returning void


  // code blocks -> special kind of expressions. value of the block is the last expression
  // inside the code block is visible inside only

  val aCodeBlock = {  // define value, write expressions
    val y = 2
    val z = y+1 // local variable

    if(z > 2) "hello" else "goodbye"
  }

  // instructions are executed like java, expressions are evaluated like scala

  // difference between "hello world" vs println("hello world") ?





}
