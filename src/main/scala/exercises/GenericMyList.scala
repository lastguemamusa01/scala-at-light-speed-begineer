package exercises

abstract class GenericMyList[+A] {

  // describe list of integers -> implement your own collection
  // singly linked list, holds integers
  // methods -> head = return the first integer of the list
  // tail = remainder of the list
  // isEmpty = is this list empty
  // add(int) => return new list with this element added
  // toString -> return a string representation of the list
  // add these methods in 2 or more or however many you decide subclasses or subtypes of GenericMyList abstract class
  // first decide on the method signatures for each of these methods

  def head: A
  def tail: GenericMyList[A]
  def isEmpty: Boolean
  def add[B >: A](element: B): GenericMyList[B]
  def printElements: String
  // polymorphic call
  override def toString: String = "[" + printElements + "]"

}

object GenericEmpty extends GenericMyList[Nothing] {   // list of Nothing
  def head: Nothing = throw new NoSuchElementException // ??? return nothing -> will be implemented later ->
  def tail: GenericMyList[Nothing] = throw new NoSuchElementException  // no element is empty list, throw exception
  def isEmpty: Boolean = true
  def add[B >: Nothing](element: B): GenericMyList[B] = new GenericCons(element,GenericEmpty)
  def printElements: String = ""
}

class GenericCons[+A](h: A, t: GenericMyList[A]) extends GenericMyList[A] {

  def head: A = h
  def tail: GenericMyList[A] = t
  def isEmpty: Boolean = false
  def add[B >: A](element: B): GenericMyList[B] = new GenericCons(element, this)
  def printElements: String =
    if(t.isEmpty) "" + h
    else h + " " + t.printElements // recursive method  -> this only work in scala 2, for scala 3 use String interpolator
}

object GenericMyListTest extends App {

  // linked list
  val listOfIntegers: GenericMyList[Int] = new GenericCons(1, new GenericCons(2, new GenericCons(3, GenericEmpty)))
  val listOfStrings: GenericMyList[String] = new GenericCons("Hello", new GenericCons("Scala", GenericEmpty))

  println(listOfIntegers.toString)
  println(listOfStrings.toString)

}
