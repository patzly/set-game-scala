package de.htwg.se.set.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TripletSpec extends AnyWordSpec with Matchers:

  "A Triplet" when:
    "constructed with matching features" should:
      val card1 = Card(1, Color.RED, Symbol.OVAL, Shading.SOLID, false)
      val card2 = Card(2, Color.RED, Symbol.OVAL, Shading.SOLID, false)
      val card3 = Card(3, Color.RED, Symbol.OVAL, Shading.SOLID, false)
      val triplet = Triplet(card1, card2, card3)
      "be a set" in:
        triplet.isSet shouldBe true
    "constructed with colliding features" should:
      val card1 = Card(1, Color.RED, Symbol.OVAL, Shading.SOLID, false)
      val card2 = Card(2, Color.GREEN, Symbol.OVAL, Shading.SOLID, false)
      val card3 = Card(3, Color.RED, Symbol.OVAL, Shading.SOLID, false)
      val triplet = Triplet(card1, card2, card3)
      "not be a set" in:
        triplet.isSet shouldBe false
    "constructed with equal cards" should:
      "throw an IllegalArgumentException" in:
        val card1 = Card(1, Color.RED, Symbol.OVAL, Shading.SOLID, false)
        val card2 = Card(1, Color.RED, Symbol.OVAL, Shading.SOLID, false)
        val card3 = Card(3, Color.RED, Symbol.OVAL, Shading.SOLID, false)
        val thrown = intercept[IllegalArgumentException]:
          Triplet(card1, card2, card3)
        thrown.getMessage shouldEqual "Cards must be different"