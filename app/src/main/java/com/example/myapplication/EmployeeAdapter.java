package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class EmployeeAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Employee> employees;
    private LayoutInflater inflater;
    private Set<String> selectedEmployeeCodes = new HashSet<>();

    public EmployeeAdapter(Context context, ArrayList<Employee> employees) {
        this.context = context;
        this.employees = employees;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return employees.size();
    }

    @Override
    public Object getItem(int position) {
        return employees.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_list_empoyee, parent, false);
        }

        ImageView image = convertView.findViewById(R.id.image);
        TextView empCode = convertView.findViewById(R.id.empCode);
        TextView empName = convertView.findViewById(R.id.empName);
        TextView empGender = convertView.findViewById(R.id.empGender);
        TextView empPhone = convertView.findViewById(R.id.empPhone);
        TextView empAddress = convertView.findViewById(R.id.empAddress);
        TextView empDepartment = convertView.findViewById(R.id.empDepartment);
        CheckBox checkBox = convertView.findViewById(R.id.checkBox);

        Employee employee = employees.get(position);

        empCode.setText(employee.getEmpcode());
        empName.setText(employee.getName());
        empGender.setText(employee.getGender());
        empPhone.setText(employee.getPhone());
        empAddress.setText(employee.getAddress());
        empDepartment.setText(employee.getDepcode());

        byte[] imageBytes = employee.getImage();
        if (imageBytes != null && imageBytes.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            image.setImageBitmap(bitmap);
        } else {
            image.setImageResource(R.drawable.avatar);
        }

        checkBox.setChecked(selectedEmployeeCodes.contains(employee.getEmpcode()));
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedEmployeeCodes.add(employee.getEmpcode());
            } else {
                selectedEmployeeCodes.remove(employee.getEmpcode());
            }
        });

        return convertView;
    }



    public Set<String> getSelectedEmployeeCodes() {
        return selectedEmployeeCodes;
    }
}
