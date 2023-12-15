package de.htwg.se.set.modelComponent

import de.htwg.se.set.model.{Color, Symbol, Shading}
import de.htwg.se.set.model.game.{Card, Triplet}
import de.htwg.se.set.{model, modelComponent}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TripletSpec extends AnyWordSpec with Matchers:

  "A Triplet" when:
    val card1 = Card(1, Color.RED, Symbol.OVAL, Shading.SOLID)
    val card2 = Card(2, Color.GREEN, Symbol.SQUIGGLE, Shading.OUTLINED)
    val card3 = Card(3, Color.BLUE, Symbol.DIAMOND, Shading.STRIPED)
    val tripletSet = Triplet(card1, card2, card3)
    val card4 = Card(1, Color.RED, Symbol.OVAL, Shading.SOLID)
    val card5 = Card(2, Color.RED, Symbol.OVAL, Shading.SOLID)
    val card6 = Card(2, Color.GREEN, Symbol.OVAL, Shading.SOLID)
    val tripletNoSet = Triplet(card4, card5, card6)

    "created with distinct cards" should:
      "be initialized correctly" in:
        tripletSet.card1 shouldBe card1
        tripletSet.card2 shouldBe card2
        tripletSet.card3 shouldBe card3
    "created with identical cards" should:
      "throw an IllegalArgumentException" in:
        val card = Card(1, Color.RED, Symbol.OVAL, Shading.SOLID)
        val exception = intercept[IllegalArgumentException] {
          Triplet(card, card, card)
        }
        exception.getMessage shouldBe "Cards must be different"

    "evaluating whether it's a set" should:
      "return true for a valid set" in:
        tripletSet.isSet shouldBe true
      "return false for an invalid set" in:
        tripletNoSet.isSet shouldBe false

    "converting to a string" should:
      "return the correct string representation" in:
        tripletSet.toString shouldBe card1.toString + "+" + card2.toString + "+" + card3.toString

    "be equal to another triplet with the same cards" in:
      tripletSet shouldEqual Triplet(card3, card2, card1)
    "not be equal to another triplet with different cards" in:
      tripletSet should not equal tripletNoSet
    "not be equal to an instance of another class" in:
      tripletSet should not equal card1