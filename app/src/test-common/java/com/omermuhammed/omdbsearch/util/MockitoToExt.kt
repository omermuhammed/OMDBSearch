package com.omermuhammed.omdbsearch.util

import com.omermuhammed.omdbsearch.data.repository.OMDBSearchRepository
import org.mockito.ArgumentCaptor
import org.mockito.Mockito

/**
 * a kotlin friendly mock that handles generics
 */
inline fun <reified T> mock(java: Class<OMDBSearchRepository>): T = Mockito.mock(T::class.java)

inline fun <reified T> argumentCaptor(): ArgumentCaptor<T> = ArgumentCaptor.forClass(T::class.java)