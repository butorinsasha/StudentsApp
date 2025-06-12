package screens

//import matchers.GenderErrorMatcher
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import data.PersonData
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import matcher.GenderErrorMatcher
import ru.tinkoff.favouritepersons.R

class PersonScreen(testContext: TestContext<*>) : BaseScreen(testContext) {
    val nameEditText = KEditText { withId(R.id.et_name) }
    val surnameEditText = KEditText { withId(R.id.et_surname) }
    val genderEditText = KEditText { withId(R.id.et_gender) }
    val birhdayEditText = KEditText { withId(R.id.et_birthdate) }
    val emailEditText = KEditText { withId(R.id.et_email) }
    val phoneEditText = KEditText { withId(R.id.et_phone) }
    val addressEditText = KEditText { withId(R.id.et_address) }
    val photoUrlEditText = KEditText { withId(R.id.et_image) }
    val scoreEditText = KEditText { withId(R.id.et_score) }
    val submitButton = KButton { withId(R.id.submit_button) }
    val genderTextInputLayout = onView(withId(R.id.til_gender))
    val genderErrorText = KTextView { withText("Поле должно быть заполнено буквами М или Ж") }


    fun checkFieldValue(field: String, expectedValue: String) {
        step("Проверка значения поля $field") {
            when (field) {
                "Имя" -> nameEditText.hasText(expectedValue)
                "Фамилия" -> surnameEditText.hasText(expectedValue)
                "Пол" -> genderEditText.hasText(expectedValue)
                "Дата рождения" -> birhdayEditText.hasText(expectedValue)
                else -> throw Exception("Некорректное поле")
            }
        }
    }

    fun editName(text: String) {
        step("Редактирование имени") {
            nameEditText.replaceText(text)
        }
    }

    fun clickSubmit() {
        step("Нажатие кнопки Сохранить") {
            submitButton.click()
        }

    }

    fun createPerson() {
        step("Создание карточки студента вручную") {
            val person = PersonData()
            nameEditText.replaceText(person.name)
            surnameEditText.replaceText(person.surname)
            genderEditText.replaceText(person.gender)
            birhdayEditText.replaceText(person.birthday)
            emailEditText.replaceText(person.email)
            phoneEditText.replaceText(person.phone)
            addressEditText.replaceText(person.address)
            photoUrlEditText.replaceText(person.photo)
            scoreEditText.replaceText(person.score)
            clickSubmit()
        }
    }

    fun editGender(text: String) {
        step("Вводим текст в поле Пол") {
            genderEditText.replaceText(text)
        }
    }

    fun clearGender() {
        step("Очистка поля Пол") {
            genderEditText.clearText()
            Thread.sleep(2000L)
        }
    }

    fun checkGenderErrorTextIsDisplayed() {
        step("Проверка отображения ошибки в поле Пол") {
            genderErrorText.isDisplayed()
        }
    }

    fun checkGenderErrorTextIsNotDisplayed() {
        step("Проверка отсутствия сообщения об ошибке в поле Пол") {
            genderErrorText.doesNotExist()
        }

    }

    fun checkErrorGender() {
        step("Проверка собщения об ошибке в поле Пол") {
            genderTextInputLayout.check(
                matches(
                    GenderErrorMatcher("Поле должно быть заполнено буквами М или Ж")
                )
            )
        }
    }
}
