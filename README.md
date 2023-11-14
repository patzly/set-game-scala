<img src="assets/header.png" />

# SET Game

[![License: GPLv3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl.txt) [![Tests](https://github.com/patzly/set-game-scala/actions/workflows/scala.yml/badge.svg)](https://github.com/patzly/set-game-scala/actions/workflows/scala.yml) [![Coverage](https://coveralls.io/repos/github/patzly/set-game-scala/badge.svg?branch=main)](https://coveralls.io/github/patzly/set-game-scala?branch=main)

This is an unofficial Scala version of the SET game for Software Engineering classes at HTWG Konstanz.

The object of the game is to identify a 'SET' of three cards from 12 cards placed face up on the table. Each card has four features:

* **Number:** 1, 2, 3
* **Symbol:** oval, squiggle, diamond
* **Shading:** solid, striped, outlined
* **Color:** red, green, purple

A set contains of three cards in which each of the cards' features, looked at one-by-one, are the same on each card, or, are different on each card.
If two cards are the same and one card is different in any feature, then it is not a SET. A SET must be either all the same or all different in each individual feature.

## Features

* Singleplayer/Multiplayer mode
* "Easy start" mode
* Interactive GUI, game state representation as text-based UI

## License

Copyright &copy; 2023 Matthias Elbel & Patrick Zedler. All rights reserved.

[GNU General Public License version 3](https://www.gnu.org/licenses/gpl.txt)

> SET Game is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
>
> SET Game is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
