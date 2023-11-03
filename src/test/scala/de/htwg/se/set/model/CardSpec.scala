package de.htwg.se.set.model

import de.htwg.se.set.util.PrintUtil
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CardSpec extends AnyWordSpec with Matchers:

  "A Card" should:
    "be able to be selected using the select function" in:
      val card = Card(1, Color.RED, Symbol.OVAL, Shading.SOLID, selected = false)
      val selectedCard = card.select
      selectedCard.selected shouldBe true

    "be able to be unselected using the unselect function" in:
      val card = Card(1, Color.RED, Symbol.OVAL, Shading.SOLID, selected = true)
      val unselectedCard = card.unselect
      unselectedCard.selected shouldBe false

    "be equal to another card with the same number, color, symbol, and shading, regardless of selection status" in:
      val card1 = Card(1, Color.RED, Symbol.OVAL, Shading.SOLID, selected = true)
      val card2 = Card(1, Color.RED, Symbol.OVAL, Shading.SOLID, selected = false)
      card1 shouldEqual card2
    "not be equal to another card with different number, color, symbol, or shading" in:
      val card1 = Card(1, Color.RED, Symbol.OVAL, Shading.SOLID, selected = false)
      val card2 = Card(2, Color.GREEN, Symbol.SQUIGGLE, Shading.STRIPED, selected = false)
      card1 should not equal card2

    "produce a string representation with color when toString is invoked" in:
      val selectedCard = Card(1, Color.RED, Symbol.OVAL, Shading.SOLID, selected = true)
      val unselectedCard = Card(1, Color.RED, Symbol.OVAL, Shading.SOLID, selected = false)
      selectedCard.toString shouldEqual PrintUtil.cyan("1ROF")
      unselectedCard.toString shouldEqual PrintUtil.yellow("1ROF")

    "produce a simple string representation without shading when toStringEasy is invoked" in:
      val shading = Shading.SOLID
      val card = Card(1, Color.RED, Symbol.OVAL, shading, selected = true)
      card.toStringEasy should not include shading.toString