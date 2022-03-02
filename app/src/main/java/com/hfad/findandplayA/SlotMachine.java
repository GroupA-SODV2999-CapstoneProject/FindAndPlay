//TODO ****** UI AND ANIMATION NOTES ******
// * Animation is for all categories to spin or only  selected category as noted in onClickListener
// * We need a "Go" or "Start" button added to the layout (it will stay deactivated until the slot machine has spun at least once) (id = startBtn)
// * The "SPIN!" button needs to be given id = spinBtn

package com.hfad.findandplayA;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SlotMachine extends AppCompatActivity {

    private static final String TAG = "Slot_Activity";
    private final Toast networkErrorToast = Toast.makeText(getApplicationContext(), "Error: Please check your internet connection and try again...", Toast.LENGTH_LONG);
    private Button spinBtn;
    private Button startBtn;
    private Game game;
    private boolean spinned = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot_machine);
        spinBtn = (Button) findViewById((R.id.button));
        startBtn = (Button) findViewById((R.id.startBtn));
        startBtn.setEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        game = new Game();
        if (Game.categoryEmpty) {
            networkErrorToast.show();
        }
    }

    /**
     * Starts the animation of the slot machine spinning (either one or all categories depending on
     * game state). It then calls on pickRandomItem to populate the inGameItems and displays them on
     * the slot machine UI.
     *
     * @param view View passed from onClickListener that calls spin.
     */
    public void spin(View view) {  //TODO change to onclick that calls game.spin
        //First spin
        if (!spinned) {
            spinBtn.setText(R.string.respinBtnTxt);

            //TODO Start animation of all categories

            game.spinAll();

            for (int i = 0; i < Game.inGameItems.size(); i++) {
                //TODO Display selected items from inGameItems
                //Add each item to UI (Note: items are stored sorted in ascending order by category # (ie. 0 == category 1)
            }

            startBtn.setEnabled(true);
            spinned = true;

            //TODO REMOVE *****DEBUGGER******
            for (PlayItem item : Game.inGameItems) {
                Log.d("SelectedForGame", item.getItemName());
            }
        }
        //Subsequent spin(s)
        else {
            if (adminPermission()) {
                //TODO Get category number from selected
                int category = 1; //TODO Update

                //TODO start animation on single category

                game.spinOne(category);

                //TODO update UI to be new item (Note: new item will be "game.inGameItems.get(category - 1)")
            }

            //TODO REMOVE *****DEBUGGER******
            for (PlayItem item : Game.inGameItems) {
                Log.d("SelectedNewForGame", item.getItemName());
            }
        }
    }


    /**
     * Prompt user through an AlertDialog to answer a random math question (easy, but hard enough that under 5 can't answer).
     *
     * @return True if answered correctly, else false.
     */
    private boolean adminPermission() {
        //TODO add prompt for random math question to act as admin approval of an action
        //Check submitted answer
        //TODO maybe an animation of a green checkmark to indicate it was correct?
        //TODO maybe an animation of a red x to show it was incorrect
        return true;
    }

    public void startGame(View view) {
        //TODO Add Navigation to the next in-game activity?
    }


    //TODO Method needed to assign playItems to each child in the current group
}
