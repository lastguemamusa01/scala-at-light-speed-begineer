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
        Thread.sleep(random.nextInt(500)) // max wil be 5 seconds -> 500
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

  prodConsLargeBuffer()

}
