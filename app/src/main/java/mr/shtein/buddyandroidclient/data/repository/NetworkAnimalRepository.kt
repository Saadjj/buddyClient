package mr.shtein.buddyandroidclient.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mr.shtein.buddyandroidclient.exceptions.validate.ServerErrorException
import mr.shtein.buddyandroidclient.model.Animal
import mr.shtein.buddyandroidclient.model.dto.AnimalFilter
import mr.shtein.buddyandroidclient.retrofit.NetworkService
import kotlin.math.min

const val ANIMAL_TYPE_ID_KEY = "type_id"
const val CITY_ID_KEY = "city_id"
const val BREED_ID_KEY = "breed_id"
const val CHARACTERISTIC_ID_KEY = "characteristic_id"
private const val MIN_AGE_KEY = "min_age"
private const val MAX_AGE_KEY = "max_age"
private const val GENDER_KEY = "gender"

class NetworkAnimalRepository(private val networkService: NetworkService) : AnimalRepository {

    override suspend fun getAnimals(filter: AnimalFilter): List<Animal> =
        withContext(Dispatchers.IO) {
            val minAge = getMinAge(filter.minAge)
            val maxAge = getMaxAge(filter.maxAge)
            val result = networkService.getAnimals(
                animalTypeId = filter.animalTypeId,
                cityId = filter.cityId,
                breedId = filter.breedId,
                genderId = filter.genderId,
                characteristicId = filter.characteristicId,
                minAge = minAge,
                maxAge = maxAge
            )
            when (result.code()) {
                200 -> {
                    return@withContext result.body() ?: listOf()
                }
                500 -> {
                    throw ServerErrorException()
                }
                else -> {
                    return@withContext listOf()
                }
            }
        }

    private fun getMinAge(minAge: Int): Int? {
        return if (minAge == -1) {
            null
        } else {
            minAge
        }
    }

    private fun getMaxAge(maxAge: Int): Int? {
        return if (maxAge == -1) {
            null
        } else {
            maxAge
        }
    }
}