package com.example.kontrol.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.kontrol.model.Usuario;

import java.util.ArrayList;

public class DBUsuario extends SQL {

    public static String  DB_USUARIO = "USUARIO",
            FLD_ID = "ID",
            FLD_USER = "USER",
            FLD_NOME = "NOME",
            FLD_EMAIL = "EMAIL",
            FLD_DATANASC = "DATANASC",
            FLD_SENHA = "SENHA";

    public static String SCRIPT_CREATE_USUARIO = "CREATE TABLE IF NOT EXISTS " + DB_USUARIO + "(" +
            FLD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            FLD_USER + " varchar(50) NOT NULL, " +
            FLD_NOME + " varchar(50) NOT NULL, " +
            FLD_EMAIL + " varchar(50) NOT NULL, " +
            FLD_DATANASC + " date NOT NULL, " +
            FLD_SENHA + " varchar(20) NOT NULL " +
            ");";

    //public String SCRIPT_CREATE_COLUNAX_USUARIO = "ALTER TABLE " +  DB_USUARIO + " ADD COLUMN " + FLD_COLUNA + " [tipo] varchar(x)";

    public DBUsuario(Context context) {
        super(context);
    }

    public boolean atualiza(Usuario usuario){
        ContentValues cv = setContentValues(usuario);

        try {
            String [] args = {usuario.getId().toString()};

            update(DB_USUARIO, cv,FLD_ID + "=?", args);
            Log.i("INFO","Sucesso ao atualizar usuario");
        }catch (Exception e){
            Log.e("INFO","Erro ao atualizar usuario" + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean inserir(Usuario usuario) {
        ContentValues cv = setContentValues(usuario);
        try {
            inserir(DB_USUARIO, null, cv);
            Log.i("INFO","Sucesso ao cadastrar usuario");
        }catch (Exception e){
            Log.e("INFO","Erro ao cadastrar usuario\n" + e.getMessage());
            return false;
        }
        return true;
    }

    public ContentValues setContentValues(Usuario usuario) {
        ContentValues cv = new ContentValues();
        cv.put(FLD_USER, usuario.getUser());
        cv.put(FLD_NOME, usuario.getNome());
        cv.put(FLD_EMAIL, usuario.getEmail());
        cv.put(FLD_DATANASC, usuario.getDataNasc());
        cv.put(FLD_SENHA, usuario.getSenha());
        return cv;
    }

    public Usuario recuperaUsuario(String where, String[] whereArgs){
        try {
            Cursor c = select(DB_USUARIO, null, where, whereArgs, null, null, null);
            if (c != null && c.moveToFirst()) {
                return new Usuario(
                        c.getLong(c.getColumnIndexOrThrow(FLD_ID)),
                        c.getString(c.getColumnIndex(FLD_USER)),
                        c.getString(c.getColumnIndexOrThrow(FLD_NOME)),
                        c.getString(c.getColumnIndexOrThrow(FLD_EMAIL)),
                        c.getString(c.getColumnIndexOrThrow(FLD_DATANASC))
                );
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
    public ArrayList<Usuario> recuperaListaUsuarios() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        try {
            Cursor c = select(DB_USUARIO, null, null, null, null, null, null);

            while (c.moveToNext()) {
                Usuario usuario = new Usuario(
                        c.getLong(c.getColumnIndexOrThrow(FLD_ID)),
                        c.getString(c.getColumnIndexOrThrow(FLD_USER)),
                        c.getString(c.getColumnIndexOrThrow(FLD_NOME)),
                        c.getString(c.getColumnIndexOrThrow(FLD_EMAIL)),
                        c.getString(c.getColumnIndexOrThrow(FLD_DATANASC))
                );
                usuarios.add(usuario);
            }
        } catch (Exception e) {
            return null;
        }
        return !usuarios.isEmpty() ? usuarios : null;
    }

}
