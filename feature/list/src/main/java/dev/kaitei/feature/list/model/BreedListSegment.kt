package dev.kaitei.feature.list.model

import dev.kaitei.doggo.model.Breed

data class BreedListSegment(
    val segmentCapitalLetter: String,
    val breeds: List<Breed>,
) : Comparable<BreedListSegment> {
    override fun compareTo(other: BreedListSegment): Int =
        segmentCapitalLetter.compareTo(other.segmentCapitalLetter)
}
