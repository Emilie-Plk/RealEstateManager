package com.emplk.realestatemanager.data.autocomplete.response

import com.google.gson.annotations.SerializedName

data class StructuredFormattingResponse(
    @SerializedName("main_text") val mainText: String?,
    @SerializedName("main_text_matched_substrings") val mainTextMatchedSubstrings: List<MatchedSubstringResponse>?,
    @SerializedName("secondary_text") val secondaryText: String?
)