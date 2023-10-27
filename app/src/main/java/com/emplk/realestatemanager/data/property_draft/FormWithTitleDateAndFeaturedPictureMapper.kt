package com.emplk.realestatemanager.data.property_draft

import com.emplk.realestatemanager.domain.property_draft.FormWithTitleDateAndFeaturedPictureEntity
import javax.inject.Inject

class FormWithTitleDateAndFeaturedPictureMapper @Inject constructor() {
    fun mapToFormsWithTitleDateAndFeaturedPictureEntities(forms: List<FormWithTitleDateAndFeaturedPicture>): List<FormWithTitleDateAndFeaturedPictureEntity> =
        buildList {
            forms.forEach { form ->
                add(
                    FormWithTitleDateAndFeaturedPictureEntity(
                        id = form.formWithTitleAndLastEditionDate.id,
                        title = form.formWithTitleAndLastEditionDate.title,
                        lastEditionDate = form.formWithTitleAndLastEditionDate.lastEditionDate,
                        featuredPicture = form.featuredPicture?.uri,
                        featuredPictureDescription = form.featuredPicture?.description,
                    )
                )
            }
        }

}