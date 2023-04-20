package exercises

abstract class MyList {

  // describe list of integers -> implement your own collection
  // singly linked list, holds integers
  // methods -> head = return the first integer of the list
  // tail = remainder of the list
  // isEmpty = is this list empty
  // add(int) => return new list with this element added
  // toString -> return a string representation of the list
  // add these methods in 2 or more or however many you decide subclasses or subtypes of MyList abstract class
  // first decide on the method signatures for each of these methods

  def head: Int
  def tail: MyList
  def isEmpty: Boolean
  def add(element: Int): MyList
  def printElements: String
  // polymorphic call
  override def toString: String = "[" + printElements + "]"

}

object Empty extends MyList {
  def head: Int = throw new NoSuchElementException // ??? return nothing -> will be implemented later ->
  def tail: MyList = throw new NoSuchElementException  // no element is empty list, throw exception
  def isEmpty: Boolean = true
  def add(element: Int): MyList = new Cons(element,Empty)
  def printElements: String = ""
}

class Cons(h: Int, t: MyList) extends MyList {

  def head: Int = h
  def tail: MyList = t
  def isEmpty: Boolean = false
  def add(element: Int): MyList = new Cons(element, this)
  def printElements: String =
    if(t.isEmpty) "" + h
    else h + " " + t.printElements // recursive method  -> this only work in scala 2, for scala 3 use String interpolator
}

object LisTest extends App {

  // linked list
  val list = new Cons(1, new Cons(2, new Cons(3, Empty)))
  println(list.tail.head)
  println(list.add(4).head)
  println(list.isEmpty)

  println(list.toString)

}




