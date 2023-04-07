package lectures.part2oop

object OOBasics extends App {

  val person = new Person("min ku nam",34)
  // println(person)  // lectures.part2oop.Person@77f03bb1
  // val person = new Person("min ku nam",34)   -> println(person.age) // error, class parameter but not class member
  // class parameter not field
  // class parameter - > class Person(name: String, age: Int)
  // class filed -> class Person(val name: String, val age: Int)
  // class - blueprints

  // println(person.age)
  // println(person.x)
  person.greet("daniel")
  person.greet()
  val person2 = new Person("min ku kim")
  val person3 = new Person()
  println(person2.name)
  println(person3.age)


  val author = new Writer("Charles", "Dickens", 1812)
  val impostor = new Writer("Charles", "Dickens", 1812)
  val novel = new Novel("Great Expectations", 1861, author)

  println(novel.getAuthorAge())
  println(novel.isWrittenBy(author))  // true
  println(author.getFullName())
  println(novel.isWrittenBy(impostor))  //false

  val counter = new Counter   // start 0 by default
  counter.inc.print
  counter.inc.inc.inc.print
  counter.inc(10).print


}

class Person(val name: String, val age: Int=0)  { // constructor -> Person(name: String, val age: Int)
  // body
  // val and var definitions, function definitions -> methods in class, expressions

  val x = 2  // val definition is class field

  // expressions
  println(1+3)

  // method
  def greet(name: String): Unit = println(s"${this.name} says: hi, $name")

  // this is implied, overloading -> same name but different signatures(different number of parameters
  // or paratemers of different types, coupled  with possibly different return types)
  def greet(): Unit = println(s"Hi, i am $name")

  // multiple constructors or overloading contructors
  // this(name:String) auxilialy contructor, this(name,0) primary contructor
  def this(name: String) = this(name,0)  // this is not good. it is better just using default parameter class Person(name: String, val age: Int = 0)
  def this() = this("jhon doe")
  // default parameters works for contructors too

}

/*
Novel(book) and a Writer class

 */

class Writer(firstName: String, surName: String,val  year: Int) {

  def getFullName(): String = {
    firstName + " " + surName
  }

}

class Novel(name: String, yearOfRelease: Int, author: Writer) {

  def getAuthorAge(): Int = {
    // get author age at the year of Release
    yearOfRelease - author.year
  }

  def isWrittenBy(author: Writer): Boolean = {
     author == this.author
  }

  def getCopy(newYearOfRelease: Int): Novel = {
    // return new Instance of Novel
    new Novel(name, newYearOfRelease, author)
  }

}

/*
  Counter class
    - receivers an int value
    - method current count
    - method to increment/descrement => new Counter
    - overload inc/dec to receive an amount
 */

class Counter(val count: Int = 0) {  // number: Int is parameter and field is(val number: Int)

  // def count = number -> // this is not neccesarry

  def inc = {
    println("incrementing")
    new Counter(count+1)
  } // immutability -> create new count instead of modifying the current one
  // functional programming -> instances are fixed, the cannot modified inside
  // if you going to modify, return as new instance

  def dec = {
    println("decrementing")
    new Counter(count-1)
  }

  def inc(n: Int): Counter = {
    if(n <= 0) this
    else inc.inc(n-1)
  }
  def dec(n: Int): Counter = {
    // new Counter(count-n)
    if(n <= 0) this
    else dec.dec(n-1)
  }

  def print = println(count)

  // when you log something, instead of side effect add println

}
