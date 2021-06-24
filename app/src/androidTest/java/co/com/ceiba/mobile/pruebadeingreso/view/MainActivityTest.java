package co.com.ceiba.mobile.pruebadeingreso.view;


import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import co.com.ceiba.mobile.pruebadeingreso.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

	@Rule
	public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

	@Test
	public void mainActivityTest() {
		ViewInteraction button = onView(
				allOf(withId(R.id.btn_view_post), withText("Ver publicaciones"),
						childAtPosition(
								allOf(withId(R.id.contentBtnViewPost),
										childAtPosition(
												withId(R.id.contentCard),
												3)),
								0),
						isDisplayed()));
		button.perform(click());

		ViewInteraction button2 = onView(
				allOf(withId(R.id.btn_view_post), withText("Ver publicaciones"),
						childAtPosition(
								allOf(withId(R.id.contentBtnViewPost),
										childAtPosition(
												withId(R.id.contentCard),
												3)),
								0),
						isDisplayed()));
		button2.perform(click());

		pressBack();

		ViewInteraction frameLayout = onView(
				allOf(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class), isDisplayed()));
		frameLayout.check(matches(isDisplayed()));

		pressBack();

		ViewInteraction editText = onView(
				allOf(withId(R.id.editTextSearch),
						childAtPosition(
								childAtPosition(
										withId(R.id.textInputLayoutSearch),
										0),
								0),
						isDisplayed()));
		editText.perform(click());

		ViewInteraction editText2 = onView(
				allOf(withId(R.id.editTextSearch),
						childAtPosition(
								childAtPosition(
										withId(R.id.textInputLayoutSearch),
										0),
								0),
						isDisplayed()));
		editText2.perform(click());

		ViewInteraction editText3 = onView(
				allOf(withId(R.id.editTextSearch),
						childAtPosition(
								childAtPosition(
										withId(R.id.textInputLayoutSearch),
										0),
								0),
						isDisplayed()));
		editText3.perform(click());

		ViewInteraction editText4 = onView(
				allOf(withId(R.id.editTextSearch),
						childAtPosition(
								childAtPosition(
										withId(R.id.textInputLayoutSearch),
										0),
								0),
						isDisplayed()));
		editText4.perform(click());

		ViewInteraction editText5 = onView(
				allOf(withId(R.id.editTextSearch),
						childAtPosition(
								childAtPosition(
										withId(R.id.textInputLayoutSearch),
										0),
								0),
						isDisplayed()));
		editText5.perform(replaceText("ervin"), closeSoftKeyboard());
	}

	private static Matcher<View> childAtPosition(
			final Matcher<View> parentMatcher, final int position) {

		return new TypeSafeMatcher<View>() {
			@Override
			public void describeTo(Description description) {
				description.appendText("Child at position " + position + " in parent ");
				parentMatcher.describeTo(description);
			}

			@Override
			public boolean matchesSafely(View view) {
				ViewParent parent = view.getParent();
				return parent instanceof ViewGroup && parentMatcher.matches(parent)
						&& view.equals(((ViewGroup) parent).getChildAt(position));
			}
		};
	}
}
