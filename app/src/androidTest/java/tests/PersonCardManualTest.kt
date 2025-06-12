package tests

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import io.qameta.allure.kotlin.Allure.step
import org.junit.Test
import screens.MainScreen
import screens.PersonScreen


class PersonCardManualTest : BaseTest() {

    @Test
    fun addPersonManuallyTest() = run {
        MainScreen(this).addPersonByManually()
        PersonScreen(this).createPerson()
        MainScreen(this).checkFieldsPersonInCard()
    }

    @Test
    fun changeNamePersonTest() = run {

        stubFor(
            get("/api/")
                .willReturn(
                    aResponse().withStatus(200)
                        .withBody(getFileToString("responses/person1_response.json"))
                )
        )

        val name = "Иосиф"

        with(MainScreen(this)) {
            addPersonByNetwork()
            openPersonScreen(0)
        }

        with(PersonScreen(this)) {
                editName(name)
                clickSubmit()
        }

        MainScreen(this).checkNameInCard(name)
    }

    @Test
    fun checkErrorTextGenderTest() = run {
        MainScreen(this).addPersonByManually()
        with (PersonScreen(this)) {
            clickSubmit()
            checkErrorGender()
        }
    }

    @Test
    fun checkErrorGenderHideTest() = run {
        with(MainScreen(this)) {
            addPersonByManually()
        }

        with(PersonScreen(this)) {
            editGender("Ю")
            clickSubmit()
            checkGenderErrorTextIsDisplayed()
            clearGender()
            checkGenderErrorTextIsNotDisplayed()
        }
    }
}