package com.hfad.findandplayA;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Map;

/** Parent class for activities harboring full activity navigation.
 *
 * @version 1.0
 * @author TLabelle366
 */
public class FindAndPlay_ParentClass extends AppCompatActivity {
    protected Bundle loadedTransfers;
    /** Accepts a destination and changes to the destination activity
     *
     * @param destination: The class of an activity meant to be loaded.
     */
    protected void UpdateView(Class destination) {
        //Start the Activity
        this.startActivity(new Intent(this, destination));
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(findViewById(R.id.openBurgerMenuButton) != null) {
            init_BurgerMenuButton();
        }
    }

    /** As UpdateView(..), but accepts a single flag for the intent.
     *
     * @param destination: The class of an activity meant to be loaded.
     * @param flag: An Int value represent an Intent Flag accessed through Intent.<FLAG NAME>
     */
    protected void UpdateView(Class destination, int flag) {
        //Start the intent
        Intent primeIntent = new Intent(this, destination);

        //Sets the flag onto the intent
        primeIntent.setFlags(primeIntent.getFlags() | flag);

        //Start the Activity
        this.startActivity(primeIntent);
    }

    /** As UpdateView(..), but accepts an array of flags for the intent.
     * @See UpdateView()
     * @param destination: The class of an activity meant to be loaded.
     * @param flags: An array of Ints representing Intent flags accessed through Intent.<FLAG NAME>
     */
    protected void UpdateView(Class destination, int[] flags) {
        //Begin the intent
        Intent primeIntent = new Intent(this, destination);

        //Loops through and sets the flags onto the intent
        for (int i : flags) { primeIntent.setFlags(primeIntent.getFlags() | i); }

        //Start the Activity
        this.startActivity(primeIntent);
    }

    /** As UpdateView(..), but accepts a Map variable of Key-Value pairs to transfer to the new activity.
     *
     * NOTE: Once used the values will need to be initialized into the inherited value loadedTransfers using the inherited method LoadTransfers().
     *
     * @param destination: The class of an activity meant to be loaded.
     * @param keyValuePairs: A Map variable of Key-Value pairs meant to be sent to the next activity.
     */
    protected void UpdateView(Class destination, Map<String, String> keyValuePairs) {
        Intent primeIntent = new Intent(this, destination);

        //Loops through and adds each value to the intent
        for (String key : keyValuePairs.keySet()) {
            primeIntent.putExtra(key, keyValuePairs.get(key));
        }

        //Starts the activity
        this.startActivity(primeIntent);
    }

    //*** Will create a class that takes a generic/any variable to use as the value of the key/value pairs.
    /** As UpdateView(..), but accepts an array of flags in addition to a map variable key value pairs.
     *
     * NOTE: Once used the values will need to be initialized into the inherited value loadedTransfers using the inherited method LoadTransfers().
     *
     * @param destination: The class of an activity meant to be loaded.
     * @param flags: An array of Ints representing Intent flags accessed through Intent.<FLAG NAME>
     * @param keyValuePairs: A Map variable of Key-Value pairs meant to be sent to the next activity.
     */
    protected void UpdateView(Class destination, int[] flags, Map<String,String> keyValuePairs) {
        Intent primeIntent = new Intent(this, destination);

        //Loops through and sets the flags onto the intent
        for (int i : flags) { primeIntent.setFlags(primeIntent.getFlags() | i); }

        //Loops through and adds each key-value pair to the intent
        for (String key : keyValuePairs.keySet()) { primeIntent.putExtra(key, keyValuePairs.get(key)); }


        this.startActivity(primeIntent);
    }

    /** Gets the Bundle for the activity. This initializes the inherited value "loadedTransfers".
     */
    protected void LoadTransfers() {
        this.loadedTransfers = getIntent().getExtras();
        //Once this function has completed you can use loadedTransfers.get
    }

    protected void init_BurgerMenuButton() {
        View BurgerButton = findViewById(R.id.openBurgerMenuButton);
        BurgerButton.setOnClickListener(View -> {
            UpdateView(HamburgerMenu.class);
        });
    }

    //Pre-Made Navigation
    //Basic Accounts
    /*
    private void UpdateView_ToHomeNonAdmin() {
        this.UpdateView(HomeNonAdmin.class);
    }
    private void UpdateView_ToProfilePage() {
        this.UpdateView(ProfilePage.class);
    }
    private void UpdateView_ToSlotMachine() {
        this.UpdateView(SlotMachine.class);
    }
    private void UpdateView_ToCameraScreen() {
        this.UpdateView(CameraScreen.class);
    }

    //Admin Navigation
    private void UpdateView_ToHomeAdmin() {
        this.UpdateView(HomeAdmin.class);
    }
    private void UpdateView_ToAdminUserCreator() {
        this.UpdateView(AdminUserCreator.class);
    }
    private void UpdateView_ToAdminGroups() {
        this.UpdateView(AdminGroups.class);
    }
    */
}