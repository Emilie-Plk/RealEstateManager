package com.emplk.realestatemanager.data.property_draft.mappers

import com.emplk.realestatemanager.data.property_draft.getTestPicturePreviewDtos
import com.emplk.realestatemanager.data.property_draft.mappers.FormDraftMapper
import com.emplk.realestatemanager.data.property_draft.mappers.PicturePreviewMapper
import com.emplk.realestatemanager.fixtures.getTestFormDraftDto
import com.emplk.realestatemanager.fixtures.getTestFormDraftEntity
import com.emplk.realestatemanager.fixtures.getTestPicturePreviewEntities
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class FormDraftMapperTest {

    private val picturePreviewMapper: PicturePreviewMapper = mockk()

    private val formDraftMapper = FormDraftMapper(
        picturePreviewMapper
    )

    @Test
    fun `mapToFormDraftDto nominal case`() {
        // When
        val formDraftDto = formDraftMapper.mapToFormDraftDto(getTestFormDraftEntity(1L))

        // Then
        assertEquals(getTestFormDraftDto(1L), formDraftDto)
        confirmVerified(picturePreviewMapper)
    }

    @Test
    fun `mapToFormDraftEntity nominal case`() {
        // Given
        every { picturePreviewMapper.mapToPicturePreviewEntities(getTestPicturePreviewDtos(1L)) } returns getTestPicturePreviewEntities()

        // When
        val formDraftEntity = formDraftMapper.mapToFormDraftEntity(
            getTestFormDraftDto(1L),
           getTestPicturePreviewDtos(1L)
        )

        // Then
        assertEquals(getTestFormDraftEntity(1L), formDraftEntity)
        verify(exactly = 1) { picturePreviewMapper.mapToPicturePreviewEntities(getTestPicturePreviewDtos(1L)) }
        confirmVerified(picturePreviewMapper)
    }
}