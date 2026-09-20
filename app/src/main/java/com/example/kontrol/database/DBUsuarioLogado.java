package com.example.kontrol.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.kontrol.model.Usuario;

import java.util.ArrayList;

public class DBUsuarioLogado extends SQL {

    public static String  DB_USUARIO_LOGADO = "USUARIO_LOGADO",
            FLD_ID = "ID",
            FLD_USER = "USER";

    public static String SCRIPT_CREATE_USUARIO_LOGADO = "CREATE TABLE IF NOT EXISTS " + DB_USUARIO_LOGADO + "(" +
            FLD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            FLD_USER + " varchar(50) NOT NULL " +
            ");";

    //public String SCRIPT_CREATE_COLUNAX_USUARIO = "ALTER TABLE " +  DB_USUARIO + " ADD COLUMN " + FLD_COLUNA + " [tipo] varchar(x)";

    public DBUsuarioLogado(Context context) {
        super(context);
    }

    public boolean atualiza(Usuario usuario){
        ContentValues cv = setContentValues(usuario);

        try {
            String [] args = {usuario.getId().toString()};

            update(DB_USUARIO_LOGADO, cv,FLD_ID + "=?", args);
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
            inserir(DB_USUARIO_LOGADO, null, cv);
            Log.i("INFO","Sucesso ao cadastrar usuario");
        }catch (Exception e){
            Log.e("INFO","Erro ao cadastrar usuario\n" + e.getMessage());
            return false;
        }
        return true;
    }

    public ContentValues setContentValues(Usuario usuario) {
        ContentValues cv = new ContentValues();
        cv.put(FLD_ID, usuario.getId());
        cv.put(FLD_USER, usuario.getUser());
        return cv;
    }

    public Usuario recuperaUsuario(String where, String[] whereArgs){
        try {
            Cursor c = select(DB_USUARIO_LOGADO, null, where, whereArgs, null, null, null);
            if (c != null && c.moveToFirst()) {
                return new Usuario(
                        c.getLong(c.getColumnIndexOrThrow(FLD_ID)),
                        c.getString(c.getColumnIndexOrThrow(FLD_USER))
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
            Cursor c = select(DB_USUARIO_LOGADO, null, null, null, null, null, null);

            while (c.moveToNext()) {
                Usuario usuario = new Usuario(
                        c.getLong(c.getColumnIndexOrThrow(FLD_ID)),
                        c.getString(c.getColumnIndexOrThrow(FLD_USER))
                );
                usuarios.add(usuario);
            }
        } catch (Exception e) {
            return null;
        }
        return !usuarios.isEmpty() ? usuarios : null;
    }

}
