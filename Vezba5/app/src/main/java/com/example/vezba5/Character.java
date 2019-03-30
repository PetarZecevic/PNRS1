package com.example.vezba5;

import android.graphics.drawable.Drawable;

/**
 * Klasa koja enkapsulira resurse koje ce koristiti priladodjeni Adapter.
 */
public class Character {
    public Drawable mImage;
    public String mName;
    Character(Drawable d, String n)
    {
        mImage = d;
        mName = n;
    }
}
