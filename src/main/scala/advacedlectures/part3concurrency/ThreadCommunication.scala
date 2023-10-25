package advacedlectures.part3concurrency

import scala.collection.mutable
import scala.util.Random

object ThreadCommunication extends App {

  /*
  The producer-consumer problem

  producer -> [ x ] -> consumer(extract)
  parallel in same time
  don't know when each other has finished working

  obligated consumer wat until producer finish his job
  multi threads - producer consumer problem and synchronization problem


  Syncronized -> entering a synchronized expression on a object locks the object :

  val somObject = "hello"

  someObject.synchronized { -> lock the object's monitor
    //
  }

  a monitor is a data structure that it's internally used by the JVM to keep track of which object locked by which thread.

  only AnyRefs can have synchronized blocks
  int or boolean do not have syncronized expressions

  General principles :
  - make no assumptions about who gets the lock first
  - keep locking to a minimum -> for performance concerns
  - maintain thread safety at ALL times in parallel applications



   */

  class SimpleContainer {

    private var value: Int = 0

    def isEmpty: Boolean = value == 0

    def set(newValue: Int) = value = newValue

    def get: Int = {
      val result = value
      value = 0
      result
    }
  }

  def naiveProdCons(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[consumer] waiting...")
      while(container.isEmpty) {
        println("[consumer] actively waiting...")
      }
      println("[consumer] I have consumed" + container.get)
    })

    val producer = new Thread(() => {
      println("[producer] computing...")
      Thread.sleep(500)
      val value = 42
      println("[producer] i have produced, after long work , the value " + value)
      container.set(value)
    })

    consumer.start()
    producer.start()
  }

  // cal naive producer and consumer -> a lot of computing wasting resources
  // naiveProdCons()

  // wait and notify
  /*
  wait - ing on an object's monitor suspends you(the thread) indefinitely

  // thread 1
  val someObject = "hello"
  someObject.synchronized { // lock the object's monitor
    // ... code part 1
    someObject.wait() // release the lock and... wait
    // ... code part 2 // when allowed to proceed, lock the monitor again and continue
  }

  // thread 2
  someObject.synchronized { // lock the object's monitor
    // ... code
    someObject.notify() // signal one(random) sleeping thread they may continue
    // ... more code
  } // but only after i'm done and unlock the monitor

  notifyAll() -> to wawken all threads
  wait and notify only work inside the syncronizhed object -> someObject.syncrhonized {

   */

  def smartProdCons(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[consumer] waiting...")
      container.synchronized {
        container.wait()
      }

      // container must have some value
      println("[consumer] i have consumed" + container.get)
    })

    val producer = new Thread(() => {
      println("[producer] hard at work...")
      Thread.sleep(2000)
      val value = 42

      container.synchronized {
        println("[producer] i'm producing " + value)
        container.set(value)
        container.notify()
      }
    })

    consumer.start()
    producer.start()

  }

  // smartProdCons()

  /*
  buffer
  producer -> [ ? ? ?] -> consumer
  buffer is full -> producer most block until the consumer extracting some values
  buffer is empty -> consumer most block until the producer produce some values
  */

  def prodConsLargeBuffer(): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 3

    val consumer = new Thread(() => {
      val random = new Random()

      while(true) { // this always be true, to simulate the consumer that is alive forever
        buffer.synchronized {
          if(buffer.isEmpty) {
            println("[consumer] buffer empty, waiting... ")
            buffer.wait()
          }

          // there must be at least one value in the buffer
          val x = buffer.dequeue()
          println("[consumer] consumed " + x)

          // hey producer, there's empty space available, are you lazy ?!
          buffer.notify()
        }
        // computation simulation with sleep
        Thread.sleep(random.nextInt(500)) // max wil be 5 seconds -> half second
      }
    })

    val producer = new Thread(() => {
      val random = new Random()
      var i = 0  // sequencer

      while(true) {
        buffer.synchronized {
          if(buffer.size == capacity) { // if the buffer is full
            println("[producer] buffer is full, waiting...")
            buffer.wait()
          }

          // there must be at least one empty space in the buffer
          println("[producer] producing " + i)
          buffer.enqueue(i)

          // hey consumer, new food for you!
          buffer.notify()

          i += 1
        }

        // computation simulation with sleep
        Thread.sleep(random.nextInt(500))
      }
    })

    consumer.start()
    producer.start()
  }

  // prodConsLargeBuffer()

  /*
   prod-cons, level 3
   producer1 ->  [? ? ?] -> consumer1
   producer 2 -----^  ^---- consumer2
   multiple producers and consumers
   */

  class Consumer(id: Int, buffer: mutable.Queue[Int]) extends Thread {
    override def run(): Unit = { // Runnable Thread, Thread implements Runnable
      val random = new Random()

      while (true) { // this always be true, to simulate the consumer that is alive forever
        buffer.synchronized {
          /*
          producer produces value, 2 Cons are waiting
          notifies one consumer, notifies on buffer
          notifies the other consumer -> X
          */
          while (buffer.isEmpty) { // repeatively check, ok im awake up and buffer is empty ?
            println(s"[consumer $id] buffer empty, waiting... ")
            buffer.wait()
          }

          // there must be at least one value in the buffer
          val x = buffer.dequeue()
          println(s"[consumer $id] consumed " + x)

          // hey somebody(producer or consumer) wake up
          buffer.notifyAll()  // use notifyAll instead of notify, in multi consumer and producer, to prevent deadlock
        }
        // computation simulation with sleep
        Thread.sleep(random.nextInt(500)) // max wil be 5 seconds -> half second
      }
    }
  }

  class Producer(id: Int, buffer: mutable.Queue[Int], capacity: Int) extends Thread {
    override def run(): Unit = {
      val random = new Random()
      var i = 0 // sequencer

      while (true) {
        buffer.synchronized {
          while (buffer.size == capacity) { // if the buffer is full
            println(s"[producer $id] buffer is full, waiting...")
            buffer.wait()
          }

          // there must be at least one empty space in the buffer
          println(s"[producer $id] producing " + i)
          buffer.enqueue(i)

          // hey somebody(producer or consumer) wake up
          buffer.notifyAll()

          i += 1
        }

        // computation simulation with sleep
        Thread.sleep(random.nextInt(500))
      }
    }
  }

  def multiProdCons(nConsumers: Int, nProducers: Int): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 20

    (1 to nConsumers).foreach(i => new Consumer(i, buffer).start())
    (1 to nProducers).foreach(i => new Producer(i, buffer, capacity).start())

  }

  // multiProdCons(3, 3)

  /*
  exercises
  1) think of an example whre notifyAll acts in a different way than notify ?
  2) create a deadlock -> deadlock is a situation when one thread or multiple threads block each other and they cannot continue
  3) create a livelock -> livelcok is also a situation when threads cannot continue, but as opposed to a deadlock.
  implies that threads yield execution to each other in such a way that nobody can continue, threads are active, not blocked, but they cannot continue
  */

  // notifyAll

  def testNotifyAll(): Unit = {
    val bell = new Object

    (1 to 10).foreach(i => new Thread(() => {
      bell.synchronized {
        println(s"[thread $i] waiting...")
        bell.wait()
        println(s"[thread $i] hooray!")
      }
    }).start())

    new Thread(() => {
      Thread.sleep(2000)
      println("[announcer] Rock'n roll!")
      bell.synchronized {
        bell.notifyAll()
      }

    }).start()
  }

  // thread 1 to 10 will be waiting, after 2 minutes of announcer rock n roll. all threads will be hooray !
  // this is cannot possible with notify(), only one thread will be hooray! when 9 still be blocked
  // testNotifyAll()

  // 2 - deadlock
  // small and imaginary society where people salute by bowing and when someone bows to you,
  // you bow to them and you only rise when the other person has started rising
  // this dont make sense, because each other wating to started rising to rise, so this people will be bowing everytime

  case class Friend(name: String) {
    def bow(other: Friend) = {
      this.synchronized {
        println(s"$this: I am bowing to my fried $other")  // s is interporated string
        other.rise(this)
        println(s"$this: my friend $other has risen")
      }
    }

    def rise(other: Friend) = {
      this.synchronized {
        println(s"$this: I am rising to my fried $other")
      }
    }

    // livelock
    var side = "right"
    def switchSide(): Unit = {
      if(side == Right) side = "left"
      else side = "right"
    }

    def pass(other: Friend): Unit = {
      while(this.side == other.side) {
        println(s"$this: oh, but please, $other, feel free to pass...")
        switchSide()
        Thread.sleep(1000) // 1 seconds
      }
    }

  }

  val sam = Friend("Sam")
  val pierre = Friend("Pierre")

  // new Thread(() => sam.bow(pierre)).start()
  // new Thread(() => pierre.bow(sam)).start()

  /*
  lock each other
  Friend(Sam): I am bowing to my fried Friend(Pierre) // sam's lock,    | then pierre's lock
  Friend(Pierre): I am bowing to my fried Friend(Sam) // pierre's lock, | then sam's lock
  */


  // 3 - livelock
  // exreme polite society, you an dyour friend meet from different directions on the same
  // road and are about to bump into each other, then you must be so polite that you will
  // give way to your friend to cross
  // left and right side

  new Thread(() => sam.pass(pierre)).start()
  new Thread(() => pierre.pass(sam)).start()

  /*

  Friend(Sam): oh, but please, Friend(Pierre), feel free to pass...
  Friend(Pierre): oh, but please, Friend(Sam), feel free to pass...
  Friend(Pierre): oh, but please, Friend(Sam), feel free to pass...
  Friend(Sam): oh, but please, Friend(Pierre), feel free to pass...
  ........

  infinite loop -
  no thread blocked, but no thread continue running, because there are yielding execution to each other.
   */



}
