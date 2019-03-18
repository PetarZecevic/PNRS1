package com.example.vezba4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{
    private EditText mNewItem;
    private ArrayAdapter<String> mListAdapter;
    private TextView mListEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(this);

        mNewItem = findViewById(R.id.list_text);
        mListEmpty = findViewById(R.id.empty_view);
        mListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        ListView list = findViewById(R.id.list);
        list.setAdapter(mListAdapter);
        list.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String item = mNewItem.getText().toString();
        mListAdapter.add(item);
        mListEmpty.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String item = mListAdapter.getItem(position);
        mListAdapter.remove(item);
        if(mListAdapter.isEmpty())
            mListEmpty.setVisibility(View.VISIBLE);
    }
}

// Adapter sluzi za pretvaranje bilo kog tipa resursa u view objekat.
// ListView se prilagodjava ekranu.
// Recikliranjem na stari element nabacuje novi sadrzaj u zavisnosti kako se seta po listi.
// Analogija sa onClick - onItemClicked
// Na onClick dodajemo element u listu a sa onItemClicked brisemo element iz liste.
// Za kompleksnije View objekte kreiramo svoj layout i koristimo CustomAdapter klasu.
// nad Adapterom vrsimo sve operacije vezane za listu.
// Lokalne promenljive u okviru onCreate metode se ne unistavaju vec se nastavljaju da zive
// kroz zivotni vek aplikacije.