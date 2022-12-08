package com.example.contactapp;

import static com.example.contactapp.BaseDatos.TABLA;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MostrarDatos extends AppCompatActivity {

    BaseDatos baseDatos;
    SQLiteDatabase sqLiteDatabase;
    RecyclerView recyclerView;
    Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_contactos);

        baseDatos =new BaseDatos(this);
        findId();
        displayData();
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
    }

    private void displayData() {
        sqLiteDatabase= baseDatos.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select *from "+ TABLA +"",null);
        ArrayList<Model>models=new ArrayList<>();
        while (cursor.moveToNext()){
            int id=cursor.getInt(0);
            byte[]imagen=cursor.getBlob(1);
            String nombre=cursor.getString(2);
            String telefono=cursor.getString(3);
            models.add(new Model(id,imagen,nombre,telefono));
        }
        cursor.close();
        adapter =new Adapter(this,R.layout.registro_contacto,models,sqLiteDatabase);
        recyclerView.setAdapter(adapter);
    }

    private void findId() {
        recyclerView=findViewById(R.id.rv);
    }
}