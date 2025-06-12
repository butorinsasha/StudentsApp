package tests

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.http.Fault
import com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED
import org.junit.Test
import screens.MainScreen

class PersonCardNetworkTest : BaseTest() {


    @Test
    fun addPersonByNetworkTest() = run {

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
            checkTextNotFoundNotDisplayed()
        }
    }

    @Test
    fun deletePersonTest() = run {

        val scenario = "Delete Person Scenario"

        stubFor(
            get("/api/").inScenario(scenario).whenScenarioStateIs(STARTED).willSetStateTo("person2")
                .willReturn(
                    aResponse().withStatus(200)
                        .withBody(getFileToString("responses/person1_response.json"))
                )
        )
        stubFor(
            get("/api/").inScenario(scenario).whenScenarioStateIs("person2")
                .willSetStateTo("person3").willReturn(
                    aResponse().withStatus(200)
                        .withBody(getFileToString("responses/person2_response.json"))
                )
        )
        stubFor(
            get("/api/").inScenario(scenario).whenScenarioStateIs("person3").willReturn(
                aResponse().withStatus(200)
                    .withBody(getFileToString("responses/person3_response.json"))
            )
        )

        with(MainScreen(this)) {
            val countCards = 3
            addPersonByNetwork(countCards)
            deletePerson(2)
            checkSizeListPerson(countCards)
        }
    }

    @Test
    fun checkSnackbarInternetConnectionTest() = run {

        stubFor(
            get("/api/")
                .willReturn(
                    aResponse()
                        .withFault(Fault.CONNECTION_RESET_BY_PEER)
                )
        )

        with(MainScreen(this)) {
            addPersonByNetwork()
            checkSnackbarIsDisplayed()
        }
    }
}