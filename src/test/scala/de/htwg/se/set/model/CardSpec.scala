package de.htwg.se.set.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CardSpec extends AnyWordSpec with Matchers:
  
  "A card" when:
    "constructed" should:
      val card = Card(1, Color.RED, Symbol.OVAL, Shading.SOLID)
      "have a number" in:
        card.number shouldBe 1
      "have a color" in:
        card.color shouldBe Color.RED
      "have a symbol" in:
        card.symbol shouldBe Symbol.OVAL
      "have a shading" in:
        card.shading shouldBe Shading.SOLID
      "have a correct String representation in normal mode" in:
        card.toString shouldBe "1ROF"
      "have a correct String representation in easy mode" in:
        card.toStringEasy shouldBe "1RO"