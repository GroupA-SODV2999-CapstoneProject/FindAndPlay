package com.hfad.findandplayA;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SlotMachine extends AppCompatActivity {

    private static final String TAG = "MyActivity";
    private static ArrayList<PlayItem> catOne; //TODO Update when items created
    private static ArrayList<PlayItem> catTwo; //TODO Update when items created
    private static ArrayList<PlayItem> catThree; //TODO Update when items created
    private static ArrayList<PlayItem> catFour; //TODO Update when items created
    private static ArrayList<PlayItem> catFive; //TODO Update when items created
    private final String dbCatId = "CategoryID";
    private final String dbName = "CategoryName";
    private final String dbDes = "Description";
    //TODO change id of relevant buttons/images once interface is committed by other group (button -> spinBtn)
    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    private ValueEventListener dbListener;
    private Button spinBtn;
    private String selectedName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot_machine);
        spinBtn = (Button) findViewById((R.id.spinBtn));
        populateCategories(); //Performance might be an issue here, calling this in the background or even during a splash screen at startup might be better
    }

    //Initialize array of categories from database available items from each category (certain # to grab instead of all?)
    private void populateCategories() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query query = ref.child("Items").orderByChild(dbCatId);

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                //TODO Need to see what the JSON structure of the snapshot is so we can convert the results properly
                switch (snapshot.child(dbCatId).getValue().toString()) {
                    case "1":
                        catOne.add(new PlayItem(snapshot.child(dbCatId).getValue().toString(), snapshot.child(dbName).getValue().toString(), snapshot.child(dbDes).getValue().toString()));
                        break;
                    case "2":
                        catTwo.add(new PlayItem(snapshot.child(dbCatId).getValue().toString(), snapshot.child(dbName).getValue().toString(), snapshot.child(dbDes).getValue().toString()));
                        break;
                    case "3":
                        catThree.add(new PlayItem(snapshot.child(dbCatId).getValue().toString(), snapshot.child(dbName).getValue().toString(), snapshot.child(dbDes).getValue().toString()));
                        break;
                    case "4":
                        catFour.add(new PlayItem(snapshot.child(dbCatId).getValue().toString(), snapshot.child(dbName).getValue().toString(), snapshot.child(dbDes).getValue().toString()));
                        break;
                    case "5":
                        catFive.add(new PlayItem(snapshot.child(dbCatId).getValue().toString(), snapshot.child(dbName).getValue().toString(), snapshot.child(dbDes).getValue().toString()));
                        break;
                    default:
                        throw new IllegalStateException("Unexpected category value: " + snapshot.child(dbCatId));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                switch (snapshot.child(dbCatId).getValue().toString()) {

                    case "1":
                        catOne.remove(new PlayItem(snapshot.child(dbCatId).getValue().toString(), snapshot.child(dbName).getValue().toString(), snapshot.child(dbDes).getValue().toString()));
                        break;
                    case "2":
                        catTwo.remove(new PlayItem(snapshot.child(dbCatId).getValue().toString(), snapshot.child(dbName).getValue().toString(), snapshot.child(dbDes).getValue().toString()));
                        break;
                    case "3":
                        catThree.remove(new PlayItem(snapshot.child(dbCatId).getValue().toString(), snapshot.child(dbName).getValue().toString(), snapshot.child(dbDes).getValue().toString()));
                        break;
                    case "4":
                        catFour.remove(new PlayItem(snapshot.child(dbCatId).getValue().toString(), snapshot.child(dbName).getValue().toString(), snapshot.child(dbDes).getValue().toString()));
                        break;
                    case "5":
                        catFive.remove(new PlayItem(snapshot.child(dbCatId).getValue().toString(), snapshot.child(dbName).getValue().toString(), snapshot.child(dbDes).getValue().toString()));
                        break;
                    default:
                        throw new IllegalStateException("Unexpected category  value: " + snapshot.child(dbCatId));
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Error connecting to database...");
                Toast.makeText(getApplicationContext(), "Failed to load items. Please try again later...", Toast.LENGTH_LONG).show();
            }
        });


//        db.child(dbCatId/*).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>(){
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if(!task.isSuccessful()) {
//                    Log.e("firebase", "Error retrieving items: ", task.getException());
//                }
//                else {
//                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
//                }
//            }
//        });
    }

    public void spinSlotMachine(View view) {
        //Change text of spin button
        if (view.getId() == R.id.spinBtn) {
            spinBtn.setText("Spin for item again");
            //TODO check if size of new text fits in button
        }


        //TODO Start animation

        //TODO Assign values of spin result
    }

    //TODO Method to respin for an individual item (Limited or restricted to admin override)
    public void respinSlotMachine(View view, int category) {
        //TODO prompt for admin permission first

        switch (category) {
            case 1:
                //TODO Spin for individual category
                break;
            case 2:
                //TODO Spin for individual category
                break;
            case 3:
                //TODO Spin for individual category
                break;
            case 4:
                //TODO Spin for individual category
                break;
            case 5:
                //TODO Spin for individual category
                break;
            default:
                throw new IllegalStateException("Unexpected category  value: " + category);

        }
    }

    public void selectItemForSpin(View view) {
        selectedName = getResources().getResourceName(view.getId());
    }

    //TODO Method to assign those items to each child in the current group
}

//REMOVE Test code
//*********************************
class PlayItem {
    public String catID;
    public String catName;
    public String descrip;

    public PlayItem(String id, String name, String description) {
        catID = id;
        catName = name;
        descrip = description;
    }
}
//**********************************
