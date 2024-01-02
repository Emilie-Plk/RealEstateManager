package com.emplk.realestatemanager.data.property_draft.mappers

import com.emplk.realestatemanager.data.property_draft.FormWithTitleDateAndFeaturedPicture
import com.emplk.realestatemanager.domain.property_draft.model.FormWithDetailEntity
import javax.inject.Inject

class FormWithTitleDateAndFeaturedPictureMapper @Inject constructor() {
    fun mapToFormsWithTitleDateAndFeaturedPictureEntities(forms: List<FormWithTitleDateAndFeaturedPicture>): List<FormWithDetailEntity> =
        buildList {
            forms.forEach { form ->
                add(
                    FormWithDetailEntity(
                        id = form.formWithTitleAndLastEditionDateEntity.id,
                        title = form.formWithTitleAndLastEditionDateEntity.title,
                        lastEditionDate = form.formWithTitleAndLastEditionDateEntity.lastEditionDate,
                        featuredPicture = form.featuredPicture?.uri,
                        featuredPictureDescription = form.featuredPicture?.description,
                    )
                )
            }
        }

}