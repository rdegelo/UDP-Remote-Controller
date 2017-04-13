package com.technocreatives.rdegelo.creativeremotecontroller;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.technocreatives.rdegelo.creativeremotecontroller.model.Sequence;
import com.technocreatives.rdegelo.creativeremotecontroller.model.Settings;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by r.degelo on 13/04/2017.
 */
@RunWith(AndroidJUnit4.class)
public class ManageActivityTest {

    Settings settings;

    @Before
    public void prepareSettingsDisplay() {
        settings = new Settings();
        settings.Load(InstrumentationRegistry.getTargetContext());

        settings.setUse_broadcast(false);
        settings.setIp("0.0.0.0");
        settings.setPort(9999);

        settings.Save(InstrumentationRegistry.getTargetContext());

        Intent startIntent = new Intent();
        myRule.launchActivity(startIntent);
    }

    @Rule
    public ActivityTestRule<ManageActivity> myRule =
            new ActivityTestRule<ManageActivity>(ManageActivity.class, true, false);

    @Test
    public void testSettingsDisplay() {
        // Type text and then press the button.
        Espresso.onView(ViewMatchers.withId(R.id.manage_enable_broadcast))
                .check(ViewAssertions.matches(ViewMatchers.isNotChecked()));

        Espresso.onView(ViewMatchers.withId(R.id.manage_ip_edit))
                .check(ViewAssertions.matches(ViewMatchers.withText(settings.getIp())));

        Espresso.onView(ViewMatchers.withId(R.id.manage_port_edit))
                .check(ViewAssertions.matches(ViewMatchers.withText(settings.getPort() + "")));
    }

    @Test
    public void testSettingsSave() throws Exception {
        Espresso.onView(ViewMatchers.withId(R.id.manage_ip_edit)).perform(ViewActions.clearText());
        Espresso.onView(ViewMatchers.withId(R.id.manage_ip_edit)).perform(ViewActions.typeText("1.1.1.1"));

        Espresso.onView(ViewMatchers.withId(R.id.manage_port_edit)).perform(ViewActions.clearText());
        Espresso.onView(ViewMatchers.withId(R.id.manage_port_edit)).perform(ViewActions.typeText("1020"));

        Espresso.onView(ViewMatchers.withId(R.id.manage_enable_broadcast)).perform(ViewActions.click());

        final Instrumentation ins = InstrumentationRegistry.getInstrumentation();
        ins.callActivityOnPause(myRule.getActivity());

        myRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ins.callActivityOnResume(myRule.getActivity());
            }
        });

        Thread.sleep(1000);

        settings = new Settings();
        settings.Load(InstrumentationRegistry.getTargetContext());

        Assert.assertEquals(settings.isUse_broadcast(), true);
        Assert.assertEquals(settings.getIp(), "1.1.1.1");
        Assert.assertEquals(settings.getPort(), 1020);
    }
}
