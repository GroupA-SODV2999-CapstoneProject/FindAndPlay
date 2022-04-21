package com.hfad.findandplayA;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ImaginativePlayActivity extends FindAndPlay_ParentClass {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imaginative_play);
    }

    /** Runs the super.onStart() and declares the button onClick
     *
     */
    @Override
    protected void onStart() {
        super.onStart();

        Button buttonReturn = (Button) findViewById(R.id.button_ReturnToMainMenu);
        buttonReturn.setOnClickListener(button_ReturnToMainMenu_onClick());
    }

    /** Changes view to the MainActivity
     *
     * @return null
     */
    protected View.OnClickListener button_ReturnToMainMenu_onClick() {
        UpdateView(MainActivity.class);
        return null;
    }
}
