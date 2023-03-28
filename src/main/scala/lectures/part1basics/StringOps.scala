package lectures.part1basics

object StringOps extends App {

  val str: String = "Hello, I am learning Scala"

  println(str.charAt(2)) // 0 1 2 indexes
  println(str.substring(7, 11)) // first parameter inclusive, sendo parameter exclusive
  println(str.split(" ").toList)
  println(str.startsWith("Hello"))
  println(str.replace(" ","-"))
  println(str.toLowerCase())
  println(str.length)

  // all the functions are also present in the java language
  // scala have access -> String class in java language

  // scala have own utilities
  val aNumberString = "45"
  val aNumber = aNumberString.toInt  // convert the string to int

  // concatenation with prepeding(+:) and appending(:+)
  println('a' +: aNumberString :+ 'z')

  // reverse
  println(str.reverse)

  // list like functions ->
  println(str.take(2))  // He

  // scala specific : string interpolators

  // s-interpolators
  val name = "Min Ku"
  val age = 12
  val greeting = s"my name is $name and age is $age"  // $ -> name variable will be injected

  val anotherGreeting = s"my name is $name and I will be turning ${age+1}"  // expression like plus 1

  println(anotherGreeting)

  // f-interpolators
  // like s interpolators, but also receive printf like format
  val speed = 1.2f
  val myth = f"$name can eat $speed%2.2f burgers per minute" // %2.2f like print f format - 2 characters, total minimum and 2 decimal precisions

  println(myth)  // 1.20

  //f interpolator is like printF
  // $name%s   -> string format
  // f interpolators check for type correctness

  // raw-interpolators
  // like s interpolators, although it has the property, they can print characters literally

  println("This is a \n newline") //backslace -> escape to print in new line
  println(raw"This is a \n newline")  // This is a \n newline


  val escaped = "This is a \n newline"
  println(raw"$escaped") // this will not going to print as raw, backslace espcape printing in new line



}
