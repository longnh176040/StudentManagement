package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class AddFormActivity extends AppCompatActivity {
    EditText id, name, address, email;
    DatePicker dob;
    Button submit;
    Intent intent;
    //boolean status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form);

        id = findViewById(R.id.txt_add_id);
        name = findViewById(R.id.txt_add_name);
        address = findViewById(R.id.txt_add_add);
        email = findViewById(R.id.txt_add_email);
        dob = findViewById(R.id.datePicker);
        submit = findViewById(R.id.btn_submit_add);

        intent = getIntent();
        //status = intent.getBooleanExtra("ADD", false);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddStudent();
            }
        });
    }

    private void AddStudent() {
        int id = Integer.parseInt(this.id.getText().toString());
        String name = this.name.getText().toString();
        String address = this.address.getText().toString();
        String email = this.email.getText().toString();

        int day = this.dob.getDayOfMonth();
        int month = this.dob.getMonth();
        int year = this.dob.getYear();
        Date dob = new Date(year, month, day);

        String doB = dob.toString();

        intent.putExtra("ID", id);
        intent.putExtra("NAME", name);
        intent.putExtra("ADDRESS", address);
        intent.putExtra("EMAIL", email);
        intent.putExtra("DOB", dob);

        setResult(456, intent);
        finish();
    }
}
