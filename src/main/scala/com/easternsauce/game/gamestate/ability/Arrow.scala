package com.easternsauce.game.gamestate.ability
import com.easternsauce.game.core.CoreGame

case class Arrow(params: AbilityParams) extends Ability {

  override def init()(implicit game: CoreGame): Ability = {
    game.gameplay.entityCreators.scheduleAbilityComponentToCreate(
      params.id,
      AbilityComponentType.ArrowComponent,
      params.currentAreaId,
      params.creatureId,
      params.pos,
      params.facingVector,
      params.damage
    )
    this
  }

  override def update()(implicit game: CoreGame): Ability = {
    this
  }

  override def copy(params: AbilityParams): Ability = Arrow(params)

}
