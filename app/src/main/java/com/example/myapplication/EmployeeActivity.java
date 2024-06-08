package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Set;

public class EmployeeActivity extends AppCompatActivity {
    private Button btnNewImg, btnExit, btnAdd, btnUpdate, btnSearch, btnDelete;
    private ImageView imgView;
    private EditText codeEditText, nameEditText, phoneEditText, addressEditText;
    private Spinner departmentSpinner;
    private ListView resultListView;
    DBHelper dbHelper = new DBHelper(this);

    ArrayList<Department> list_dep = new ArrayList<>();
    ArrayList<String> listItem = new ArrayList<>();
    Bitmap emp_image;
    private RadioButton maleRadioButton, femaleRadioButton;
    private static final int REQUEST_CODE = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empoyee);

        dbHelper = new DBHelper(this);

        btnNewImg = findViewById(R.id.newImg_btn);
        btnExit = findViewById(R.id.btnExit);
        btnAdd = findViewById(R.id.addButton_em);
        btnUpdate = findViewById(R.id.updateButton_em);
        btnSearch = findViewById(R.id.searchButton_em);
        btnDelete = findViewById(R.id.deleteButton_em);
        imgView = findViewById(R.id.img_avatar);
        codeEditText = findViewById(R.id.codeEditText);
        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        addressEditText = findViewById(R.id.addressEditText);
        departmentSpinner = findViewById(R.id.item_spinner);
        maleRadioButton = findViewById(R.id.maleRadioButton);
        femaleRadioButton = findViewById(R.id.femaleRadioButton);
        resultListView = findViewById(R.id.resultListView);

        maleRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                femaleRadioButton.setChecked(false);
            }
        });

        femaleRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maleRadioButton.setChecked(false);
            }
        });

        btnNewImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEmployee();
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEmployee();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEmployee();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmployee();
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadDepartmentsIntoSpinner();

        // Set OnItemClickListener for the resultListView
        resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Employee selectedEmployee = (Employee) parent.getItemAtPosition(position);
                populateInputFields(selectedEmployee);
            }
        });
    }

    private void loadDepartmentsIntoSpinner() {
        list_dep = dbHelper.getAllDepartment();
        for (Department dep : list_dep) {
            listItem.add(dep.getCode() + "\t" + dep.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, listItem);
        departmentSpinner.setAdapter(adapter);
    }

    private void addEmployee() {
        if (!validateInput()) {
            Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String empCode = codeEditText.getText().toString();
        String empName = nameEditText.getText().toString();
        String empGender = maleRadioButton.isChecked() ? "Male" : "Female";
        String selectedItem = departmentSpinner.getSelectedItem().toString();
        String[] depCode = selectedItem.split("\t");
        String empDepCode = depCode[1];
        String empPhone = phoneEditText.getText().toString();
        String empAddress = addressEditText.getText().toString();
        byte[] empImage = ConvertToArrayByte(imgView);


        Employee emp = new Employee();
        emp.setEmpcode(empCode);
        emp.setName(empName);
        emp.setGender(empGender);
        emp.setDepcode(empDepCode);
        emp.setPhone(empPhone);
        emp.setAddress(empAddress);
        emp.setImage(empImage);

        if (dbHelper.insertEmployee(emp) > 0) {
            Toast.makeText(getApplicationContext(), "Save successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Save error", Toast.LENGTH_SHORT).show();
        }
    }


    private void searchEmployee() {
        String empCode = codeEditText.getText().toString();
        String name = nameEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String address = addressEditText.getText().toString();

        ArrayList<Employee> results = dbHelper.searchEmployee(empCode, name, phone, address);

        if (results.isEmpty()) {
            Toast.makeText(getApplicationContext(), "No employees found", Toast.LENGTH_SHORT).show();
        } else {
            EmployeeAdapter adapter = new EmployeeAdapter(this, results);
            resultListView.setAdapter(adapter);
        }
    }


    private void updateEmployee() {
        if (!validateInput()) {
            Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String empCode = codeEditText.getText().toString();
        String empName = nameEditText.getText().toString();
        String empGender = maleRadioButton.isChecked() ? "Male" : "Female";
        String selectedItem = departmentSpinner.getSelectedItem().toString();
        String[] depCode = selectedItem.split("\t");
        String empDepCode = depCode[0];
        String empPhone = phoneEditText.getText().toString();
        String empAddress = addressEditText.getText().toString();
        byte[] empImage = ConvertToArrayByte(imgView);

        Employee emp = new Employee();
        emp.setEmpcode(empCode);
        emp.setName(empName);
        emp.setGender(empGender);
        emp.setDepcode(empDepCode);
        emp.setPhone(empPhone);
        emp.setAddress(empAddress);
        emp.setImage(empImage);

        if (dbHelper.updateEmployee(emp) > 0) {
            Toast.makeText(getApplicationContext(), "Update successfully", Toast.LENGTH_SHORT).show();
            searchEmployee();
        } else {
            Toast.makeText(getApplicationContext(), "Update error", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteEmployee() {
        EmployeeAdapter adapter = (EmployeeAdapter) resultListView.getAdapter();
        if (adapter == null) {
            Toast.makeText(this, "No employees to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        Set<String> selectedEmployeeCodes = adapter.getSelectedEmployeeCodes();
        if (selectedEmployeeCodes.isEmpty()) {
            Toast.makeText(this, "No employees selected", Toast.LENGTH_SHORT).show();
            return;
        }

        for (String code : selectedEmployeeCodes) {
            dbHelper.deleteEmployee(code);
        }

        Toast.makeText(this, "Selected employees deleted", Toast.LENGTH_SHORT).show();
        searchEmployee();
    }

    private boolean validateInput() {
        return !codeEditText.getText().toString().isEmpty() &&
                !nameEditText.getText().toString().isEmpty() &&
                !phoneEditText.getText().toString().isEmpty() &&
                !addressEditText.getText().toString().isEmpty() &&
                departmentSpinner.getSelectedItem() != null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            if (photo != null) {
                emp_image = photo;
                imgView.setImageBitmap(photo);
            } else {
                Toast.makeText(getApplicationContext(), "No Image!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public byte[] ConvertToArrayByte(ImageView img) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) img.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void populateInputFields(Employee employee) {
        codeEditText.setText(employee.getEmpcode());
        nameEditText.setText(employee.getName());
        phoneEditText.setText(employee.getPhone());
        addressEditText.setText(employee.getAddress());

        if (employee.getGender().equalsIgnoreCase("Male")) {
            maleRadioButton.setChecked(true);
            femaleRadioButton.setChecked(false);
        } else {
            femaleRadioButton.setChecked(true);
            maleRadioButton.setChecked(false);
        }

        byte[] imageBytes = employee.getImage();
        if (imageBytes != null && imageBytes.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            imgView.setImageBitmap(bitmap);
        } else {
            imgView.setImageResource(R.drawable.avatar);
        }

        for (int i = 0; i < departmentSpinner.getCount(); i++) {
            String item = (String) departmentSpinner.getItemAtPosition(i);
            if (item.startsWith(employee.getDepcode() + "\t")) {
                departmentSpinner.setSelection(i);
                break;
            }
        }
    }

}
