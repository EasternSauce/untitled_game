package com.easternsauce.game.gamestate.creature

import com.easternsauce.game.gamestate.SimpleTimer
import com.easternsauce.game.gamestate.ability.AbilityType
import com.easternsauce.game.gamestate.ability.AbilityType.AbilityType
import com.easternsauce.game.gamestate.creature.CreatureAnimationType.CreatureAnimationType
import com.easternsauce.game.gamestate.creature.CreatureType.CreatureType
import com.easternsauce.game.gamestate.creature.PrimaryWeaponType.PrimaryWeaponType
import com.easternsauce.game.gamestate.creature.SecondaryWeaponType.SecondaryWeaponType
import com.easternsauce.game.gamestate.id.{AreaId, GameEntityId}
import com.easternsauce.game.math.Vector2f

case class CreatureParams(
    id: GameEntityId[Creature],
    currentAreaId: AreaId,
    pos: Vector2f,
    velocity: Vector2f = Vector2f(0, 0),
    destination: Vector2f,
    lastPos: Vector2f,
    textureSize: Int,
    spriteVerticalShift: Float,
    bodyRadius: Float,
    isPlayer: Boolean,
    baseSpeed: Float,
    life: Float,
    maxLife: Float,
    damage: Float,
    facingVector: Vector2f = Vector2f(1, 0),
    texturePaths: Map[CreatureAnimationType, String],
    animationDefinition: AnimationDefinition,
    attackRange: Float,
    primaryWeaponType: PrimaryWeaponType,
    secondaryWeaponType: SecondaryWeaponType,
    isRenderBodyOnly: Boolean,
    animationTimer: SimpleTimer = SimpleTimer(isRunning = true),
    attackAnimationTimer: SimpleTimer = SimpleTimer(isRunning = false),
    deathAnimationTimer: SimpleTimer = SimpleTimer(isRunning = false),
    isRespawnDelayInProgress: Boolean = false,
    isDestinationReached: Boolean = true,
    abilityCooldownTimers: Map[AbilityType, SimpleTimer] =
      AbilityType.values.toList
        .map(abilityType => abilityType -> SimpleTimer(isRunning = false))
        .toMap,
    spawnPointId: Option[String],
    creatureType: CreatureType,
    recentlyHitTimer: SimpleTimer = SimpleTimer(isRunning = false)
) {}
