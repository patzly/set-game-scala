package de.htwg.se.set.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CardSpec extends AnyWordSpec with Matchers {
  "A normal Card" when { "new" should {
    val card = Card(1, Color.RED, Symbol.OVAL, Shading.SOLID, false)
    "have a number"  in {
      card.number should be(1)
    }
    "have a color" in {
      card.color should be(Color.RED)
    }
    "have a symbol" in {
      card.symbol should be(Symbol.OVAL)
    }
    "have a shading" in {
      card.shading should be(Shading.SOLID)
    }
    "have a short String representation" in {
      card.toString should be("1ROF")
    }
  }}

  "An easy Card" when {
    "new" should {
      val card = Card(1, Color.RED, Symbol.OVAL, Shading.SOLID, true)
      "have a number" in {
        card.number should be(1)
      }
      "have a color" in {
        card.color should be(Color.RED)
      }
      "have a symbol" in {
        card.symbol should be(Symbol.OVAL)
      }
      "have a shading" in {
        card.shading should be(Shading.SOLID)
      }
      "have a short String representation" in {
        card.toString should be("1RO")
      }
    }
  }
}