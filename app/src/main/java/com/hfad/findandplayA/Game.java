//TODO ****** UI AND ANIMATION NOTES ******
// * Animation is for all categories to spin in spinAll() or only selected category in spinOne()
// *

package com.hfad.findandplayA;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Game {
    private static final String TAG = "Game";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final int numOfCat = 4; //TODO update based on # of categories in db
    private ArrayList<PlayItem> catOne = new ArrayList<>();
    private ArrayList<PlayItem> catTwo = new ArrayList<>();
    private ArrayList<PlayItem> catThree = new ArrayList<>();
    private ArrayList<PlayItem> catFour = new ArrayList<>();
//    private ArrayList<PlayItem> catFive = new ArrayList<PlayItem>();
//    private ArrayList<PlayItem> catSix = new ArrayList<PlayItem>();
    public static ArrayList<PlayItem> inGameItems = new ArrayList<>();
    public static boolean categoryEmpty = false;


    public Game() {
        catOne = getAllItemsOfCategory(1);
        catTwo = getAllItemsOfCategory(2);
        catThree = getAllItemsOfCategory(3);
        catFour = getAllItemsOfCategory(4);
//        catFive = getAllItemsOfCategory(5);
//        catSix = getAllItemsOfCategory(6);
    }

    // TODO (certain # to grab instead of all?)
    // TODO Maybe better put in a different class? Seems it might be useful elsewhere.

    /**
     * Pulls all documents of of a matching category # from Firestore "Items" Collection,
     * converts them to PlayItem objects and returns them in an ArrayList.
     *
     * @param categoryNum The number to match to the "CategoryID" key in the "Items" collection.
     * @return An ArrayList of relevant PlayItems.
     */
    public ArrayList<PlayItem> getAllItemsOfCategory(int categoryNum) {
        ArrayList<PlayItem> returnedItems = new ArrayList<>();
        //Populate category 1 from firebase
        String dbCatId = "CategoryID";
        String dbCollName = "Items";
        db.collection(dbCollName).whereEqualTo(dbCatId, categoryNum)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot doc : Objects.requireNonNull(task.getResult())) {
                    returnedItems.add(doc.toObject(PlayItem.class));
                }
                //TODO REMOVE ****DEBUGGER*****
                if (!returnedItems.isEmpty()) {
                    for (PlayItem item : returnedItems) {
                        Log.d("FromFirestore", item.getItemName());
                    }
                } else {
                    //TODO add dialog that forces restart and to check internet connection (Category is empty)
                    Log.d("FromFirestore", "Category " + categoryNum + " returned no results...");
                    categoryEmpty = true;
                }
            } else {
                Log.e(TAG, "Error retrieving category " + categoryNum + " documents: ", task.getException());
                categoryEmpty = true;
            }
        });
        return returnedItems;
    }


    /**
     * Pulls a random PlayItem matching a given categoryID from the relevant Categories ArrayList (catOne, catTwo, etc....)
     *
     * @param categoryID CategoryID of item needed
     * @return A random PlayItem object containing a matching categoryID. If none exists, an empty PlayItem.
     */
    private PlayItem pickRandomItem(int categoryID) {
        Random r = new Random();
        int index;
        switch (categoryID) {
            case 1:
                if (catOne.isEmpty()) {
                    Log.e(TAG, "Error: Category 1 list is empty...");
                } else {
                    index = r.nextInt(catOne.size() - 1);
                    return catOne.get(index);
                }
                break;
            case 2:
                if (catTwo.isEmpty()) {
                    Log.e(TAG, "Error: Category 2 list is empty...");
                } else {
                    index = r.nextInt(catTwo.size() - 1);
                    return catTwo.get(index);
                }
                break;
            case 3:
                if (catThree.isEmpty()) {
                    Log.e(TAG, "Error: Category 3 list is empty...");
                } else {
                    index = r.nextInt(catThree.size() - 1);
                    return catThree.get(index);
                }
                break;
            case 4:
                if (catFour.isEmpty()) {
                    Log.e(TAG, "Error: Category 4 list is empty...");
                } else {
                    index = r.nextInt(catFour.size() - 1);
                    return catFour.get(index);
                }
                break;
//            case 5:
//                if(catFive.isEmpty()) {
//                    Log.e(TAG, "Error: Category 5 list is empty...");
//                }
//                else {
//                    index = r.nextInt(catFive.size() - 1);
//                    return catFive.get(index);
//                }
//                break;
//            case 6:
//                if(catSix.isEmpty()) {
//                    Log.e(TAG, "Error: Category 6 list is empty...");
//                }
//                else {
//                    index = r.nextInt(catSix.size() - 1);
//                    return catSix.get(index);
//                }
//                break;
        }
        Log.e(TAG, "Error: Index out of range in PlayItem.");
        return new PlayItem();
    }


    /**
     * Picks a random item from all available categories, then stores them in inGameItems ArrayList
     *
     */
    public void spinAll() {
        inGameItems.clear();
        //Pick a random item from each category
        for (int i = 1; i <= numOfCat; i++) {
            inGameItems.add(pickRandomItem(i));
        }
    }

    /**
     * Changes a single item matching the given category in inGameItems to a new randomly selected one.
     *
     * @param category  The categoryID of the item to replace.
     */
    public void spinOne(int category) {
        inGameItems.set(category - 1, pickRandomItem(category));

        //TODO REMOVE *****DEBUGGER******
        for (PlayItem item : inGameItems) {
            Log.d("SelectedNewForGame", item.getItemName());
        }

    }
}
