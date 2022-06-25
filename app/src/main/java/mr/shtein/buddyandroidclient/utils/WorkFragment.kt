package mr.shtein.buddyandroidclient.utils

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class WorkFragment : Parcelable {
    ADD_KENNEL,
    ANIMAL_LIST,
    USER_PROFILE,
    ANIMAL_CARD,
    REGISTRATION,
    LOGIN,
    OTHER
}