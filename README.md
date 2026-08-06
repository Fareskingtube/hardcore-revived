# Hardcore Revived

This is a fabric minecraft mod that tries to enhance the multiplayer hardcore experience by allowing you to revive your
dead
friends in a **Revival Altar** by **sacrificing** one of your **hearts**.

# What the mod adds

This mod allows you to revive your friends in hardcore by building a multiblock **(Revival Altar)** and placing your
heart in it.

## The Revival Altar

The Revival Altar is the center of this mod it has 2 main functions:

1. It passively gives you regeneration while within its range.
2. Allows you to revive your friends by placing a heart in it then getting your friend who's in spectator mode within
   its range.

## The Altar

[![Revival Altar Video](https://i.ibb.co/ynv4TLf5/image.png)](https://youtu.be/oQ9XKFr5kiY)

## Revival

[![Revival Altar Revival Video](https://i.ibb.co/BVvqgx4y/2026-08-06-14-42-15.png)](https://youtu.be/zcmzzAZ1ubY)

## Hardcore Heart

This item is the physical manifestation of your heart in your hand.
It can be obtained via **heart extractor**.
Once you have a heart it can be reinjected via **Heart Injector**,
gifted to a friend or thrown into lava.

[![Revival Altar Video](https://i.ibb.co/fYWgTYm4/2026-08-06-14-45-45.png)](https://youtu.be/Y6KFRJC33Tc)

## Resuscitatio Mortuorum

This is the in game guide book that shows you what each item does how to get it.
Also shows you a framework on how to build the [Altar Multiblock](#the-revival-altar) as shown in
the [provided video](#the-altar).

You should spawn with it when making a new world, but you can also craft it with a **book** and a **blood block**.

# Requirements

## Version

Currently Hardcore Revived only works on version 1.21.1 of minecraft.

## Mod Loader

Hardcore Revived only supports the [Fabric Mod Loader](https://fabricmc.net/) and
requires [Fabric API](https://github.com/FabricMC/fabric-api).

## Dependencies

These are mods you have to have in your `mods/` folder for the mod to work correctly:

* [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api/)
  Version [0.116.12+1.21.1](https://www.curseforge.com/minecraft/mc-mods/fabric-api/files/8073321) or higher.
* [Patchouli (Fabric)](https://www.curseforge.com/minecraft/mc-mods/patchouli-fabric)
  Version [1.21.1-93](https://www.curseforge.com/minecraft/mc-mods/patchouli-fabric/files/7730941) or higher.
* [JEI](https://www.curseforge.com/minecraft/mc-mods/jei) (Optional but useful for seeing recipes).

## Incompatibility

This mod is incompatible with some mod that modifies your `MAX_HEALTH` player attribute.

Known incompatibilities:

* [Apotheosis](https://www.curseforge.com/minecraft/mc-mods/apotheosis)

Any mod that allows you to increase then increase your `MAX_HEALTH` player attribute at will allows for farming hearts
for infinite health.
Any mod that **only** allows you to only increase or **only** decrease your `MAX_HEALTH` player attribute **should** be
fine.

# Installation

To install the mod you just need to put the `.jar` file in your
`YOUR_MINECRAFT_INSTANCE/mods/` folder with the [Dependency Mods](#dependencies) on a [Fabric](https://fabricmc.net/)
installation.

# AI usage

AI was usage was mostly limited to debugging and explaining some concepts/finding documentation since up-to-date
documentation for mod development is fairly limited.

The known places where generative was used to write code:

1. Spawning particle methods in `src/main/java/net/fareskingtube/block/entity/custom/RevivalAltarBlockentity`

# Modpack Policy

You're allowed to use this mod in your modpack. You don't have to ask permission .


