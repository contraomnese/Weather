package com.contraomnese.weather.data.mappers

/**
 * Bidirectional mapper between persistence (Entity) and domain models.
 */
interface BiDirectMapper<E, M> {
    fun toDomain(entity: E): M
    fun toEntity(model: M): E
}

/**
 * Unidirectional mapper for one-way transformations, e.g. API → Domain.
 */
interface UniDirectMapper<in E, out M> {
    fun toDomain(entity: E): M
}

fun <E, M> BiDirectMapper<E, M>.toDomainList(entities: List<E>): List<M> =
    entities.map { toDomain(it) }

fun <E, M> BiDirectMapper<E, M>.toEntityList(models: List<M>): List<E> =
    models.map { toEntity(it) }

fun <E, M> UniDirectMapper<E, M>.toDomainList(entities: List<E>): List<M> =
    entities.map { toDomain(it) }