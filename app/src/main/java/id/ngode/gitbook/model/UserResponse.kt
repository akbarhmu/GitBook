package id.ngode.gitbook.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "tb_favorites")
@Parcelize
data class UserResponse(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = COLUMN_ID)
    var id: Int? = 0,

    @ColumnInfo(name = COLUMN_LOGIN)
    @field:SerializedName("login")
    var login: String? = null,

    @field:SerializedName("company")
    val company: String? = null,

    @field:SerializedName("public_repos")
    val publicRepos: Int? = null,

    @field:SerializedName("followers")
    val followers: Int? = null,

    @ColumnInfo(name = COLUMN_AVATAR)
    @field:SerializedName("avatar_url")
    var avatarUrl: String? = null,

    @ColumnInfo(name = COLUMN_HTML_URL)
    @field:SerializedName("html_url")
    var htmlUrl: String? = null,

    @field:SerializedName("following")
    val following: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("location")
    val location: String? = null
) : Parcelable {
    companion object {
        const val COLUMN_ID = "id"
        const val COLUMN_LOGIN = "login"
        const val COLUMN_AVATAR = "avatar_url"
        const val COLUMN_HTML_URL = "html_url"
    }
}