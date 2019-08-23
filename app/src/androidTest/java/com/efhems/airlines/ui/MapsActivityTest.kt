package com.efhems.airlines.ui


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.efhems.airlines.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MapsActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MapsActivity::class.java)

    @Test
    fun mapsActivityTest() {
        val appCompatAutoCompleteTextView = onView(
            allOf(
                withId(R.id.origin),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.cont),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatAutoCompleteTextView.perform(click())

        val appCompatAutoCompleteTextView2 = onView(
            allOf(
                withId(R.id.origin),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.cont),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatAutoCompleteTextView2.perform(replaceText("a"), closeSoftKeyboard())

        val appCompatAutoCompleteTextView3 = onView(
            allOf(
                withId(R.id.origin), withText("a"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.cont),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatAutoCompleteTextView3.perform(click())

        val appCompatAutoCompleteTextView4 = onView(
            allOf(
                withId(R.id.origin), withText("a"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.cont),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatAutoCompleteTextView4.perform(click())

        val appCompatAutoCompleteTextView5 = onView(
            allOf(
                withId(R.id.origin), withText("a"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.cont),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatAutoCompleteTextView5.perform(replaceText("aa"))

        val appCompatAutoCompleteTextView6 = onView(
            allOf(
                withId(R.id.origin), withText("aa"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.cont),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatAutoCompleteTextView6.perform(closeSoftKeyboard())

        val linearLayoutCompat = onData(anything())
            .inAdapterView(
                childAtPosition(
                    withClassName(`is`("android.widget.PopupWindow")),
                    0
                )
            )
            .atPosition(0)
        linearLayoutCompat.perform(click())

        val appCompatAutoCompleteTextView7 = onView(
            allOf(
                withId(R.id.dest),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.cont),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatAutoCompleteTextView7.perform(replaceText("aa"), closeSoftKeyboard())

        val linearLayoutCompat2 = onData(anything())
            .inAdapterView(
                childAtPosition(
                    withClassName(`is`("android.widget.PopupWindow ")),
                    0
                )
            )
            .atPosition(1)
        linearLayoutCompat2.perform(click())

        val appCompatImageButton = onView(
            allOf(
                withId(R.id.pick_date), withContentDescription("TODO"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.cont),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        val appCompatImageButton2 = onView(
            allOf(
                withClassName(`is`("androidx.appcompat.widget.AppCompatImageButton")),
                withContentDescription("Next month"),
                childAtPosition(
                    allOf(
                        withClassName(`is`("android.widget.DayPickerView")),
                        childAtPosition(
                            withClassName(`is`("com.android.internal.widget.DialogViewAnimator")),
                            0
                        )
                    ),
                    2
                )
            )
        )
        appCompatImageButton2.perform(scrollTo(), click())

        val appCompatButton = onView(
            allOf(
                withId(android.R.id.button1), withText("OK"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        )
        appCompatButton.perform(scrollTo(), click())

        pressBack()

        val cardView = onView(
            allOf(
                withId(R.id.search_btn),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.cont),
                        0
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        cardView.perform(click())

        val constraintLayout = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.rc_tags_items),
                        childAtPosition(
                            withId(R.id.bottom_sheet_header),
                            3
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        constraintLayout.perform(click())

        val appCompatImageView = onView(
            allOf(
                withId(R.id.dismiss_dialog), withContentDescription("close bottom sheet"),
                childAtPosition(
                    allOf(
                        withId(R.id.bottom_sheet_head),
                        childAtPosition(
                            withId(R.id.bottom_sheet_header),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageView.perform(click())

        val imageView = onView(
            allOf(
                withContentDescription("Zoom in"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.RelativeLayout")),
                        2
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        imageView.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
