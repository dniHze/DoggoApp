package dev.kaitei.doggo.api.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import dev.kaitei.doggo.api.Breeds
import dev.kaitei.doggo.model.Breed
import dev.kaitei.doggo.model.SubBreed

class BreedAdapter {

    @FromJson
    fun fromJson(value: Map<String, List<String>>): Breeds =
        value.map { (breedName, subBreedNames) ->
            val subBreeds = subBreedNames.map { subBreedName ->
                val id = "$breedName/$subBreedName"
                SubBreed(
                    id = id,
                    name = subBreedName,
                    parentName = breedName
                )
            }
            Breed(
                id = breedName,
                name = breedName,
                subs = subBreeds
            )
        }

    @ToJson
    fun toJson(breeds: Breeds): Map<String, List<String>> =
        breeds.map { breed ->
            breed.id to breed.subs.map { subBreed -> subBreed.name }
        }.toMap()
}