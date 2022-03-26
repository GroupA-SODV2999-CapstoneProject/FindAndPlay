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

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.hfad.findandplayA.viewmodels.Game;
import com.hfad.findandplayA.viewmodels.PlayItem;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.BiConsumer;

public class SlotMachine extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Slot_Activity";
    private static final Integer ANIMATION_DURATION_MS = 3 *1000; // milliseconds
    private static final Integer ANIMATION_DELAY_MS = 250; // milliseconds
    private Button spinBtn;
    private Button startBtn;
    private Game game;
    private boolean spinned = false;
    private boolean errState = false;
    private PlayItem playItem;
    private boolean audioOn = true;

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

        spinBtn = (Button) findViewById(R.id.spinBtn);
        startBtn = (Button) findViewById(R.id.startBtn);
        startBtn.setEnabled(false);
        startBtn.setVisibility(View.GONE);
        spinBtn.setOnClickListener(this);
        startBtn.setOnClickListener(this);

        ImageView audioBtn = (ImageView) findViewById(R.id.audioBtn);
        audioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ref) {
                audioOn = ! audioOn;
                audioBtn.setImageResource( audioOn
                        ? android.R.drawable.ic_lock_silent_mode_off
                        : android.R.drawable.ic_lock_silent_mode );
            }
        });
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

            Bitmap[] imgData = new Bitmap[3];
            ImageView[] btns = { btn1, btn2, btn3 };
            final int[] completed = {0};
            final BiConsumer<Bitmap,Integer> listener = (data, index) ->
            {
                imgData[index] = data;
                completed[0]++;

                Log.d(TAG, "--------- index: " + index);
                Log.d(TAG, "--------- is null: " + (null == data));
                Log.d(TAG, "--------- Has null: " + Arrays.asList(imgData).contains(null));

                // check if all images bitmap data loaded from cache or remotely
                if ( completed[0] >= 3 ) { // increase count when you add more category buttons
                    if ( Arrays.asList(imgData).contains(null) ) {
                        Toast.makeText(SlotMachine.this, "Error: not all images are loaded.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    for ( int i=0; i<btns.length; i++ )
                        loadImageWithAnimation(btns[i], imgData, i);

                    // btns[0].setImageBitmap(imgData[0]);
                    // btns[1].setImageBitmap(imgData[1]);
                    // btns[2].setImageBitmap(imgData[2]);
                }
            };

            loadCategoryItem(0, listener);
            loadCategoryItem(1, listener);
            loadCategoryItem(2, listener);

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

        Intent startGameIntent = new Intent(SlotMachine.this, PlayerSelect.class);
        startActivity(startGameIntent);
    }

    protected void loadCategoryItem( int index, BiConsumer<Bitmap,Integer> then )
    {
        String url = Game.inGameItems.get(index).getIcon();
        // url = "https://picsum.photos/300/300?random=2&_=" + java.util.UUID.randomUUID().toString();

        // load image from cache or url into view
        Picasso.get().load(url).into(new Target() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                then.accept(bitmap, index);
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                then.accept(null, index);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });
    }

    protected void loadImageWithAnimation( ImageView ref, Bitmap[] imgData, int index )
    {
        int resId = getResources().getIdentifier("animate__sv" + (index+1), "id", this.getPackageName());
        ScrollView animSv = (ScrollView) findViewById(resId);
        RelativeLayout animRl = (RelativeLayout) animSv.getChildAt(0);

        animSv.setVerticalScrollBarEnabled(false);
        animSv.setHorizontalScrollBarEnabled(false);
        animSv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) { return true; }
        });

        int[] imgSize = {ref.getWidth(), ref.getHeight()};
        animRl.removeView(ref);

        Bitmap[] dataAlt = new Bitmap[30*(1+index)];
        int[] imgIds = new int[dataAlt.length];

        for ( int i=0; i<dataAlt.length; i++ ) {
            Bitmap data = imgData[ i % (imgData.length-1) ];

            // set target image as first and last
            if ( 0 == i || i == dataAlt.length -1 ) {
                data = imgData[index];
            }

            dataAlt[i] = data;
        }

        for ( int i=0; i<dataAlt.length; i++ ) {
            ImageView img = new ImageView(getBaseContext());
            img.setImageBitmap(dataAlt[i]);
            img.setAdjustViewBounds(true);

            imgIds[i] = View.generateViewId();
            img.setId(imgIds[i]);

            if ( i > 0 ) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.BELOW, imgIds[i - 1]);
                img.setLayoutParams(params);
            }

            animRl.addView(img);
        }

        animSv.post(new Runnable() {
            @Override
            public void run() {
                int animationDuration = 750*(1+index);

                if ( audioOn ) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            if ( audioOn ) playSound(R.raw.magicwand);
                        }
                    }, animationDuration);
                }

                int scrollTo = animSv.getScrollY() + (dataAlt.length -1)*imgSize[1];
                animSv.scrollTo(0, scrollTo);

                ObjectAnimator objectAnimator = ObjectAnimator.ofInt(animSv, "scrollY", scrollTo, 0)
                        .setDuration(animationDuration);
                objectAnimator.start();
            }
        });
    }

    protected void playSound( int id )
    {
        runOnUiThread(() ->
        {
            MediaPlayer mediaPlayer = MediaPlayer.create(SlotMachine.this, id);
            mediaPlayer.start();
        });
    }

    //TODO Method needed to assign playItems to each child in the current group
}
