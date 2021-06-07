package com.example.termtracker;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.termtracker.Dialogs.ConfirmationDialogFragment;
import com.example.termtracker.Fragments.AssessmentsFragment;
import com.example.termtracker.Fragments.CoursesFragment;
import com.example.termtracker.Fragments.TermsFragment;
import com.example.termtracker.Misc.Receiver;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Brandon Maxfield
 * <p>
 * FUTURE ENHANCEMENTS
 * 1. When adding a note or assessment, allow user to filter classes by term.
 * 2. Add the ability for the user to change the color scheme.  Some may consider the dark theme
 *      to be dull, I find it to by easy on the eyes and professional.
 */


public class MainActivity extends AppCompatActivity implements ConfirmationDialogFragment.ConfirmationDialogFragmentListener {


    private static final String CHANNEL_ID = "Date Reminders";


    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;



    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        setCurrentFragment(new TermsFragment());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createNotificationChannel();
        setupNotifications();
    }

    //changes the fragment that is on display
    public void setCurrentFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_display, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //overloaded default method, used to set the appbar menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        return true;
    }

    // Overridden default method used to handle click events on appbar menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        // Display menu item's title by using a Toast.
        if (id == R.id.appbar_add) {
            launchAddActivity();
            return true;
        }
        if (id == R.id.appbar_notifications) {
            showNotificationDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void launchAddActivity() {
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            item -> {
                Fragment fragment;
                getSupportActionBar();
                switch (item.getItemId()) {

                    case R.id.nav_terms:
                        fragment = new TermsFragment();
                        setCurrentFragment(fragment);
                        return true;
                    case R.id.nav_courses:
                        fragment = new CoursesFragment();
                        setCurrentFragment(fragment);
                        return true;
                    case R.id.nav_assessments:
                        fragment = new AssessmentsFragment();
                        setCurrentFragment(fragment);
                        return true;

                }
                return false;
            };

    public void setupNotifications(){

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTime().compareTo(new Date()) < 0) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        Intent intent = new Intent(getApplicationContext(), Receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        } else {
            Toast.makeText(this, "Something went wrong while setting up notifications.", Toast.LENGTH_SHORT).show();
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Date Reminders", importance);
            channel.setDescription("This channel is for displaying alerts to the user indicating whether or not there are" +
                    " dates of importance today.  Alerts happen at approximately 10:00 am system time.");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotificationDialog() {
        FragmentManager fm = this.getSupportFragmentManager();
        ConfirmationDialogFragment confirmationDialogFragment = ConfirmationDialogFragment.newInstance(
                "By default, this app sends notifications every day at 10:00 am that will inform you of whether or not there is a course or assessment start or end date.",
                "Change Settings",
                "Cancel");
        confirmationDialogFragment.show(fm, "notification_confirmation_dialog");
    }

    @Override
    public void onConfirmDialogResolved(boolean result) {
        if (result) {
            Intent intent = new Intent();
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", getPackageName());
            intent.putExtra("app_uid", getApplicationInfo().uid);
            intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());

            startActivity(intent);
        }
    }
}
