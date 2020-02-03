package com.omermuhammed.omdbsearch

import com.omermuhammed.omdbsearch.network.Resource
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ResourceTest {
    @Test
    fun failure() {
        val exceptionMessage = "foo"
        val errorMessage = Resource.Failure<Any>(exceptionMessage)
        assertThat<String>(errorMessage.errorMessage, `is`("foo"))
    }

    @Test
    fun success() {
        val data = "foo"
        val resource = Resource.Success<Any>(data)
        assertThat<String>(resource.data.toString(), `is`("foo"))
    }
}