package de.htwg.se.set.util

import de.htwg.se.set.controller.Event

trait Observer:
  
  def update(event: Event): Unit

trait Observable:
  
  var subscribers: Vector[Observer] = Vector()
  def add(s: Observer): Unit = subscribers = subscribers :+ s
  def remove(s: Observer): Unit = subscribers = subscribers.filterNot(o => o == s)
  def notifyObservers(event: Event): Unit = subscribers.foreach(o => o.update(event))