package de.htwg.se.set.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class DeckSpec extends AnyWordSpec with Matchers:

  "A Deck" when:
    val deck = new Deck(easy = true)
    val tableSinglePlayer = deck.tableCardsSinglePlayer(12)
    val tableMultiPlayer = deck.tableCards(12, List[Card](), List[Card]())
    "initialized in easy mode" should:
      "contain 27 unique cards with SOLID shading" in:
        deck.allCards.length shouldBe 27
      "ensure all cards have SOLID shading" in:
        deck.allCards.forall(_.shading == Shading.SOLID) shouldBe true
    "initialized in normal mode" should:
      val deckNormal = new Deck(easy = false)
      "contain 81 unique cards with all kinds of shading" in:
        deckNormal.allCards.length shouldBe 81
      "ensure the cards have all kinds of shading" in:
        deckNormal.allCards.map(_.shading).distinct should contain allElementsOf Shading.values

    "dealing cards for table in multi-player mode" should:
      "deal the correct number of cards" in:
        tableMultiPlayer.length shouldBe 12
      "deal unique cards" in:
        tableMultiPlayer.distinct.length shouldBe tableMultiPlayer.length

    "dealing cards for table in single-player mode" should:
      "deal the correct number of cards" in :
        tableSinglePlayer.length shouldBe 12
      "deal unique cards" in:
        tableSinglePlayer.distinct.length shouldBe tableSinglePlayer.length

    "selecting cards" should:
      "correctly mark cards as selected" in:
        val card1 = tableSinglePlayer.head
        val card2 = tableSinglePlayer(1)
        val card3 = tableSinglePlayer(2)
        val updatedTable = deck.selectCards(tableSinglePlayer, card1, card2, card3)
        updatedTable.count(_.selected) shouldBe 3

    "unselecting cards" should:
      "correctly mark all cards as unselected" in:
        val card1 = tableSinglePlayer.head
        val card2 = tableSinglePlayer(1)
        val card3 = tableSinglePlayer(2)
        val selectedTable = deck.selectCards(tableSinglePlayer, card1, card2, card3)
        val unselectedTable = deck.unselectCards(selectedTable)
        unselectedTable.count(_.selected) shouldBe 0

    "adding cards to players cards" should:
      "add a set of cards to the player's collection" in:
        val initialPlayersCards = List.empty[Card]
        val set = Triplet(deck.allCards.head, deck.allCards(1), deck.allCards(2))
        val updatedPlayersCards = deck.playersCardsAdd(initialPlayersCards, set)
        updatedPlayersCards should contain allOf (set.card1, set.card2, set.card3)

    "accessing a card by coordinate" should:
      val table = deck.allCards.take(12)
      val columns = 3
      val coordinate = "B2"
      val card = deck.cardAtCoordinate(table, coordinate, columns)
      "return the correct card" in:
        card shouldBe table(4)