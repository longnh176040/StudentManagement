package com.example.studentmanagement;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentmanagement.adapters.StudentAdapter;
import com.example.studentmanagement.model.StudentModel;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.bloco.faker.Faker;

public class MainActivity extends AppCompatActivity {
    List<StudentModel> studentModels;
    ListView listView;
    ActionBar actionBar;
    Faker faker;
    StudentAdapter studentAdapter;

    SQLiteDatabase DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Student Management");
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        faker = new Faker();

        studentModels = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            studentModels.add(new StudentModel(i, faker.name.firstName(), faker.name.lastName() + "@gmail.com", faker.address.streetAddress(), faker.date.birthday(18, 22).toString()));
        }

        File storagePath = getApplication().getFilesDir();
        String path = storagePath + "/" + "student_management_db";

        try {
            DB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
            DB.execSQL("create table if not exists students("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + "dob date,"
                    + "address text,"
                    + "email text);");

            for (int i = 0; i < studentModels.size(); i++) {
                StudentModel student = studentModels.get(i);

                ContentValues rowValues = new ContentValues();
                rowValues.put("name", student.name);
                rowValues.put("dob", student.doB.toString());
                rowValues.put("address", student.address);
                rowValues.put("email", student.email);

                DB.insert("students", null, rowValues);
            }

            DB.close();
            Toast toast = Toast.makeText(getApplicationContext(), "\nSUCCESS", Toast.LENGTH_LONG);
            toast.show();
        } catch (SQLException e) {
            Log.v("ERROR", e.getMessage());
            Toast toast = Toast.makeText(getApplicationContext(), "\nERROR " + e.getMessage(), Toast.LENGTH_LONG);
            toast.show();
        }

        try {
            DB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);

            Cursor cursor = DB.rawQuery("select * from students", null);
            cursor.moveToPosition(-1);

            while (cursor.moveToNext()) {
                studentModels.add(new StudentModel(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("address")),
                        cursor.getString(cursor.getColumnIndex("email")),
                        cursor.getString(cursor.getColumnIndex("dob"))
                ));
            }
            cursor.close();
            DB.close();
            Toast toast = Toast.makeText(getApplicationContext(), "\nSUCCESS access", Toast.LENGTH_LONG);
            toast.show();
        } catch (SQLException e) {
            Log.v("ERROR", e.getMessage());
            Toast toast = Toast.makeText(getApplicationContext(), "\nERROR " + e.getMessage(), Toast.LENGTH_LONG);
            toast.show();
        }

        listView = findViewById(R.id.list_view);
        studentAdapter = new StudentAdapter(studentModels, this);
        listView.setAdapter(studentAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("TAG", "click action");
                InfoDialog(position);
            }
        });

        registerForContextMenu(listView);
        listView.setLongClickable(true);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
//        if (v.getId() == R.id.list_view) {
//            ListView lv = (ListView) v;
//            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
//            View view = (View) lv.getItemAtPosition(info.position);
//            menu.setHeaderTitle("Select action");
//            menu.add(0, 1, 0, "Reply");
//            menu.add(0, 2, 0, "Delete");
//        }

        menu.setHeaderTitle("Select action");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mnu_upd) {
            Log.v("TAG", "UPDATE action");

        } else if (item.getItemId() == R.id.mnu_del) {
            Log.v("TAG", "DELETE action");
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            StudentModel s = studentModels.get(info.position);
            studentModels.remove(s);
            studentAdapter.notifyDataSetChanged();
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mnu_add) {
            //Log.v("TAG", "UPDATE action");
            Intent form = new Intent(MainActivity.this, AddFormActivity.class);
            startActivityForResult(form, 123);
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == 456) {
            Log.v("TAG", "SUCCESS");

            //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            //String dateInString = "7-Jun-2013";
            //Date date;
            //try {
                //date = formatter.parse(data.getStringExtra("DOB"));
                studentModels.add(new StudentModel(data.getIntExtra("ID", 0),
                        data.getStringExtra("NAME"), data.getStringExtra("EMAIL"),
                        data.getStringExtra("ADDRESS"), data.getStringExtra("DOB")));
                studentAdapter.notifyDataSetChanged();
            //} catch (ParseException e) {
              //  Log.v("Exception", e.getLocalizedMessage());
            //}
        }
    }

    private void InfoDialog(int position) {
        Dialog info = new Dialog(this);
        info.setContentView(R.layout.infor_dialog);

        TextView txt_id = info.findViewById(R.id.txt_id_dialog);
        TextView txt_name = info.findViewById(R.id.txt_name_dialog);
        TextView txt_dob = info.findViewById(R.id.txt_dob_dialog);
        TextView txt_add = info.findViewById(R.id.txt_add_dialog);
        TextView txt_email = info.findViewById(R.id.txt_email_dialog);

        txt_id.setText(studentModels.get(position).id);
        txt_name.setText(studentModels.get(position).name);
        txt_dob.setText(studentModels.get(position).doB.toString().trim());
        txt_add.setText(studentModels.get(position).address.trim());
        txt_email.setText(studentModels.get(position).email);

        Button btn_ok = info.findViewById(R.id.btn_ok_dialog);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.dismiss();
            }
        });

        info.show();
        info.getWindow().setLayout(1050, 800);
    }
}