package com.cleanup.todoc.ui;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.cleanup.todoc.utils.TestUtils.withRecyclerView;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.isA;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.cleanup.todoc.R;
import com.cleanup.todoc.data.DataBase;
import com.cleanup.todoc.ui.create_task.ProjectViewState;
import com.cleanup.todoc.ui.main.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {



    @Before
    public void setUp() {
        ActivityScenario.launch(MainActivity.class);
    }

    @Test
    public void checkThatIsNoTaskAtTheBeginning() {
        onView(allOf(withId(R.id.lbl_no_task), isCompletelyDisplayed())).check(matches(withText(R.string.no_task)));
//        onView(allOf(withId(R.id.lbl_no_task),isCompletelyDisplayed())).check(matches(withId(R.drawable.ic_work_off)));
    }

    @Test
    public void addAndRemoveTask() {

        onView(allOf(withId(R.id.lbl_no_task), isCompletelyDisplayed())).check(matches(withText(R.string.no_task)));

        onView(withId(R.id.fab_add_task))
                .perform(click());

        onView(withId(R.id.create_task_et_name))
                .perform(replaceText("aaa Tâche example"));

        onView(withId(R.id.create_task_spi_project))
                .perform(click());

        onData(isA(ProjectViewState.class))
                .inRoot(isPlatformPopup())
                .atPosition(2)
                .check(matches((withText("Projet Circus"))))
                .perform(
                        scrollTo(),
                        click()
                );
        onView(withId(R.id.create_task_btn))
                .perform(click());

        onView(withRecyclerView(R.id.list_tasks)
                .atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));

//        onView(withId(R.id.fab_add_task)).perform(click());
//        onView(withId(R.id.create_task_et_name)).perform(replaceText("hhh Tâche example"));
//        onView(withId(R.id.create_task_btn)).perform(click());

//        onView(withId(R.id.img_delete))
//                .perform(click());

        onView(withId(R.id.img_delete))
                .perform(click());

        onView(allOf(withId(R.id.lbl_no_task), isCompletelyDisplayed()))
                .check(matches(withText(R.string.no_task)));
    }

    @Test
    public void sortTasks() {

        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.create_task_et_name)).perform(replaceText("aaa Tâche example"));
        onView(withId(R.id.create_task_btn)).perform(click());
//        Thread.sleep(5000);
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.create_task_et_name)).perform(replaceText("zzz Tâche example"));
        onView(withId(R.id.create_task_btn)).perform(click());
//        Thread.sleep(5000);
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.create_task_et_name)).perform(replaceText("hhh Tâche example"));
        onView(withId(R.id.create_task_btn)).perform(click());
//        Thread.sleep(5000);

        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));

        // Sort alphabetical
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_alphabetical)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));

        // Sort alphabetical inverted
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_alphabetical_invert)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));

        // Sort old first
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_oldest_first)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));

        // Sort recent first
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_recent_first)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
    }
}
