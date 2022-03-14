//TODO Summary (Marked on relevant lines in code)
// * ****** UI AND SLOT MACHINE ANIMATION ******
// * Animation is either for all categories to spin or only a selected category as noted in spin
// * I added a "Start" button to the layout (it will stay deactivated/hidden until the slot machine has spun at least once), it will need to be placed/sized appropriately in UI
// * After the animation is done and there are images associated with Items in Firestore, I'll need to add assigning the results of a spin (inGameItems) to the UI
// *
// * ****** CHILD GROUP / MEMBER ******
// * Needs to assign the final inGameItems to each child in the current game (Not sure if that will be in different class or this one, feel free to move it wherever)
// *
// * ****** NAVIGATION ******
// * Once the "Start" (id = startBtn) is clicked we'll need to navigate to whatever activity is next (in-game) or we have to change the UI of this activity to hide options to spin the slot machine.
// *
// * ****** ADMIN APPROVAL FOR RESPINNING ******
// * We're going to need admin approval for approving photos of items in game, however we implement that needs to be used to approve spinning the slot machine again (I was imagining a math question in a Dialog prompt)
// * If this is implemented in a different class the method used will need to return a boolean and replace any adminPermission() refs in this class
// *
// * ****** MISC ******
// * After animation is finished I'll need to add item highlighting and selection functionality for spinning again (it's just set to spin again on category 1 for testing)



package com.hfad.findandplayA;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.hfad.findandplayA.viewmodels.Game;
import com.hfad.findandplayA.viewmodels.PlayItem;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class SlotMachine extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Slot_Activity";
    private int counter = 0;
    private TextView timerText;
    private Button spinBtn;
    private Button startBtn;
    private Game game;
    private boolean spinned = false;
    private boolean errState = false;
    private PlayItem playItem;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot_machinev2);

        // hide loading spinner once data loaded from firestore
        RelativeLayout spinner = (RelativeLayout) findViewById(R.id.layout_loading_spinner);
        RelativeLayout main = (RelativeLayout) findViewById(R.id.layout_main_content);
        main.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);

        // items loading is tied to constructor, so instantiating a game obj when activity is started
        game = new Game(ok -> {
            spinner.setVisibility(View.GONE);
            main.setVisibility(View.VISIBLE);
        });
        timerText = findViewById(R.id.timerText);
        spinBtn = (Button) findViewById(R.id.spinBtn);
        startBtn = (Button) findViewById(R.id.startBtn);
        startBtn.setEnabled(false);
        startBtn.setVisibility(View.GONE);
        spinBtn.setOnClickListener(this);
        startBtn.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        // Jamie, this will reload the game each time you bring the app to foreground, e.g when switching
        // between apps and opening the app again, or when phone locks then unlocks and app openned
        // @see https://developer.android.com/guide/components/activities/activity-lifecycle#onresume
        super.onResume();
        game = new Game(ok -> {});
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.spinBtn) {
            spin(v);
            new CountDownTimer(30000, 1000) {
                @Override
                public void onTick(long l) {
                    NumberFormat format = new DecimalFormat("00");
                    long hour = (l/3600000)%24;
                    long minute = (l/60000)%60;
                    long second = (l/1000)%60;
                    timerText.setText(format.format(hour) + ":" + format.format(minute) + ":" + format.format(second));
                    startBtn.setEnabled(false);
                }

                @Override
                public void onFinish() {
                    timerText.setText("00:00:00");
                    Toast.makeText(SlotMachine.this,"Time Over!", Toast.LENGTH_SHORT).show();
                    startBtn.setEnabled(true);
                }
            }.start();
        } else if (id == R.id.startBtn) {
            startGame(v);
        } else {
            Log.e(TAG, "Error:View ID not recognized after onClick event from onClick. ID = " + v.getId());
        }
    }

    /**
     * Starts the animation of the slot machine spinning (either one or all categories depending on
     * game state). It then calls on pickRandomItem to populate the inGameItems and displays them on
     * the slot machine UI.
     *
     * @param view View passed from onClickListener that calls spin.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void spin(View view) {
        //Error retrieving items
        if (Game.categoryEmpty) {
            //Attempt to pull from Firestore again
            game = new Game(ok -> {});

            Toast networkErrorToast = Toast.makeText(getApplicationContext(), "Something went wrong...\nCheck internet and try again", Toast.LENGTH_LONG);
            networkErrorToast.show();
        }

        //First spin
        else if (!spinned) {
            startBtn.setVisibility(View.VISIBLE);
            spinBtn.setVisibility(View.GONE);
            spinBtn.setText(R.string.respinBtnTxt);

            ImageView btn1 = (ImageView) findViewById(R.id.imageButton);
            ImageView btn2 = (ImageView) findViewById(R.id.imageButton2);
            ImageView btn3 = (ImageView) findViewById(R.id.imageButton3);

            //TODO Start animation of all categories

            game.spinAll();


            loadCategoryItem(0, btn1);
            loadCategoryItem(1, btn2);
            loadCategoryItem(2, btn3);

            for (PlayItem item : Game.inGameItems) {
                Log.d("ITEM", item.getIcon());
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
        if(true) {
            //TODO maybe an animation of a green checkmark to indicate it was correct?
            return true;
        }
        else {
            //TODO maybe an animation of a red x to show it was incorrect?
            return false;

        }
    }

    public void startGame(View view) {
        //TODO Add Navigation to the next in-game activity (or change UI so we don't spin anymore)?
    }

    protected void loadCategoryItem( int index, ImageView ref )
    {
        Game.getImageBitmap(Game.inGameItems.get(index).getIcon(), data -> runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ref.setImageBitmap(data);
            }
        }));
    }

    //TODO Method needed to assign playItems to each child in the current group
}
