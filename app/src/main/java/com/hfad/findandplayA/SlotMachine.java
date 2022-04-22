package com.hfad.findandplayA;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.hfad.findandplayA.viewmodels.Game;
import com.hfad.findandplayA.viewmodels.MathProblem;
import com.hfad.findandplayA.viewmodels.PlayItem;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.function.BiConsumer;

public class SlotMachine extends FindAndPlay_ParentClass implements View.OnClickListener {

    private static final String TAG = "Slot_Activity";
    private final HashMap<Integer, Boolean> loadedCatBtns = new HashMap<>();
    private final Bitmap[] _imgData = new Bitmap[4];
    private Button spinBtn;
    private Button startBtn;
    private Button rulesBtn;
    private Game game;
    private boolean spinned = false;
    private boolean audioOn = true;
    private int selectedCatIndex = -1;
    private ImageView profileOpen;

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
        profileOpen =  findViewById(R.id.profileOpen);
        startBtn.setEnabled(false);
        startBtn.setVisibility(View.GONE);
        spinBtn.setOnClickListener(this);
        startBtn.setOnClickListener(this);

        ImageView audioBtn = (ImageView) findViewById(R.id.audioBtn);
        audioBtn.setOnClickListener(ref -> {
            audioOn = !audioOn;
            audioBtn.setImageResource(audioOn
                    ? android.R.drawable.ic_lock_silent_mode_off
                    : android.R.drawable.ic_lock_silent_mode);
        });

        profileOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SlotMachine.this, ProfileView.class);
                startActivity(intent);
            }
        });


        rulesBtn = findViewById(R.id.btnRules);

        rulesBtn.setOnClickListener(view -> showDialog());

        init_BurgerMenuButton();
    }

    private void showDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.rules);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        ImageView btnclose = dialog.findViewById(R.id.btnClose);
        btnclose.setOnClickListener(view -> dialog.dismiss());
        dialog.show();

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
        rulesBtn.setVisibility(View.GONE);
        //Error retrieving items
        if (Game.categoryEmpty) {
            //Attempt to pull from Firestore again
            game = new Game(ok -> {
            });

            Toast networkErrorToast = Toast.makeText(getApplicationContext(), "Something went wrong...\nCheck internet and try again", Toast.LENGTH_LONG);
            networkErrorToast.show();
            return;
        }

        // first or subsequent spin
        startBtn.setVisibility(View.VISIBLE);
        spinBtn.setVisibility(View.GONE);
        spinBtn.setText(R.string.respinBtnTxt);
        spinBtn.setTextSize(20); // that re-spin text is a bit too large

        //Subsequent Spins
        if (spinned) {
            MathProblem mathProblem = new MathProblem();
            ArrayList<String> selections = new ArrayList<>();
            int[] selected = {-1};

            //Get a math problem and solution
            MathProblem.Question question = mathProblem.getMathProblem();
            //Create a new dialog
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            //Set the dialogs features
            dialogBuilder.setTitle(question.prompt);
            dialogBuilder.setCancelable(false);

            //Create 3 wrong answers to go with the solution
            Random random = new Random();
            int answer = Integer.parseInt(question.answer);
            selections.add(Integer.toString(answer + random.nextInt(50) + 1));
            selections.add(Integer.toString(answer - random.nextInt(50) + 1));
            selections.add(Integer.toString(answer + random.nextInt(50) + 1));
            selections.add(question.answer);

            Collections.shuffle(selections);

            CharSequence[] selectionsArray = selections.toArray(new CharSequence[0]);

            //Set the onClick for the MC options in the dialog
            dialogBuilder.setSingleChoiceItems(selectionsArray, selected[0], (dialog, s) -> {
                selected[0] = s;
                String currentItem = selectionsArray[s].toString();

                //Check if the answer matches the solution
                if (currentItem.equals(question.answer)) {
                    game.spinOne(selectedCatIndex + 1);
                    loadImgData();
                    for (PlayItem item : Game.inGameItems) {
                        Log.d("SelectedForGame", item.getItemName());
                    }
                }
                dialog.dismiss();
            }).setNegativeButton("Cancel", (dialog, s) -> dialog.dismiss());

            AlertDialog adminDialog = dialogBuilder.create();
            adminDialog.show();

        }
        //First Spin
        else {
            game.spinAll();
            loadImgData();
            spinned = true;
            for (PlayItem item : Game.inGameItems) {
                Log.d("SelectedForGame", item.getItemName());
            }
        }
        startBtn.setEnabled(true);
    }

    private void loadImgData() {
        ImageView btn1 = (ImageView) findViewById(R.id.imageButton);
        ImageView btn2 = (ImageView) findViewById(R.id.imageButton2);
        ImageView btn3 = (ImageView) findViewById(R.id.imageButton3);
        ImageView btn4 = (ImageView) findViewById(R.id.imageButton6);


        Bitmap[] imgData = _imgData;
        ImageView[] btns = {btn1, btn2, btn3, btn4};
        final int[] completed = {0};
        final BiConsumer<Bitmap, Integer> listener = (data, index) ->
        {
            imgData[index] = data;
            _imgData[index] = data == null ? _imgData[index] : data; // cache
            completed[0]++;

            // check if all images bitmap data loaded from cache or remotely
            if (completed[0] >= 4) { // increase count when you add more category buttons
                boolean hasMissing = Arrays.asList(imgData).contains(null);

                if (-1 != selectedCatIndex)
                    hasMissing = imgData[selectedCatIndex] == null;

                if (hasMissing) {
                    Toast.makeText(SlotMachine.this, "Error: not all images are loaded.", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (int i = 0; i < btns.length; i++) {
                    if (-1 != selectedCatIndex && i != selectedCatIndex)
                        continue;

                    loadedCatBtns.put(i, false);
                    loadImageWithAnimation(btns[i], imgData, i);
                }
            }
        };

        for (int i = 0; i < btns.length; i++) {
            loadCategoryItem(i, listener);
        }
    }

    public void startGame(View view) {
        Intent startGameIntent = new Intent(SlotMachine.this, PlayerSelect.class);
        startActivity(startGameIntent);
    }

    protected void loadCategoryItem(int index, BiConsumer<Bitmap, Integer> then) {
        String url = Game.inGameItems.get(index).getIcon();
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

    @SuppressLint("ClickableViewAccessibility")
    protected void loadImageWithAnimation(ImageView ref, Bitmap[] imgData, final int index) {
        int resId = getResources().getIdentifier("animate__sv" + (index + 1), "id", this.getPackageName());
        ScrollView animSv = (ScrollView) findViewById(resId);
        RelativeLayout animRl = (RelativeLayout) animSv.getChildAt(0);

        if (ref == null || -1 != selectedCatIndex) { // handle subsequent spins
            for (int i = 0; i < animRl.getChildCount(); i++) { // cleanup existing items except first
                if (i > 0) animRl.removeView(animRl.getChildAt(i));
            }

            ref = (ImageView) animRl.getChildAt(0);
        }

        animSv.setVerticalScrollBarEnabled(false);
        animSv.setHorizontalScrollBarEnabled(false);

        Handler tapHandler = new Handler();
        @SuppressLint("UseCompatLoadingForDrawables") Runnable tapRunner = () -> {
            if (loadedCatBtns.get(index) == null) { // img not loaded
                Log.e(TAG, "Error: image not loaded");
                return;
            }
            selectedCatIndex = selectedCatIndex == index ? -1 : index;

            for (int i = 0; i < imgData.length; i++) {
                ScrollView _animSv = (ScrollView) findViewById(getResources().getIdentifier("animate__sv" + (i + 1),
                        "id", SlotMachine.this.getPackageName()));

                if (null != _animSv) {
                    _animSv.setForeground(i == selectedCatIndex
                            ? getResources().getDrawable(R.drawable.sm_category_overlay) : null);
                }
            }

            if (selectedCatIndex != -1) {
                startBtn.setVisibility(View.GONE);
                spinBtn.setVisibility(View.VISIBLE);
            } else {
                startBtn.setVisibility(View.VISIBLE);
                spinBtn.setVisibility(View.GONE);
            }
        };

        animSv.setOnTouchListener((v, event) ->
        {
            tapHandler.removeCallbacks(tapRunner);
            tapHandler.postDelayed(tapRunner, 250);

            return true;
        });

        int[] imgSize = {ref.getWidth(), ref.getHeight()};
        animRl.removeView(ref);

        Bitmap[] dataAlt = new Bitmap[30 * (-1 != selectedCatIndex ? 1 : 1 + index)];
        int[] imgIds = new int[dataAlt.length];

        for (int i = 0; i < dataAlt.length; i++) {
            Bitmap data = imgData[i % (imgData.length - 1)];

            // set target image as first and last
            if (0 == i || i == dataAlt.length - 1) {
                data = imgData[index];
            }

            dataAlt[i] = data;
        }

        for (int i = 0; i < dataAlt.length; i++) {
            ImageView img = new ImageView(getBaseContext());
            img.setImageBitmap(dataAlt[i]);
            img.setAdjustViewBounds(true);

            imgIds[i] = View.generateViewId();
            img.setId(imgIds[i]);

            if (i > 0) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.BELOW, imgIds[i - 1]);
                img.setLayoutParams(params);
            }

            animRl.addView(img);
        }

        animSv.post(() -> {
            int animationDuration = 750 * (-1 != selectedCatIndex ? 2 : 1 + index);

            if (index == selectedCatIndex) { // reset selection and call associated UI events
                int tmp = selectedCatIndex;
                tapRunner.run();
                selectedCatIndex = tmp;
            }

            Handler handler = new Handler();
            handler.postDelayed(() -> {
                if (audioOn) playSound(R.raw.magicwand);

                if (index == selectedCatIndex)
                    selectedCatIndex = -1;
            }, animationDuration);

            int scrollTo = animSv.getScrollY() + (dataAlt.length - 1) * imgSize[1];
            animSv.scrollTo(0, scrollTo);

            ObjectAnimator objectAnimator = ObjectAnimator.ofInt(animSv, "scrollY", scrollTo, 0)
                    .setDuration(animationDuration);
            objectAnimator.start();
        });

        loadedCatBtns.put(index, true);
    }

    protected void playSound(int id) {
        runOnUiThread(() ->
        {
            MediaPlayer mediaPlayer = MediaPlayer.create(SlotMachine.this, id);
            mediaPlayer.start();
        });
    }

    //TODO Method needed to assign playItems to each child in the current group
}
