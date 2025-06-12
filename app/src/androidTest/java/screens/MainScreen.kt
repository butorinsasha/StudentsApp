package screens

import android.view.View
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import data.PersonData
import io.github.kakaocup.kakao.check.KCheckBox
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher
import org.junit.Assert.assertEquals
import ru.tinkoff.favouritepersons.R

class MainScreen(testContext: TestContext<*>) : BaseScreen(testContext) {

    private val textNotFound = KTextView { withId(R.id.tw_no_persons) }
    private val buttonAddPersonNetwork = KButton { withId(R.id.fab_add_person_by_network) }
    private val buttonAddPersonManually = KButton { withId(R.id.fab_add_person_manually) }
    private val actionButton = KButton { withId(R.id.fab_add_person) }
    private val sortingButton = KButton { withId(R.id.action_item_sort) }
    private val radioButtonDefault = KCheckBox { withId(R.id.bsd_rb_default) }
    private val radioButtonAge = KCheckBox { withId(R.id.bsd_rb_age) }
    private val snackbar = KTextView { withText("Internet error! Check your connection") }


    private val listPerson = KRecyclerView(
        builder = { withId(R.id.rv_person_list) },
        itemTypeBuilder = {
            itemType {
                ItemRecyclerView(it)
            }
        })

    class ItemRecyclerView(matcher: Matcher<View>) : KRecyclerItem<ItemRecyclerView>(matcher) {
        val personCard = KView(matcher) { withId(R.id.person_info_constraint) }
        val personName = KTextView(matcher) { withId(R.id.person_name) }
        val personInfo = KTextView(matcher) { withId(R.id.person_private_info) }
        val personAvatar = KImageView(matcher) { withId(R.id.person_avatar) }
        val personEmail = KTextView(matcher) { withId(R.id.person_email) }
        val personPhone = KTextView(matcher) { withId(R.id.person_phone) }
        val personAddress = KTextView(matcher) { withId(R.id.person_address) }
        val personRating = KTextView(matcher) { withId(R.id.person_rating) }
    }

    fun clickActionButton() {
        step("Нажимаем кнопку добавить") {
            actionButton.click()
        }
    }

    fun clickAddWithNetwork(count: Int = 1) {
        step("Нажимаемт на кнопку добавление карточки вручную $count раз") {
            for (i in 1..count) {
                buttonAddPersonNetwork.click()
                Thread.sleep(1000)
            }
        }
    }

    fun addPersonByNetwork(count: Int = 1) {
        step("Добавление карточки через сеть") {
            clickActionButton()
            clickAddWithNetwork(count)
        }
    }

    fun clickAddWithManually() {
        step("Нажимаем на кнопку добавление карточки вручную") {
            buttonAddPersonManually.click()
        }
    }

    fun addPersonByManually() {
        step("Добавление карточки вручную") {
            clickActionButton()
            clickAddWithManually()
        }
    }

    fun checkTextNotFoundNotDisplayed() {
        step("Проверяем, что сообщение об отсутствии студентов в списке более не отображается") {
            textNotFound.isNotDisplayed()
        }
    }

    fun deletePerson(indexCard: Int) {
        listPerson.childAt<ItemRecyclerView>(indexCard) {
            personCard.perform { swipeLeft() }
        }
    }

    fun checkSizeListPerson(total: Int) {
        assertEquals(
            "Проверка уменьшения количества карточек на 1", total - 1,
            listPerson.getSize()
        )
    }

    fun clickButtonSorting() {
        step("Открытие окна сортировки") {
            sortingButton.click()
        }
    }

    fun checkSelectedDefaultButton() {
        step("Проверка выбранной сортировки по умолчанию") {
            radioButtonDefault.isChecked()
        }
    }

    fun selectAgeSorting() {
        step("Выбор сортировки по возрасту") {
            clickButtonSorting()
            radioButtonAge.click()
        }
    }

    fun checkSortingWithAge(listAge: Array<String>) {
        step("Проверка сортировки по возрасту от большего к меньшему") {
            listPerson {
                childAt<ItemRecyclerView>(0) {
                    personInfo.containsText(listAge[0])
                }
                childAt<ItemRecyclerView>(1) {
                    personInfo.containsText(listAge[1])
                }
                childAt<ItemRecyclerView>(2) {
                    personInfo.containsText(listAge[2])
                }
            }
        }
    }

    fun openPersonScreen(index: Int) {
        step("Открытие карточки человека") {
            listPerson.childAt<ItemRecyclerView>(index) {
                click()
            }
        }
    }

    fun checkNameInCard(expectedText: String) {
        listPerson.firstChild<ItemRecyclerView> {
            personName.containsText(expectedText)
        }
    }

    fun checkFieldsPersonInCard() {
        val person = PersonData()
        listPerson.firstChild<ItemRecyclerView> {
            personName.containsText(person.name)
            personName.containsText(person.surname)
            personInfo.containsText("Male")
            personInfo.containsText("24")
            personAvatar.isDisplayed()
            personEmail.hasText(person.email)
            personPhone.hasText(person.phone)
            personAddress.hasText(person.address)
            personRating.hasText(person.score)
        }
    }

    fun checkSnackbarIsDisplayed() {
        step("Проверка отображение снекбара") {
            snackbar.inRoot { isDisplayed() }
        }
    }
}