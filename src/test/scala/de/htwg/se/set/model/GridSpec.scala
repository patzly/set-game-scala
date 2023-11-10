package de.htwg.se.set.model

import de.htwg.se.set.util.PrintUtil
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GridSpec extends AnyWordSpec with Matchers:
  
  "A Grid" when:
    "constructed in normal mode" should:
      "have a correct String representation" in:
        val card1 = Card(1, Color.RED, Symbol.OVAL, Shading.SOLID, selected = false)
        val card2 = Card(2, Color.GREEN, Symbol.SQUIGGLE, Shading.OUTLINED, selected = true)
        val cards = List(card1, card2, card1, card2, card1, card2)
        val grid = Grid(2, cards, false)
        grid.toString should include(PrintUtil.yellow("1ROF"))
        grid.toString should include(PrintUtil.cyan("2GSL"))
        grid.toString should include("1")
        grid.toString should include("2")
        grid.toString should include("A")
        grid.toString should include("B")
    "constructed in easy mode" should:
      "have a correct String representation" in:
        val card1 = Card(1, Color.RED, Symbol.OVAL, Shading.SOLID, selected = false)
        val card2 = Card(2, Color.GREEN, Symbol.SQUIGGLE, Shading.OUTLINED, selected = true)
        val card3 = Card(3, Color.BLUE, Symbol.DIAMOND, Shading.STRIPED, selected = false)
        val cards = List(card1, card2, card3, card1, card2, card3, card1, card2, card3)
        val grid = Grid(3, cards, true)
        grid.toString should include(PrintUtil.yellow("1RO"))
        grid.toString should include(PrintUtil.cyan("2GS"))
        grid.toString should include(PrintUtil.yellow("3BD"))
        grid.toString should include("1")
        grid.toString should include("2")
        grid.toString should include("A")
        grid.toString should include("B")
        grid.toString should include("C")
    "constructed with a grid size unequal to the amount of cards" should:
      "throw an IllegalArgumentException" in:
        val card1 = Card(1, Color.RED, Symbol.OVAL, Shading.SOLID, selected = false)
        val card2 = Card(2, Color.GREEN, Symbol.SQUIGGLE, Shading.OUTLINED, selected = true)
        val card3 = Card(3, Color.BLUE, Symbol.DIAMOND, Shading.STRIPED, selected = false)
        val cards = List(card1, card2, card3, card1, card2, card3)
        val exception = intercept[IllegalArgumentException] {
          Grid(3, cards, true)
        }
        exception.getMessage shouldBe "Amount of cards has to be equal to the grid size"