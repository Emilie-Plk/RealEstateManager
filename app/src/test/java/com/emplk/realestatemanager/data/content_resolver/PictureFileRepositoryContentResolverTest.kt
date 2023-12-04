package com.emplk.realestatemanager.data.content_resolver

import android.content.ContentResolver
import android.content.Context
import com.emplk.realestatemanager.fixtures.testFixedClock
import com.emplk.utils.TestCoroutineRule
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule

class PictureFileRepositoryContentResolverTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val contentResolver: ContentResolver = mockk()
    private val context: Context = mockk()

    private val pictureFileRepositoryContentResolver = PictureFileRepositoryContentResolver(
        contentResolver,
        context,
        testCoroutineRule.getTestCoroutineDispatcherProvider(),
        testFixedClock
    )

    @Before
    fun setUp() {
        every { contentResolver.openInputStream(any()) } returns mockk()
        every { contentResolver.openInputStream(any())?.copyTo(any()) } returns mockk()
        every { contentResolver.openInputStream(any())?.close() } returns Unit
        every { context.cacheDir } returns mockk()
        every { context.cacheDir.absolutePath } returns "cacheDir"
    }

    /* @Test
     fun `initial case`() = testCoroutineRule.runTest {

         // Given
         val stringUri = "stringUri"
         val filePrefix = "filePrefix"
         val absolutePath = "cacheDir/filePrefix0.jpg"

         // When
         val result = pictureFileRepositoryContentResolver.saveToAppFiles(stringUri, filePrefix)

         // Then
         assertEquals(absolutePath, result)
     }*/
}