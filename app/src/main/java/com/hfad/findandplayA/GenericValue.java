package com.hfad.findandplayA;

import android.os.Parcel;
import android.os.Parcelable;

/** This is a generic value object. When initialized, value will be empty until set is used.
 *
 * @param <T> The type of value wanting to be used when called
 */
public class GenericValue<T> implements Parcelable {
    //Value to be held
    private T value;

    /** This is the constructor for the class.
     *
     * @param value: The value to hold
     */
    public GenericValue(T value) { this.value = value; }

    protected GenericValue(Parcel in) {
    }

    public static final Creator<GenericValue> CREATOR = new Creator<GenericValue>() {
        @Override
        public GenericValue createFromParcel(Parcel in) {
            return new GenericValue(in);
        }

        @Override
        public GenericValue[] newArray(int size) {
            return new GenericValue[size];
        }
    };

    /** This changes the value to whatever is needed, of the original type that this class was initialized with
     *
     * @param value: The value to hold
     */
    public void set(T value) { this.value = value; }

    /** Simple get statement for the value.
     *
     * @return Returns the value currently being held.
     */
    public T get() { return this.value; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
