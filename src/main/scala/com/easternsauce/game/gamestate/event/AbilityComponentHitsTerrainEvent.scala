package com.easternsauce.game.gamestate.event
import com.easternsauce.game.core.CoreGame
import com.easternsauce.game.gamestate.GameState
import com.easternsauce.game.gamestate.ability.{AbilityComponent, AbilityState}
import com.easternsauce.game.gamestate.id.{AreaId, GameEntityId}
import com.softwaremill.quicklens.ModifyPimp

case class AbilityComponentHitsTerrainEvent(
    abilityComponentId: GameEntityId[AbilityComponent],
    areaId: AreaId
) extends AreaGameStateEvent {

  override def applyToGameState(
      gameState: GameState
  )(implicit game: CoreGame): GameState = {

    if (
      gameState.abilityComponents
        .contains(abilityComponentId)
    ) {
      val abilityComponent = gameState.abilityComponents(abilityComponentId)

      if (gameState.abilities.contains(abilityComponent.abilityId)) {
        val ability = game.gameState.abilities(abilityComponent.abilityId)

        if (ability.currentState == AbilityState.Active) {
          gameState
            .modify(_.abilityComponents)
            .usingIf(abilityComponent.isDestroyedOnContact)(
              _.removed(abilityComponentId)
            )
            .markAbilityAsFinishedIfNoComponentsExist(
              abilityComponent.abilityId
            )
        } else {
          gameState
        }
      } else {
        gameState
      }
    } else {
      gameState
    }

  }
}
