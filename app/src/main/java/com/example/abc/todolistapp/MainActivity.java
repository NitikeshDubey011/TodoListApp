package com.example.abc.todolistapp;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DbHelper dbHelper;
    ArrayAdapter<String> ArrayAdapter;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper=new DbHelper(this);
        listView=(ListView)findViewById(R.id.listViewOfData);
        loadTaskData();
    }

    private void loadTaskData() {
        ArrayList<String> strings=dbHelper.getTaskList();
        if(ArrayAdapter==null){
            ArrayAdapter=new ArrayAdapter<String>(this,R.layout.row,R.id.textTitle,strings);
            listView.setAdapter(ArrayAdapter);
        }
        else{
            ArrayAdapter.clear();
            ArrayAdapter.addAll(strings);
            ArrayAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        Drawable icon=menu.getItem(0).getIcon();
        icon.mutate();
        icon.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.actiopnAdd:
                final EditText taskEditText=new EditText(this);
                AlertDialog alertDialog=new AlertDialog.Builder(this)
                        .setTitle("Add New Task")
                        .setMessage("What do you want to do next?")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task=String.valueOf(taskEditText.getText());
                                dbHelper.insertNewTask(task);
                                loadTaskData();
                            }
                        })
                        .setNegativeButton("Cnacel",null)
                        .create();
                alertDialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteTask(View view){
        View parent=(View)view.getParent();
        TextView taskTextView=(TextView)findViewById(R.id.textTitle);
        String task=String.valueOf(taskTextView.getText());
        dbHelper.deleteTask(task);
        loadTaskData();
    }

}
