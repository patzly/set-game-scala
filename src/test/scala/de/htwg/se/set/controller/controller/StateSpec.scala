package de.htwg.se.set.controller.controller

import de.htwg.se.set.controller.controller.base.{InvalidAction, *}
import de.htwg.se.set.controller.controller.stub.ControllerStub
import de.htwg.se.set.util.InputUtil
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class StateSpec extends AnyWordSpec with Matchers:

  private val controller = ControllerStub()

  "A SettingsState" should:
    val state = SettingsState(controller)

    "handle valid number inputs correctly" in:
      val actionFromInput1 = state.actionFromInput("1")
      actionFromInput1 shouldBe a [StartGameAction]

      val actionFromInput2 = state.actionFromInput("2")
      actionFromInput2 shouldBe a [GoToPlayerCountAction]

      val actionFromInput3 = state.actionFromInput("3")
      actionFromInput3 shouldBe a [SwitchEasyAction]

      val actionFromHandleInput1 = state.handleInput(InputUtil.UndoInput)
      actionFromHandleInput1 shouldBe a [UndoAction]

      val actionFromHandleInput2 = state.handleInput(InputUtil.RedoInput)
      actionFromHandleInput2 shouldBe a [RedoAction]

      val actionFromHandleInput3 = state.handleInput(InputUtil.InvalidInput())
      actionFromHandleInput3 shouldBe a [InvalidAction]

    "handle invalid inputs correctly" in:
      val actionFromInput = state.actionFromInput("invalid")
      actionFromInput shouldBe a [InvalidAction]

      val actionFromHandleInput = state.handleInput(InputUtil.InvalidInput())
      actionFromHandleInput shouldBe a [InvalidAction]

  "A ChangePlayerCountState" should:
    val state = ChangePlayerCountState(controller)

    "handle a valid number input correctly" in:
      val actionFromInput = state.actionFromInput("5")
      actionFromInput shouldBe a[ChangePlayerCountAction]

    "handle an invalid input correctly" in:
      val actionFromInput = state.actionFromInput("invalid")
      actionFromInput shouldBe a[InvalidAction]

  "A SelectPlayerState" should:
    val state = SelectPlayerState(controller)

    "handle a valid number input for player selection correctly" in:
      val actionFromInput = state.actionFromInput("1")
      actionFromInput shouldBe a[SelectPlayerAction]

    "handle input '0' correctly" in:
      val actionFromInput = state.actionFromInput("0")
      actionFromInput shouldBe a[AddColumnAction]

    "handle input 'e' correctly" in:
      val actionFromInput = state.actionFromInput("e")
      actionFromInput shouldBe a[ExitAction]

    "handle an invalid input correctly" in:
      val actionFromInput = state.actionFromInput("invalid")
      actionFromInput shouldBe a[InvalidAction]

  "A GameState" should:
    val state = GameState(controller)

    "handle valid coordinates input correctly" in:
      val actionFromInput = state.actionFromInput("A1 B2 C3")
      actionFromInput shouldBe a[SelectCardsAction]

    "handle input 'e' correctly" in:
      val actionFromInput = state.actionFromInput("e")
      actionFromInput shouldBe a[ExitAction]

    "handle an invalid input correctly" in:
      val actionFromInput = state.actionFromInput("invalid")
      actionFromInput shouldBe a[InvalidAction]

  "A GameEndState" should:
    val state = GameEndState(controller)

    "handle input 'f' correctly" in:
      val actionFromInput = state.actionFromInput("f")
      actionFromInput shouldBe a[ExitAction]

    "handle an invalid input correctly" in:
      val actionFromInput = state.actionFromInput("invalid")
      actionFromInput shouldBe a[InvalidAction]