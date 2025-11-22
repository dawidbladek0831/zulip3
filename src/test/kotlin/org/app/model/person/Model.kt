package org.app.model.person

internal fun PersonAddress.Companion.maximal(
    id: Long? = null, street: String = "Wall street"
): PersonAddress = PersonAddress(
    street = street
).apply { this.id = id }

internal fun PersonProfile.Companion.maximal(
    id: Long? = null, nickname: String = "Alli"
): PersonProfile = PersonProfile(
    nickname = nickname
).apply { this.id = id }

internal fun Person.Companion.maximal(
    id: Long? = null,
    name: String = "Ala",
    address: PersonAddress = PersonAddress.maximal(),
    profile: PersonProfile? = PersonProfile.maximal(),
): Person = Person(
    name = name, address = address, profile = profile
).apply { this.id = id }