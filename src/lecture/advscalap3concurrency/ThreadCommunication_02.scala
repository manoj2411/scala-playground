package lecture.advscalap3concurrency

import scala.collection.mutable

object ThreadCommunication_02 extends App {

  /*   Producer Consumer Problem
         - both are running in parallel, inserting e to a list and they dont about each other
         - we want to somehow force the consumer to wait until producer finished
         - TODO: forcing threads to run in a guaranteed order
  * */

  class SimpleContainer {
    private var value = 0
    def isEmpty = value == 0
    def get: Int = {
      val currValue = value
      value = 0 // reset value to the default state
      currValue
    }
    def set(newValue: Int) = value = newValue
  }

  def naiveProducerConsumer: Unit = {
    val valueContainer = new SimpleContainer
    val producer = new Thread(() => {
      Thread.sleep(100)
      println("[producer] setting value ..")
      valueContainer.set(42)
    })

    val consumer = new Thread(() => {
      while(valueContainer.isEmpty) {
        println("[consumer] waiting ..")
      }
      println(s"[consumer] consumer value ${valueContainer.get}")
    })

    producer.start()
    consumer.start()
  }
  //  naiveProducerConsumer

  def smartProducerConsumer: Unit = {
    val valueContainer = new SimpleContainer

    val producer = new Thread(() => {
      println("[producer] work on task")
      Thread.sleep(2000)
      valueContainer.synchronized {
        println("[producer] setting value ..")
        valueContainer.set(42)
        valueContainer.notify()
      }
    })

    val consumer = new Thread(() => {
      valueContainer.synchronized {
        println("[consumer] waiting ..")
        valueContainer.wait()
      }
      // Now valueContainer.isEmpty will be false
      println(s"[consumer] consumer value ${valueContainer.get}")
    })

    producer.start()
    consumer.start()
  }
  //  smartProducerConsumer

  /*    Extend producer-consumer from a single value container to Buffer or values
          - buffer with limited size
          producer -> [ ? ? ? ] -> consumer
  * */

  def bufferedProducerConsumer: Unit = {
    val buffer = mutable.Queue[Int]()

    val producer = new Thread(() => {
      (1 to 10).foreach { i =>
        buffer.synchronized {
          if (buffer.size == 3) {
            buffer.wait()
          }
          println(s"[producer] setting value .. $i")
          buffer.enqueue(i)
          buffer.notify()
        }
      }
    })

    val consumer = new Thread(() => {
      (0 to 15).foreach { _ =>
        buffer.synchronized {
          if (buffer.isEmpty) {
            buffer.wait()
          }
          // Gets control after notifying
          println(s"[consumer] consuming ${buffer.dequeue()}")
          buffer.notify()
        }
      }
    })

    producer.start()
    consumer.start()
  }

  bufferedProducerConsumer
}
