package connect.me.activities;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import connect.me.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RegisterTest {

    @Rule
    public ActivityTestRule<CoverActivity> mActivityTestRule = new ActivityTestRule<>(CoverActivity.class);

    @Test
    public void registerTest() {

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.btRegister), withText("Register"), isDisplayed()));
        appCompatButton.perform(click());


        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.etAge), isDisplayed()));
        appCompatEditText.perform(replaceText("mirelka@mail.bg"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.etPhoneNumber), isDisplayed()));
        appCompatEditText2.perform(replaceText("123456"), closeSoftKeyboard());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.bSubmit), withText("Register"), isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.etName), isDisplayed()));
        appCompatEditText3.perform(replaceText("Mirelka"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.etAge), isDisplayed()));
        appCompatEditText4.perform(replaceText("20"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.etPhoneNumber), isDisplayed()));
        appCompatEditText5.perform(replaceText("123456"), closeSoftKeyboard());

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.spinnerGender), isDisplayed()));
        appCompatSpinner.perform(click());

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(android.R.id.text1), withText("Female"), isDisplayed()));
        appCompatCheckedTextView.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.bSubmit), withText("Let's go"), isDisplayed()));
        appCompatButton3.perform(click());

    }

}
