package com.example.vezba5;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity{
    private ArrayAdapter<String> mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CharacterAdapter adapter = new CharacterAdapter(this);
        adapter.addCharacter(new Character(getResources().getDrawable(R.drawable.naruto), "Naruto"));
        adapter.addCharacter(new Character(getResources().getDrawable(R.drawable.sasuke), "Sasuke"));
        adapter.addCharacter(new Character(getResources().getDrawable(R.drawable.gara), "Gara"));
        adapter.addCharacter(new Character(getResources().getDrawable(R.drawable.shikamaru), "Shikamaru"));
        adapter.addCharacter(new Character(getResources().getDrawable(R.drawable.kakashi), "Kakashi"));

        ListView list = findViewById(R.id.naruto_list);
        list.setAdapter(adapter);
    }
}
