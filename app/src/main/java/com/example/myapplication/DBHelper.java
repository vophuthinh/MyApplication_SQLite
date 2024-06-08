package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context) {
        super(context, "mycompany", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE Departments(" +
                "depcode TEXT PRIMARY KEY, " +
                "name TEXT, " +
                "phone TEXT);");
        sqLiteDatabase.execSQL("CREATE TABLE Employees("+
                "empcode primary key,"+
                "name text,"+
                "gender text," +
                "phone text,"+
                "address text,"+
                "image blob,"+
                "depcode text not null constraint depcode references " +
                "Departments(depcode) ON DELETE CASCADE ON UPDATE CASCADE);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Departments");
        db.execSQL("DROP TABLE IF EXISTS Employee");
        onCreate(db);
    }

    // Employee: insert
    public int insertEmployee(Employee emp){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("empcode", emp.getEmpcode());
        content.put("name", emp.getName());
        content.put("gender", emp.getGender());
        content.put("phone", emp.getPhone());
        content.put("address", emp.getAddress());
        content.put("depcode", emp.getDepcode());
        content.put("image", emp.getImage());
        int result = (int)db.insert("Employees",
                null, content);
        db.close();
        return result;
    }

    // Employee: update
    public int updateEmployee(Employee emp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("name", emp.getName());
        content.put("gender", emp.getGender());
        content.put("phone", emp.getPhone());
        content.put("address", emp.getAddress());
        content.put("image", emp.getImage());
        content.put("depcode", emp.getDepcode());
        int result = db.update("Employees", content, "empcode=?", new String[]{emp.getEmpcode()});
        db.close();
        return result;
    }
    // Employee: delete
    public int deleteEmployee(String empcode) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("Employees", "empcode=?", new String[]{empcode});
        db.close();
        return result;
    }
    public ArrayList<Employee> searchEmployee(String empCode, String name, String phone, String address) {
        ArrayList<Employee> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        StringBuilder query = new StringBuilder("SELECT * FROM Employees WHERE 1=1");
        ArrayList<String> args = new ArrayList<>();

        if (!empCode.isEmpty()) {
            query.append(" AND empcode LIKE ?");
            args.add("%" + empCode + "%");
        }
        if (!name.isEmpty()) {
            query.append(" AND name LIKE ?");
            args.add("%" + name + "%");
        }
        if (!phone.isEmpty()) {
            query.append(" AND phone LIKE ?");
            args.add("%" + phone + "%");
        }
        if (!address.isEmpty()) {
            query.append(" AND address LIKE ?");
            args.add("%" + address + "%");
        }

        Cursor cursor = db.rawQuery(query.toString(), args.toArray(new String[0]));
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(new Employee(cursor.getString(0),
                        cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4),
                        cursor.getBlob(5), cursor.getString(6)));
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return list;
    }

    // Employee: select
    public ArrayList<Employee> getAllEmployee(){
        ArrayList<Employee> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Employees",null);
        if(cursor != null)
            cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            list.add(new Employee(cursor.getString(0),
                    cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4),
                    cursor.getBlob(5), cursor.getString(6)));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return list;
    }
    public ArrayList<Employee> searchEmployee(String name){
        ArrayList<Employee> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Employees where name like ?", new String[]{"%"+name+"%"});
        if(cursor != null)
            cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            list.add(new Employee(cursor.getString(0),
                    cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4),
                    cursor.getBlob(5), cursor.getString(6)));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return list;
    }

    // Department: insert, update, delete, select
    public int insertDepartment(Department dep) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("depcode", dep.getCode());
        content.put("name", dep.getName());
        content.put("phone", dep.getPhone());
        int result = (int) db.insert("Departments", null, content);
        db.close();
        return result;
    }

    public int updateDepartment(Department dep) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("depcode", dep.getCode());
        content.put("name", dep.getName());
        content.put("phone", dep.getPhone());
        int result = db.update("Departments", content, "depcode=?", new String[]{dep.getCode()});
        db.close();
        return result;
    }

    public int deleteDepartment(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("Departments", "depcode=?", new String[]{id});
        db.close();
        return result;
    }


    public ArrayList<Department> searchDepartment(String code, String name, String phone) {
        ArrayList<Department> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        StringBuilder query = new StringBuilder("SELECT * FROM Departments WHERE 1=1");
        ArrayList<String> args = new ArrayList<>();

        if (!code.isEmpty()) {
            query.append(" AND depcode LIKE ?");
            args.add("%" + code + "%");
        }
        if (!name.isEmpty()) {
            query.append(" AND name LIKE ?");
            args.add("%" + name + "%");
        }
        if (!phone.isEmpty()) {
            query.append(" AND phone LIKE ?");
            args.add("%" + phone + "%");
        }

        Cursor cursor = db.rawQuery(query.toString(), args.toArray(new String[0]));
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(new Department(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return list;
    }

    public ArrayList<String> getDepartmentNames() {
        ArrayList<String> departmentNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM Departments", null);
        if (cursor.moveToFirst()) {
            do {
                departmentNames.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return departmentNames;
    }
    public ArrayList<Department> getAllDepartment(){
        ArrayList<Department> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Departments",null);
        if(cursor != null)
            cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            list.add(new Department(cursor.getString(0),
                    cursor.getString(1), cursor.getString(2)));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return list;
    }
}
