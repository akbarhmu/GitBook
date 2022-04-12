package id.ngode.gitbook.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchResponse(
    @field:SerializedName("items")
    val items: List<UserResponse>?
) : Parcelable

