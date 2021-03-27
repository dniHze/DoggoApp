package dev.kaitei.doggo.api.adapter

import com.squareup.moshi.*
import dev.kaitei.doggo.api.Breeds
import dev.kaitei.doggo.model.Breed
import dev.kaitei.doggo.model.SubBreed
import java.lang.IllegalStateException

class BreedAdapter : JsonAdapter<Breeds>() {

    override fun fromJson(reader: JsonReader): Breeds? {
        return when (val token = reader.peek()) {

            JsonReader.Token.BEGIN_OBJECT -> {
                val breeds = mutableListOf<Breed>()
                reader.beginObject()
                while (reader.hasNext()) {
                    val breedName = reader.nextName()
                    val subBreeds = mutableListOf<SubBreed>()
                    reader.beginArray()
                    while (reader.hasNext()) {
                        val subBreedName = reader.nextString()
                        val id = "$breedName/$subBreedName"
                        subBreeds += SubBreed(
                            id = id,
                            name = subBreedName,
                            parentName = breedName
                        )
                    }
                    reader.endArray()
                    breeds += Breed(
                        id = breedName,
                        name = breedName,
                        subs = subBreeds
                    )
                }
                reader.endObject()
                breeds.toList()
            }
            JsonReader.Token.NULL -> {
                null
            }
            else -> throw IllegalStateException(
                "Expected BEGIN_OBJECT or NULL token, but got '$token' instead"
            )
        }
    }

    override fun toJson(writer: JsonWriter, value: Breeds?) {
        if (value == null) {
            writer.nullValue()
            return
        }

        writer.beginObject()
        value.forEach { breed ->
            writer.name(breed.id)
            writer.beginArray()
            breed.subs.forEach {
                writer.value(it.name)
            }
            writer.endArray()
        }
        writer.endObject()
    }
}