package com.agriculture.ezagro;

import com.google.firebase.database.DatabaseReference;

public class Datapoint {
    int X_Value;
    float Y_Value;

    public Datapoint()
    {

    }

    public Datapoint(int X_Value, float Y_Value)
    {
        this.X_Value=X_Value;
        this.Y_Value=Y_Value;
    }

    public int getX_Value() {
        return X_Value;
    }

    public float getY_Value() {
        return Y_Value;
    }

}
