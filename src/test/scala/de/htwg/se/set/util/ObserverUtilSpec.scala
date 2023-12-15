package de.htwg.se.set.util

import de.htwg.se.set.controller.Event
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class ObserverUtilSpec extends AnyWordSpec with Matchers:

  "An Observable" when:
    "new" should:
      val observable = new Observable {}
      "have no subscribers" in:
        observable.subscribers shouldBe empty
    
    "observers are added" should:
      val observable = new Observable {}
      val observerA = new Observer {
        override def update(e: Event): Unit = {}
      }
      val observerB = new Observer {
        override def update(e: Event): Unit = {}
      }
      observable.add(observerA)
      observable.add(observerB)
      "contain the subscribers in the subscribers list" in:
        observable.subscribers should contain theSameElementsAs Vector(observerA, observerB)
    
    "observers are removed" should:
      val observable = new Observable {}
      val observer = new Observer {
        override def update(e: Event): Unit = {}
      }
      observable.add(observer)
      observable.remove(observer)
      "no longer have the removed observer in the subscribers list" in:
        observable.subscribers should not contain observer

    "notifying observers" should:
      val observable = new Observable {}
      var eventReceived: Option[Event] = None
      val observer = new Observer {
        override def update(e: Event): Unit = { eventReceived = Some(e) }
      }
      observable.add(observer)
      "update the observers with the event" in:
        observable.notifyObservers(Event.SETTINGS_CHANGED)
        eventReceived shouldBe Some(Event.SETTINGS_CHANGED)