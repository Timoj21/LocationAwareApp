package com.example.tag.gui.activity;


import androidx.test.rule.ActivityTestRule;

import com.example.tag.R;
import com.example.tag.gui.fragment.HostJoinFragment;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class HostJoinFragmentTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void splashActivityTestHostButton() {
        mActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.loginFragmentContainer, new HostJoinFragment()).commit();
        onView(withId(R.id.hostGameButton)).perform(click());

    }

    @Test
    public void splashActivityTestJoinButton() {
        mActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.loginFragmentContainer, new HostJoinFragment()).commit();
        onView(withId(R.id.joinGameButton)).perform(click());

    }


}
