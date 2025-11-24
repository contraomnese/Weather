package com.contraomnese.weather.domain

import kotlin.random.Random
import kotlin.reflect.KClass

fun generateFake(kClass: KClass<*>): Any {

    val random = Random(Random.nextInt(0, 4000))

    val constructor = kClass.constructors.firstOrNull()
        ?: throw IllegalArgumentException("Class must have a primary constructor: $kClass")

    val args = constructor.parameters.associateWith { param ->
        val type = param.type
        when (val classifier = type.classifier) {
            String::class -> randomString(random)
            Int::class -> random.nextInt(0, 100)
            Long::class -> random.nextLong(0, 1000)
            Double::class -> random.nextDouble(0.0, 100.0)
            Float::class -> random.nextFloat() * 100
            Boolean::class -> random.nextBoolean()

            List::class -> {
                val listType = type.arguments.first().type
                    ?: return@associateWith emptyList<Any>()

                List(Random.nextInt(1, 5)) { generateFake(listType.classifier as KClass<*>) }
            }

            is KClass<*> -> {
                if ((classifier as KClass<*>).isData) {
                    generateFake(classifier)
                } else {
                    error("Unsupported class type: $classifier")
                }
            }

            else -> error("Unsupported classifier: $classifier")
        }
    }

    return constructor.callBy(args)
}

private fun randomString(random: Random): String {
    val length = random.nextInt(3, 10)
    val allowed = "abcdefghijklmnopqrstuvwxyz"
    return (1..length)
        .map { allowed.random() }
        .joinToString("")
}