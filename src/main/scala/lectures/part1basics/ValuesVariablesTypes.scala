package lectures.part1basics

object ValuesVariablesTypes extends App {

  val x: Int =42
  println(x)

  // vals are immutable -> like constants or final in java
  // compile can infer types
  val y = 62

  // semicolons are allowed in scala, but is not mandatory
  val aString: String = "hello, this is a string"
  val anotherString = "goobye"

  // boolean type

  val aBoolean: Boolean = false
  val aCharacter: Char = 'a'
  val anInt: Int = x   // 4 bytes
  val aShort: Short = 4613   //2 bytes
  val aLong: Long = 35235232423523352L // 8 bytes, similar like java Syntax put L in the last element
  val aFlot: Float = 3.53f  // like java append f,
  val aDouble: Double = 3.5235235235235235 // consistent with java syntax

  // variables

  var aVariable: Int = 4
  aVariable = 5 // side effects ->



}
