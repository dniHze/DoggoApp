package dev.kaitei.doggo.api.adapter

import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.kaitei.doggo.model.Breed
import dev.kaitei.doggo.model.SubBreed
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class BreedAdapterTest {

    private val moshi = Moshi.Builder()
        .add(BreedAdapter())
        .addLast(KotlinJsonAdapterFactory())
        .build()

    @Nested
    @DisplayName("fromJson")
    inner class FromJson {

        @Test
        fun `empty object serializes as empty list`() {
            val json = "{}"
            val type = Types.newParameterizedType(List::class.java, Breed::class.java)
            val adapter: JsonAdapter<List<Breed>> = moshi.adapter(type)
            val result = adapter.fromJson(json)
            assertNotNull(result)
            assertEquals(emptyList<Breed>(), result)
        }

        @Test
        fun `object with one breed serializes as non empty list with one breed`() {
            val json = """{"foo":[ ]}"""
            val type = Types.newParameterizedType(List::class.java, Breed::class.java)
            val adapter: JsonAdapter<List<Breed>> = moshi.adapter(type)
            val result = adapter.fromJson(json)
            assertNotNull(result)
            result!!
            assertTrue(result.isNotEmpty())
        }

        @Test
        fun `object with one breed serializes to breed with same name`() {
            val json = """{"foo":[ ]}"""
            val type = Types.newParameterizedType(List::class.java, Breed::class.java)
            val adapter: JsonAdapter<List<Breed>> = moshi.adapter(type)
            val result = adapter.fromJson(json)!!
            assertEquals("foo", result.first().name)
        }

        @Test
        fun `object with one breed serializes to breed with same id`() {
            val json = """{"foo":[]}"""
            val type = Types.newParameterizedType(List::class.java, Breed::class.java)
            val adapter: JsonAdapter<List<Breed>> = moshi.adapter(type)
            val result = adapter.fromJson(json)!!
            assertEquals("foo", result.first().id)
        }

        @Test
        fun `object with one breed with no sub-breeds serializes to breed with empty sub-breeds`() {
            val json = """{"foo":[]}"""
            val type = Types.newParameterizedType(List::class.java, Breed::class.java)
            val adapter: JsonAdapter<List<Breed>> = moshi.adapter(type)
            val result = adapter.fromJson(json)!!
            assertTrue(result.first().subs.isEmpty())
        }

        @Test
        fun `object with one breed with sub-breeds serializes to breed with non empty sub-breeds`() {
            val json = """{"foo":["bar"]}"""
            val type = Types.newParameterizedType(List::class.java, Breed::class.java)
            val adapter: JsonAdapter<List<Breed>> = moshi.adapter(type)
            val result = adapter.fromJson(json)!!
            assertTrue(result.first().subs.isNotEmpty())
        }

        @Test
        fun `one breed with sub-breeds serializes to breed with same sub-breed name`() {
            val json = """{"foo":["bar"]}"""
            val type = Types.newParameterizedType(List::class.java, Breed::class.java)
            val adapter: JsonAdapter<List<Breed>> = moshi.adapter(type)
            val result = adapter.fromJson(json)!!
            assertEquals("bar", result.first().subs.first().name)
        }

        @Test
        fun `one breed with sub-breeds serializes to breed with composite sub-breed id`() {
            val json = """{"foo":["bar"]}"""
            val type = Types.newParameterizedType(List::class.java, Breed::class.java)
            val adapter: JsonAdapter<List<Breed>> = moshi.adapter(type)
            val result = adapter.fromJson(json)!!
            assertEquals("foo/bar", result.first().subs.first().id)
        }

        @Test
        fun `breed with sub-breeds serializes to breed with sub-breed with same parent name`() {
            val json = """{"foo":["bar"]}"""
            val type = Types.newParameterizedType(List::class.java, Breed::class.java)
            val adapter: JsonAdapter<List<Breed>> = moshi.adapter(type)
            val result = adapter.fromJson(json)!!
            assertEquals("foo", result.first().subs.first().parentName)
        }

        @Test
        fun `breed with sub-breeds serializes to breed with sub-breed respecting order`() {
            val json = """{"foo":["beta", "alpha", "gamma"]}"""
            val type = Types.newParameterizedType(List::class.java, Breed::class.java)
            val adapter: JsonAdapter<List<Breed>> = moshi.adapter(type)
            val result = adapter.fromJson(json)!!
            assertEquals(listOf("beta", "alpha", "gamma"), result.first().subs.map { it.name })
        }

        @Test
        fun `breeds with or without sub-breeds serialize`() {
            val json = """{"foo":["bar", "baz"], "bar": [], "baz": ["foo"]}"""
            val type = Types.newParameterizedType(List::class.java, Breed::class.java)
            val adapter: JsonAdapter<List<Breed>> = moshi.adapter(type)
            val result = adapter.fromJson(json)!!
            val expect = listOf(
                Breed(
                    id = "foo",
                    name = "foo",
                    subs = listOf(
                        SubBreed(
                            id = "foo/bar",
                            name = "bar",
                            parentName = "foo"
                        ),
                        SubBreed(
                            id = "foo/baz",
                            name = "baz",
                            parentName = "foo"
                        ),
                    )
                ),
                Breed(
                    id = "bar",
                    name = "bar",
                    subs = listOf()
                ),
                Breed(
                    id = "baz",
                    name = "baz",
                    subs = listOf(
                        SubBreed(
                            id = "baz/foo",
                            name = "foo",
                            parentName = "baz"
                        )
                    )
                )
            )

            assertEquals(expect.sorted(), result.sorted())
        }
    }

    @Nested
    @DisplayName("toJson")
    inner class ToJson {

        @Test
        fun `empty list serializes as empty object`() {
            val json = "{}"
            val type = Types.newParameterizedType(List::class.java, Breed::class.java)
            val adapter: JsonAdapter<List<Breed>> = moshi.adapter(type)
            val result = adapter.toJson(emptyList())
            assertNotNull(result)
            assertEquals(json, result)
        }

        @Test
        fun `one breed serializes as object with one key`() {
            val json = """{"foo":[]}"""
            val type = Types.newParameterizedType(List::class.java, Breed::class.java)
            val adapter: JsonAdapter<List<Breed>> = moshi.adapter(type)
            val result = adapter.toJson(listOf(Breed("foo", "foo", emptyList())))
            assertNotNull(result)
            assertEquals(json, result)
        }

        @Test
        fun `one breed serializes as object with one key with same id`() {
            val json = """{"foo":[]}"""
            val type = Types.newParameterizedType(List::class.java, Breed::class.java)
            val adapter: JsonAdapter<List<Breed>> = moshi.adapter(type)
            val result = adapter.toJson(listOf(Breed("foo", "bar", emptyList())))
            assertNotNull(result)
            assertEquals(json, result)
        }

        @Test
        fun `two breeds serializes in a given order`() {
            val json = """{"foo":[],"bar":[]}"""
            val type = Types.newParameterizedType(List::class.java, Breed::class.java)
            val adapter: JsonAdapter<List<Breed>> = moshi.adapter(type)
            val result = adapter.toJson(
                listOf(
                    Breed("foo", "foo", emptyList()),
                    Breed("bar", "bar", emptyList())
                )
            )
            assertNotNull(result)
            assertEquals(json, result)
        }

        @Test
        fun `breed with sub-breed serializes`() {
            val json = """{"foo":["bar"]}"""
            val type = Types.newParameterizedType(List::class.java, Breed::class.java)
            val adapter: JsonAdapter<List<Breed>> = moshi.adapter(type)
            val result = adapter.toJson(
                listOf(
                    Breed(
                        id = "foo", name = "foo", subs = listOf(
                            SubBreed(id = "foo/bar", name = "bar", parentName = "foo")
                        )
                    ),
                )
            )
            assertNotNull(result)
            assertEquals(json, result)
        }

        @Test
        fun `breed with sub-breeds serializes respectingo order`() {
            val json = """{"foo":["zetta","bar"]}"""
            val type = Types.newParameterizedType(List::class.java, Breed::class.java)
            val adapter: JsonAdapter<List<Breed>> = moshi.adapter(type)
            val result = adapter.toJson(
                listOf(
                    Breed(
                        id = "foo", name = "foo", subs = listOf(
                            SubBreed(id = "foo/zetta", name = "zetta", parentName = "foo"),
                            SubBreed(id = "foo/bar", name = "bar", parentName = "foo")
                        )
                    ),
                )
            )
            assertNotNull(result)
            assertEquals(json, result)
        }
    }
}