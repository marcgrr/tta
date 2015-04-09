package models

import models.PlayerState

import collection.immutable

case class DeltaPlayerState(
    newBuildings: immutable.Seq[Building],
    newTechs: immutable.Seq[Tech],
    newCivilHand: immutable.Seq[Card],
    removedCivilHand: immutable.Seq[Card],
    population: Int,
    ore: Int,
    food: Int,
    science: Int) {

  def +(other: DeltaPlayerState): DeltaPlayerState = {
    DeltaPlayerState(
      newBuildings = newBuildings ++ other.newBuildings,
      newTechs = newTechs ++ other.newTechs,
      newCivilHand = newCivilHand ++ other.newCivilHand,
      removedCivilHand = removedCivilHand ++ other.removedCivilHand,
      population = population + other.population,
      ore = ore + other.ore,
      food = food + other.food,
      science = science + other.science)
  }

  def applyDeltaPlayerState(playerState: PlayerState): PlayerState = {
    PlayerState(
      buildings = playerState.buildings ++ this.newBuildings,
      techs = playerState.techs ++ this.newTechs,
      civilHand = (playerState.civilHand ++ this.newCivilHand) diff this.removedCivilHand,
      population = playerState.population + this.population,
      ore = playerState.ore + this.ore,
      food = playerState.food + this.food,
      science = playerState.science + this.science)
  }

}

object DeltaPlayerState {

  val empty = DeltaPlayerState(
    newBuildings = List.empty,
    newTechs = List.empty,
    newCivilHand = List.empty,
    removedCivilHand = List.empty,
    population = 0,
    ore = 0,
    food = 0,
    science = 0)
}