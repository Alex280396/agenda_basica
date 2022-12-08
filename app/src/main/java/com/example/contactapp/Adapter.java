package com.example.contactapp;

import static com.example.contactapp.BaseDatos.TABLA;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    Context context;
    int registro;
    ArrayList<Model>modelArrayList;
    SQLiteDatabase sqLiteDatabase;
    //generate constructor

    public Adapter(Context context, int registro, ArrayList<Model> modelArrayList, SQLiteDatabase sqLiteDatabase) {
        this.context = context;
        this.registro = registro;
        this.modelArrayList = modelArrayList;
        this.sqLiteDatabase = sqLiteDatabase;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.registro_contacto,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Model model=modelArrayList.get(position);
        byte[]image=model.getImagen();
        Bitmap bitmap= BitmapFactory.decodeByteArray(image,0,image.length);
        holder.imagenp.setImageBitmap(bitmap);
        holder.txtnombre.setText(model.getNombre());

        //flow menu
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(context,holder.menu);
                popupMenu.inflate(R.menu.flow_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.op_editar:
                                ///////
                                //edit operation
                                Bundle bundle=new Bundle();
                                bundle.putInt("id",model.getId());
                                bundle.putByteArray("imagen",model.getImagen());
                                bundle.putString("nombre",model.getNombre());
                                bundle.putString("telefono",model.getTelefono());
                                Intent intent=new Intent(context,MainActivity.class);
                                intent.putExtra("contacto",bundle);
                                context.startActivity(intent);
                                break;
                            case R.id.op_eliminar:
                                ///delete operation
                                BaseDatos baseDatos =new BaseDatos(context);
                                sqLiteDatabase= baseDatos.getReadableDatabase();
                                long elimregistro=sqLiteDatabase.delete(TABLA,"id="+model.getId(),null);
                                if (elimregistro!=-1){
                                    Toast.makeText(context, "Contacto eliminado exitosamente", Toast.LENGTH_LONG).show();
                                    //remove position after deleted
                                    modelArrayList.remove(position);
                                    // actualizar info
                                    notifyDataSetChanged();
                                }
                                break;
                            default:
                                return false;
                        }
                        return false;
                    }
                });

                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenp;
        TextView txtnombre;
        ImageButton menu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenp =itemView.findViewById(R.id.viewavatar);
            txtnombre =itemView.findViewById(R.id.txt_name);
            menu =itemView.findViewById(R.id.flowmenu);
        }
    }
}