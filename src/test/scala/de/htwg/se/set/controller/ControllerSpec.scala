package de.htwg.se.set.controller

import de.htwg.se.set.model._
import de.htwg.se.set.util._
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.BeforeAndAfter

class ControllerSpec extends AnyWordSpec with Matchers with BeforeAndAfter:

  var settings: Settings = Settings(1, false)
  var game: Game = _
  var controller: Controller = _

  before:
    settings = Settings(playerCount = 2, easy = false)
    val columns = 3
    val deck = Deck(false)
    val cards = deck.tableCards(columns, List(), List())
    game = Game(columns, deck, cards, List(), List())
    controller = Controller(settings, game)

  "A Controller" when:
    "new" should:
      "have initial settings and game state" in:
        controller.settings shouldBe settings
        controller.game shouldBe game
      "have a correct settings string representation" in:
        controller.settingsToString should include("2 players")
        controller.settingsToString should include("normal mode")
      "have a correct game string representation" in:
        controller.gameToString should include("1")
        controller.gameToString should include("2")
        controller.gameToString should include("3")
        controller.gameToString should include("A")
        controller.gameToString should include("B")
        controller.gameToString should include("C")

    "changing settings" should:
      "update the player count and notify observers" in:
        var notified = false
        controller.add((event: Event) =>
          event should be(Event.SETTINGS_CHANGED)
          notified = true
        )
        controller.setPlayerCount(3)
        controller.settings.playerCount should be(3)
        notified should be(true)

      "update the difficulty and notify observers" in:
        var notified = false
        controller.add((event: Event) =>
          event should be(Event.SETTINGS_CHANGED)
          notified = true
        )
        controller.setEasy(true)
        controller.settings.easy should be(true)
        notified should be(true)

      "update the columns and notify observers" in:
        var notified = false
        controller.add((event: Event) =>
          event should be(Event.COLUMNS_CHANGED)
          notified = true
        )
        controller.setColumns(1)
        controller.game.columns should be(1)
        notified should be(true)

      "add column and notify observers" in:
        var notified = false
        controller.add((event: Event) =>
          event should be(Event.COLUMNS_CHANGED)
          notified = true
        )
        controller.addColumn()
        controller.game.columns should be(4)
        notified should be(true)

      "remove column and notify observers" in:
        var notified = false
        controller.add((event: Event) =>
          event should be(Event.COLUMNS_CHANGED)
          notified = true
        )
        controller.removeColumn()
        controller.game.columns should be(2)
        notified should be(true)

    "modifying the game" should:
      "update columns when adding and notify observers" in:
        var notified = false
        controller.add((event: Event) =>
          event should be(Event.COLUMNS_CHANGED)
          notified = true
        )
        controller.addColumn()
        controller.game.columns should be(4)
        notified should be(true)

      "update columns when removing and notify observers" in:
        var notified = false
        controller.add((event: Event) =>
          event should be(Event.COLUMNS_CHANGED)
          notified = true
        )
        controller.removeColumn()
        controller.game.columns should be(2)
        notified should be(true)

      "set new deck without notification" in:
        val newDeck = Deck(false)
        controller.setDeck(newDeck)
        controller.game.deck shouldBe newDeck

      "update table cards and notify observers" in:
        var notified = false
        val deck = Deck(false)
        val newCards = deck.tableCards(3, List(), List())
        controller.add((event: Event) =>
          event should be(Event.CARDS_CHANGED)
          notified = true
        )
        controller.setTableCards(newCards)
        controller.game.tableCards should be(newCards)
        notified should be(true)

      "update players cards" in:
        val deck = Deck(false)
        val newCards = deck.tableCards(1, List(), List())
        controller.setPlayersCards(newCards)
        controller.game.playersCards should be(newCards)

      "update players and notify observers" in:
        var notified = false
        val newPlayers = List(Player(1, false, true, List()), Player(2, false, true, List()))
        controller.add((event: Event) =>
          event should be(Event.PLAYERS_CHANGED)
          notified = true
        )
        controller.setPlayers(newPlayers)
        controller.game.players should be(newPlayers)
        notified should be(true)