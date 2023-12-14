package de.htwg.se.set.util

import de.htwg.se.set.util.InputUtil.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class InputUtilSpec extends AnyWordSpec with Matchers:

  "InputUtil" when:
    "processing string inputs" should:
      "correctly handle undo, redo, finish, exit, and text inputs" in:
        InputUtil.stringInput("u", true, false, false, false) shouldBe UndoInput
        InputUtil.stringInput("u", false, false, false, false) shouldBe a[InvalidInput]
        InputUtil.stringInput("r", false, true, false, false) shouldBe RedoInput
        InputUtil.stringInput("r", false, false, false, false) shouldBe a[InvalidInput]
        InputUtil.stringInput("f", false, false, true, false) shouldBe FinishInput
        InputUtil.stringInput("f", false, false, false, false) shouldBe TextInput("f")
        InputUtil.stringInput("e", false, false, false, true) shouldBe ExitInput
        InputUtil.stringInput("e", false, false, false, false) shouldBe TextInput("e")
        InputUtil.stringInput("test", false, false, false, false) shouldBe TextInput("test")

    "processing integer inputs" should:
      "handle valid and invalid integer strings" in:
        InputUtil.intInput("42", false, false, false) shouldBe NumberInput(42)
        InputUtil.intInput("invalid", false, false, false) shouldBe a[InvalidInput]
        InputUtil.intInput("u", true, false, false) shouldBe UndoInput
        InputUtil.intInput("r", false, true, false) shouldBe RedoInput
        InputUtil.intInput("e", false, false, true) shouldBe ExitInput
      "handle integer inputs within a specific range" in:
        InputUtil.intInput("5", 1, 10, false, false, false) shouldBe NumberInput(5)
        InputUtil.intInput("0", 1, 10, false, false, false) shouldBe a[InvalidInput]
        InputUtil.intInput("u", 1, 10, true, false, false) shouldBe UndoInput
        InputUtil.intInput("r", 1, 10, false, true, false) shouldBe RedoInput
        InputUtil.intInput("e", 1, 10, false, false, true) shouldBe ExitInput

    "processing coordinates input" should:
      "handle valid and invalid coordinates" in:
        val coordinates = List("A1", "B2", "C3")
        InputUtil.coordinatesInput("A1 B2 C3", false, false) shouldBe CoordinatesInput(coordinates)
        InputUtil.coordinatesInput("invalid", false, false) shouldBe a[InvalidInput]
        InputUtil.coordinatesInput("A1 A1 A1", false, false) shouldBe a[InvalidInput]
        InputUtil.coordinatesInput("u", true, false) shouldBe UndoInput
        InputUtil.coordinatesInput("r", false, true) shouldBe RedoInput
        InputUtil.coordinatesInput("e", false, false) shouldBe ExitInput

    "processing finish input" should:
      "handle finish and invalid inputs" in:
        InputUtil.finishInput("f", false, false) shouldBe FinishInput
        InputUtil.finishInput("invalid", false, false) shouldBe a[InvalidInput]
        InputUtil.finishInput("u", true, false) shouldBe UndoInput
        InputUtil.finishInput("r", false, true) shouldBe RedoInput