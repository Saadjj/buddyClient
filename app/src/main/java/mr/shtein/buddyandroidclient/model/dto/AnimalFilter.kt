package mr.shtein.buddyandroidclient.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AnimalFilter(
    var animalTypeId: MutableList<Int>? = null,
    val cityId: MutableList<Int>? = null,
    val breedId: MutableList<Int>? = null,
    val characteristicId: MutableList<Int>? = null,
    val genderId: MutableList<Int>? = null,
    val minAge: Int = -1,
    val maxAge: Int = -1
) : Parcelable
