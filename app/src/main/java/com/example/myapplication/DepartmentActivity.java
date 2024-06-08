package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DepartmentActivity extends AppCompatActivity {

    private EditText codeEditText, nameEditText, phoneEditText;
    private DBHelper dbHelper;
    private ListView lv;
    private ArrayList<Department> list_dep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);

        dbHelper = new DBHelper(this);
        codeEditText = findViewById(R.id.codeEditText);
        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        Button btn_Del = findViewById(R.id.deleteButton);
        Button btn_Search = findViewById(R.id.searchButton);
        Button btn_Update = findViewById(R.id.updateButton);
        Button btn_Insert = findViewById(R.id.addButton);
        Button btn_exit = findViewById(R.id.btn_exit);
        lv = findViewById(R.id.resultListView);

        btn_Insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Department dep = new Department();
                dep.setCode(codeEditText.getText().toString());
                dep.setName(nameEditText.getText().toString());
                dep.setPhone(phoneEditText.getText().toString());
                if(dbHelper.insertDepartment(dep) > 0) {
                    Toast.makeText(getApplicationContext(), "Save successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Save error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = codeEditText.getText().toString().trim();
                String name = nameEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();

                list_dep = dbHelper.searchDepartment(code, name, phone);
                ArrayList<String> list_string = new ArrayList<>();
                for (Department dep : list_dep) {
                    list_string.add(dep.getCode() + " " + dep.getName() + " " + dep.getPhone());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, list_string);
                lv.setAdapter(adapter);
            }
        });

        btn_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Department dep = new Department();
                dep.setCode(codeEditText.getText().toString());
                dep.setName(nameEditText.getText().toString());
                dep.setPhone(phoneEditText.getText().toString());
                if(dbHelper.updateDepartment(dep) > 0) {
                    Toast.makeText(getApplicationContext(), "Update successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Update error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_Del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dbHelper.deleteDepartment(codeEditText.getText().toString()) > 0) {
                    Toast.makeText(getApplicationContext(), "Delete successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Delete error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lv.setOnItemClickListener((parent, view, position, id) -> {
            Department selectedDep = list_dep.get(position);
            codeEditText.setText(selectedDep.getCode());
            nameEditText.setText(selectedDep.getName());
            phoneEditText.setText(selectedDep.getPhone());
        });
    }
}
