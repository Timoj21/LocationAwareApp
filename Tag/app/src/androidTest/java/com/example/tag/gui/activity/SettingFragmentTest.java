package com.example.tag.gui.activity;

import androidx.test.rule.ActivityTestRule;

import com.example.tag.R;
import com.example.tag.gui.fragment.JoinFragment;
import com.example.tag.gui.fragment.SettingsFragment;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class SettingFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void splashActivityTestBackButton() {
        mActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentContainer, new SettingsFragment()).commit();
        onView(withId(R.id.settingsBackButton)).perform(click());

    }



    @Test
    public void splashActivityTestTextViewsDisplayed() {
        mActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentContainer, new SettingsFragment()).commit();
        onView(withId(R.id.textView2)).check(matches(isDisplayed()));
        onView(withId(R.id.textView)).check(matches(isDisplayed()));
        onView(withId(R.id.textView3)).check(matches(isDisplayed()));

    }


}
