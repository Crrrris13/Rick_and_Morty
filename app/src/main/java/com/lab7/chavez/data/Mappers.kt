package com.lab7.chavez.data

fun CharacterEntity.toModel(): Character = Character(id, name, status, species, gender, image, isFavorite)
fun Character.toEntity(): CharacterEntity = CharacterEntity(id, name, status, species, gender, image, isFavorite)

fun LocationEntity.toModel(): Location = Location(id, name, type, dimension)
fun Location.toEntity(): LocationEntity = LocationEntity(id, name, type, dimension)