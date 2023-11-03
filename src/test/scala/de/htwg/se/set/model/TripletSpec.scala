package de.htwg.se.set.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TripletSpec extends AnyWordSpec with Matchers:

  "A Triplet" when:
    "created with distinct cards" should:
      "be initialized correctly" in:
        val card1 = Card(1, Color.RED, Symbol.OVAL, Shading.SOLID, false)
        val card2 = Card(2, Color.GREEN, Symbol.SQUIGGLE, Shading.OUTLINED, false)
        val card3 = Card(3, Color.BLUE, Symbol.DIAMOND, Shading.STRIPED, false)
        val triplet = Triplet(card1, card2, card3)
        triplet.card1 shouldBe card1
        triplet.card2 shouldBe card2
        triplet.card3 shouldBe card3
    "created with identical cards" should:
      "throw an IllegalArgumentException" in:
        val card = Card(1, Color.RED, Symbol.OVAL, Shading.SOLID, false)
        val exception = intercept[IllegalArgumentException] {
          Triplet(card, card, card)
        }
        exception.getMessage shouldBe "Cards must be different"

    "evaluating whether it's a set" should:
      "return true for a valid set" in:
        val card1 = Card(1, Color.RED, Symbol.OVAL, Shading.SOLID, false)
        val card2 = Card(1, Color.GREEN, Symbol.OVAL, Shading.SOLID, false)
        val card3 = Card(1, Color.BLUE, Symbol.OVAL, Shading.SOLID, false)
        val triplet = Triplet(card1, card2, card3)
        triplet.isSet shouldBe true
      "return false for an invalid set" in:
        val card1 = Card(1, Color.RED, Symbol.OVAL, Shading.SOLID, false)
        val card2 = Card(2, Color.RED, Symbol.OVAL, Shading.SOLID, false)
        val card3 = Card(1, Color.BLUE, Symbol.OVAL, Shading.SOLID, false)
        val triplet = Triplet(card1, card2, card3)
        triplet.isSet shouldBe false

    "converting to a string" should:
      "return the correct string representation in normal mode" in:
        val card1 = Card(1, Color.RED, Symbol.OVAL, Shading.SOLID, false)
        val card2 = Card(2, Color.GREEN, Symbol.SQUIGGLE, Shading.OUTLINED, false)
        val card3 = Card(3, Color.BLUE, Symbol.DIAMOND, Shading.STRIPED, false)
        val triplet = Triplet(card1, card2, card3)
        triplet.toString shouldBe card1.toString + "+" + card2.toString + "+" + card3.toString
      "return the correct string representation in easy mode" in:
        val card1 = Card(1, Color.RED, Symbol.OVAL, Shading.SOLID, false)
        val card2 = Card(2, Color.GREEN, Symbol.SQUIGGLE, Shading.OUTLINED, false)
        val card3 = Card(3, Color.BLUE, Symbol.DIAMOND, Shading.STRIPED, false)
        val triplet = Triplet(card1, card2, card3)
        triplet.toStringEasy shouldBe card1.toStringEasy + "+" + card2.toStringEasy + "+" + card3.toStringEasy