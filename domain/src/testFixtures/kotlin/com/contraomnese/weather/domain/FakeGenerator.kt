package com.contraomnese.weather.domain

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

fun generateFake(kClass: KClass<*>): Any {
    val random = Random(System.nanoTime())

    when (kClass) {
        // -- primitive--
        String::class -> return randomString(random)
        Int::class -> return random.nextInt(0, 100)
        Long::class -> return random.nextLong(0, 1000)
        Double::class -> return random.nextDouble(0.0, 100.0)
        Float::class -> return random.nextFloat() * 100
        Boolean::class -> return random.nextBoolean()
        // -- non primitive--
        LocalDateTime::class -> return LocalDateTime(
            year = random.nextInt(1900, 2000),
            monthNumber = random.nextInt(1, 12),
            dayOfMonth = random.nextInt(1, 29),
            hour = random.nextInt(0, 23),
            minute = random.nextInt(0, 59),
            second = random.nextInt(0, 59),
            nanosecond = 0
        )

        LocalTime::class -> return LocalTime(
            hour = random.nextInt(0, 23),
            minute = random.nextInt(0, 59),
        )

        TimeZone::class -> return TimeZone.currentSystemDefault()
    }

    if (kClass.java.isEnum) {
        return kClass.java.enumConstants.random()
    }

    if (kClass.isSealed) {
        val subclass = kClass.sealedSubclasses.random()
        return subclass.objectInstance ?: generateFake(subclass)
    }

    if (kClass.isValue) {
        val primary = kClass.primaryConstructor ?: error("Value class $kClass must have a primary constructor")
        val param = primary.parameters.single()
        val underlyingType = param.type.classifier as KClass<*>
        return primary.call(generateFake(underlyingType))
    }

    val constructor = kClass.constructors.firstOrNull()
        ?: throw IllegalArgumentException("Class must have a primary constructor or handle it explicitly: $kClass")

    val args = constructor.parameters.associateWith { param ->
        val type = param.type
        val classifier = type.classifier as? KClass<*> ?: error("Classifier is not a KClass")

        if (classifier == List::class) {
            val listType = type.arguments.first().type
                ?: return@associateWith emptyList<Any>()

            List(Random.nextInt(1, 5)) {
                generateFake(listType.classifier as KClass<*>)
            }
        } else {
            generateFake(classifier)
        }
    }

    return constructor.callBy(args)
}

private fun randomString(random: Random): String {
    val chars = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return (1..10).map { chars.random(random) }.joinToString("")
}