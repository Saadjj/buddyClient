package mr.shtein.buddyandroidclient.utils

import mr.shtein.buddyandroidclient.exceptions.validate.EmptyCityFieldException
import mr.shtein.buddyandroidclient.exceptions.validate.ValidationException

class PhoneNumberValidator : Validator {

    companion object {
        var numberCount = 0
        private const val SHORT_PHONE_NUMBER_EXCEPTION = "Вы ввели слишком короткий номер"
    }

    override fun validateValue(valueForValidate: String): Boolean {
        return isValidPhoneNum(valueForValidate)
    }

    private fun isValidPhoneNum(phoneNum: String): Boolean {
        if (phoneNum.length < 13) throw ValidationException(SHORT_PHONE_NUMBER_EXCEPTION)
        return true
    }




}