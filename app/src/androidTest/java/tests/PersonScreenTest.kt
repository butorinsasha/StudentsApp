package tests

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import screens.MainScreen
import screens.PersonScreen

@RunWith(Parameterized::class)
class PersonScreenTest(
    private val fieldName: String,
    private val expectedFieldValue: String
) : BaseTest() {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): List<Array<Any>> {
            return listOf(
                arrayOf("Имя", "Ariana"),
                arrayOf("Фамилия", "Wilson"),
                arrayOf("Пол", "Ж"),
                arrayOf("Дата рождения", "1954-08-07")
            )
        }
    }

    @Test
    fun personFieldsCheckTest() = run {

        stubFor(
            get("/api/")
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withBody(getFileToString("responses/person1_response.json"))
                )
        )

        with(MainScreen(this)) {
            addPersonByNetwork()
            openPersonScreen(0)
        }
        PersonScreen(this).checkFieldValue(fieldName, expectedFieldValue)
    }
}