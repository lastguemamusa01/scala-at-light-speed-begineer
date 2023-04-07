package lectures.part2oop

object MethodNotations extends App {

  // going to create class inside the object, to avoid conflict with another class Person in OOBasic object external Person Class
  class Person(val name: String, favoriteMovie: String, val age: Int = 0) {
    def likes(movie: String): Boolean = movie == favoriteMovie
    def hangOutWith(person: Person): String = s"${this.name} is hanging out with ${person.name}"
    def +(person: Person): String = s"${this.name} is hanging out with ${person.name}"
    def +(nickname: String): Person = new Person(s"$name ($nickname)", favoriteMovie)
    def unary_! : String  = s"$name, what the heck?!"  // very carefule add space unary_! :
    def unary_+ : Person = new Person(name, favoriteMovie, age + 1)
    def isAlive: Boolean = true
    def apply(): String = s"HI, my name is $name and i like $favoriteMovie"  // () is mandatory , the method signature is very important
    def apply(n: Int) : String = s"$name watched $favoriteMovie $n times"

    def learns(thing: String) = s"$name is learning $thing"
    def learnsScala = this learns "Scala"

  }

  // scala - natural language - syntactic sugar

  val mary = new Person("Mary", "Inception")
  println(mary.likes("Inception"))
  println(mary likes "Inception") // equivalent mary.likes -> this is infix notation  or operator notation
  // only work with methods with only one parameters -> syntactic sugar

  // infix notation = operator notation (syntatic sugar)

  // "operators" in Scala
  val tom = new Person("Tom", "Fight Club")
  println(mary hangOutWith tom) // in this case, the hangOutWith method acts like operator between mary and tom
  // operator like math + -
  // you can change this method hangOutWith, renaming + or & is valid
  // def +(person: Person): String = s"${this.name} is hanging out with ${person.name}"

  println(1 + 2)
  println(1.+(2))

  // all operators are methods
  // akka actos have ! ?   - asyncronous actors

  // prefix notation
  // unary operators

  val x = -1  // - is unary operators -> unary operators also methods
  val y = 1.unary_-  // equivalent val x = -1

  // unary_ prefix only works with fea operators (- + ~ !)

  println(!mary) //   Mary, what the heck?!
  println(mary.unary_!) // equivalent !mary  -> syntatic sugar with prefix notation

  // postfix notation - syntatic sugar
  // the method or function that do not receive any parameters
  // have the property that they can be used in postfix notation

  println(mary.isAlive)
  println(mary isAlive)  //  equivalente println(mary.isAlive)  - this is not used frequently, cause human confussions

  // special method name apply


  println(mary.apply())
  println(mary()) // equivalent println(mary.apply())
  // call the mary object as function mary()
  // compiler see call the function call apply()

  // this feature breaks the barrier between OOP and functional programming
  // apply is used a lot

  /*
   1. overload plus operator
   mary + "the rockstar" => new Person "Mary (the rockstar)"

   2 - add the age to the person class with default zero value
   add a unary + operator => new person with the age + 1
   +mary => mary with the age increment

   3 - Add a "learns" method in the Person Class => Mary learns Scala"
       Add a learnsScala method , calls the learns method with "Scala" as parameter
       use it in postfix notation

   4 - Overload the apply method
       mary.apply(2) => "Mary watched Inception 2 times"
   */

  println((mary + "the rockstar") ()) // .apply() method
  println((+mary).age)  // always use () when this return new class
  println(mary learnsScala)
  println(mary(10))

  // method notation -> infix notation, prefix notation (unary operators) and postfix(rare used) notation (mary isAlive)
  // -> apply method -> allow you to call your objects like they were functions


}


