package dev.kaitei.doggo.model

import java.util.Locale

data class SubBreed(
    override val id: String,
    val name: String,
    val parentName: String
) : BaseBreed, Comparable<SubBreed> {

    val displayName: String = name.capitalize(Locale.ENGLISH)
    override val fullDisplayName: String = "$displayName ${parentName.capitalize(Locale.ENGLISH)}"

    override fun compareTo(other: SubBreed): Int = name.compareTo(other.name)
}
