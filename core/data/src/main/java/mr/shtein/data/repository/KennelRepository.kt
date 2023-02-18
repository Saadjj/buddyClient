package mr.shtein.data.repository

import mr.shtein.data.model.AvatarWrapper
import mr.shtein.data.model.KennelRequest
import mr.shtein.model.KennelPreviewResponse
import mr.shtein.model.volunteer.VolunteersBid
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface KennelRepository {

    suspend fun addNewKennel(
        token: String,
        kennelRequest: KennelRequest,
        avatarWrapper: AvatarWrapper?
    )

    suspend fun getKennelsByPersonId(
       token: String,
       personId: Long
    ): List<KennelPreviewResponse>

    suspend fun getKennelAvatar(avatarUri: String): AvatarWrapper?

    suspend fun getVolunteerBids(
        token: String,
        kennelId: Int
    ) : List<VolunteersBid>

}