package dev.kaitei.doggo.model

import java.util.Locale

data class Breed(
    override val id: String,
    val name: String,
    val subs: List<SubBreed>
) : BaseBreed, Comparable<Breed> {

    val displayName: String = name.capitalize(Locale.ENGLISH)
    override val fullDisplayName: String = displayName

    override fun compareTo(other: Breed): Int = name.compareTo(other.name)
}
