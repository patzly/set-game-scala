package de.htwg.se.set.model.game

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GameSpec extends AnyWordSpec with Matchers:

  "A Game" when:
    "initialized" should:
      val deck = Deck(false)
      val cards = deck.tableCardsSinglePlayer(4)
      val players = List[Player](Player(1, true, false, List[Triplet]()))
      val game = Game(4, deck, cards, List[Card](), players, None)
      "have a correct String representation" in:
        val expectedString = "\n" + Grid(4, cards, false)
        game.toString shouldBe expectedString