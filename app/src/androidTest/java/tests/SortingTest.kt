package tests

import org.junit.Test
import screens.MainScreen


class SortingTest : BaseTest() {

    @Test
    fun defaultSortSelectedTest() = run {
        with(MainScreen(this)) {
            clickButtonSorting()
            checkSelectedDefaultButton()
        }
    }

    @Test
    fun sortingWithAgeTest() = run {
        val sortedAges = arrayOf("100", "70", "26")

        with(MainScreen(this)) {
            addPersonByNetwork(3)
            selectAgeSorting()
            checkSortingWithAge(sortedAges)
        }
    }
}