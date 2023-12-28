package advacedlectures.part5ts

object SelfTypes extends App {

  // self types are a way of requiring a type to be mixed in

  trait Instrumentalist {
    def play(): Unit
  }

  // self: Instrumentalist =>  marker at the language level that force whoever implements Singer
  // to implement instrumentalist as well -> this is self type -> self you can call as you want like this, a
  trait Singer { self: Instrumentalist =>

    def sing(): Unit

  }

  class LeadSinger extends Singer with Instrumentalist { // self obligated that when you extends Singer, you need also with Instrumentalist
    override def play(): Unit = ???
    override def sing(): Unit = ???
  }

//  class Vocalist extends Singer { // illegal
//    override def sing(): Unit = ???
//  }

  val jamesHetfield = new Singer with Instrumentalist { // valid assigment
    override def sing(): Unit = ???
    override def play(): Unit = ???
  }

  class Guitarist extends Instrumentalist {
    override def play(): Unit = println("(guitar solo)")
  }

  val ericClaptop = new Guitarist with Singer {
    override def sing(): Unit = ???
  }

  // vs inheritance

  class A
  class B extends A // B is an A

  trait T
  trait S { self: T =>} // S requires a T

  // cake pattern => java world and another world -> "dependency injection"
  // cake injection is because dependency injection is done in last step like after baking cake you put the cream

  // java dependency injection - classical DI
  class Component {
    // API
  }
  class ComponentA extends Component
  class ComponentB extends Component

  class DependentComponent(val component: Component) // at the runtime Dependent Component can receive
  // either componentA or componentB

  // scala - cake pattern

  trait ScalaComponent {
    // API
    def action(x: Int): String
  }

  trait ScalaDependentComponent { self : ScalaComponent =>
    def dependentAction(x: Int): String = action(x) + " this rocks!"
  }

  trait ScalaApplication { self: ScalaDependentComponent =>  // scala 3 -> self: ScalaDependentComponent with ScalaComponent =>

  }

  // layer 1 - small components
  trait Picture extends ScalaComponent
  trait Stats extends ScalaComponent

  // layer 2 - compose

  trait Profile extends ScalaDependentComponent with Picture // with Picture is mixed in
  trait Analytics extends ScalaDependentComponent with Stats

  // layer 3
  trait AnalyticsApp extends ScalaApplication with Analytics

  // dependency injection in java is injected in runtime, but cake pattern in scala
  // the dependencies are checked at compile time


  // cyclical dependencies

//  class X extends Y // error
//  class Y extends X

  trait X { self: Y => }
  trait Y { self: X => }

}
