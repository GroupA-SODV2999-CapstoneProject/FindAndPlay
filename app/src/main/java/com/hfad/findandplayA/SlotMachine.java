package com.hfad.findandplayA;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
public class SlotMachine extends AppCompatActivity {

    //TODO change id of relevant buttons/images once interface is committed by other group (button -> spinBtn)
    private Button spinBtn;
    //TODO Object array or hashmap that stores a number of items (one for each category)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot_machine);
        spinBtn = (Button)findViewById((R.id.spinBtn));
        //TODO Call method to populate item arrays (performance might be an issue here, calling this in the background or even during a splash screen at startup might be better)
    }

    //TODO Method to pull available items from each category (certain # to grab instead of all?)


    public void spinSlotMachine(View view) {
        //Change text of spin button
        if(view.getId() == R.id.spinBtn) {
            spinBtn.setText("Spin for item again");
            //TODO check size of new text fits in button
        }


        //TODO Start animation

        //TODO Assign values of spin result
    }

    //TODO Method to respin for an individual item (Limited or restricted to admin override)

    //

    //TODO Method to assign those items to each child in the current group
}