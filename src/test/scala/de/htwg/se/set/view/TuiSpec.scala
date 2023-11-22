package de.htwg.se.set.view

import de.htwg.se.set.controller.Controller
import de.htwg.se.set.model.{Card, Deck, Game, Player, Settings, Triplet}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.io.StringReader

class TuiSpec extends AnyWordSpec with Matchers:

  "A Tui" when:
    val settings = Settings(1, false)
    val deck = Deck(false)
    val cards = deck.tableCardsSinglePlayer(12)
    val players = List[Player](Player(1, true, false, List[Triplet]()))
    val game = Game(4, deck, cards, List[Card](), players, None)
    val controller = Controller(settings, game)
    val tui = Tui(controller)
    "inputting a string" should:
      "return the trimmed string" in:
        val input = " test "
        Console.withIn(new StringReader(input)):
          tui.stringInput should be("test")

    "inputting an integer" should:
      "return the integer" in:
        val input = "42\n"
        Console.withIn(new StringReader(input)):
          tui.intInput should be(42)
      "ask again if the input is not an integer" in:
        val input = "invalid\n42\n"
        Console.withIn(new StringReader(input)):
          tui.intInput should be(42)

    "inputting an integer with min and max" should:
      "return the integer within bounds" in:
        val input = "42\n"
        Console.withIn(new StringReader(input)):
          tui.intInput(1, 100) should be(42)
      "ask again if the integer is out of bounds" in:
        val input = "0\n42\n"
        Console.withIn(new StringReader(input)):
          tui.intInput(1, 100) should be(42)

    "inputting coordinates" should:
      "return a list of coordinates when valid" in:
        val input = " A1  B2 C3 \n"
        Console.withIn(new StringReader(input)):
          tui.coordinatesInput should contain theSameElementsAs List("A1", "B2", "C3")
      "ask again if not all coordinates are different" in :
        val input = "A1 A1 B2\nA1 B2 C3\n"
        Console.withIn(new StringReader(input)):
          tui.coordinatesInput should contain theSameElementsAs List("A1", "B2", "C3")
      "ask again if the input is invalid" in:
        val input = "invalid\nA1 B2 C3\n"
        Console.withIn(new StringReader(input)):
          tui.coordinatesInput should contain theSameElementsAs List("A1", "B2", "C3")