package com.example.kontrol.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.kontrol.model.FormaPagamento;
import java.util.ArrayList;

public class DBFormaPagamento extends SQL {

    public static String  DB_FORMA_PAGAMENTO = "FORMA_PAGAMENTO",
            FLD_ID = "ID",
            FLD_NOME = "NOME";

    public static String SCRIPT_CREATE_FORMA_PAGAMENTO = "CREATE TABLE IF NOT EXISTS " + DB_FORMA_PAGAMENTO + "(" +
            FLD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            FLD_NOME + " varchar(50) NOT NULL  " +
            ");";

    public static String SCRIPT_INSERT_FORMA_PAGAMENTO = "INSERT INTO FORMA_PAGAMENTO (" + FLD_NOME + ") VALUES" +
            "('Dinheiro'), ('Cartão de Crédito'), ('Cartão de Débito'), ('Pix'), ('Transferência Bancária'), ('Cheque') , ('Outros') ";


    //public String SCRIPT_CREATE_COLUNAX_FORMA_PAGAMENTO = "ALTER TABLE " +  DB_FORMA_PAGAMENTO + " ADD COLUMN " + FLD_COLUNA + " [tipo] varchar(x)";

    public DBFormaPagamento(Context context) {
        super(context);
    }


    public boolean atualiza(FormaPagamento formaPagamento){
        ContentValues cv = setContentValues(formaPagamento);

        try {
            String [] args = {formaPagamento.getId().toString()};

            update(DB_FORMA_PAGAMENTO, cv,FLD_ID + "=?", args);
            Log.i("INFO","Sucesso ao atualizar forma de pagamento");
        }catch (Exception e){
            Log.e("INFO","Erro ao atualizar forma de pagamento" + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean inserir(FormaPagamento formaPagamento) {
        ContentValues cv = setContentValues(formaPagamento);
        try {
            inserir(DB_FORMA_PAGAMENTO, null, cv);
            Log.i("INFO","Sucesso ao cadastrar forma de pagamento");
        }catch (Exception e){
            Log.e("INFO","Erro ao cadastrar forma de pagamento\n" + e.getMessage());
            return false;
        }
        return true;
    }

    public ContentValues setContentValues(FormaPagamento formaPagamento) {
        ContentValues cv = new ContentValues();
        cv.put(FLD_NOME, formaPagamento.getNome());
        return cv;
    }

    public FormaPagamento recuperaFormaPagamento(String where, String[] whereArgs){
        try {
            Cursor c = select(DB_FORMA_PAGAMENTO, null, where, whereArgs, null, null, null);
            if (c != null && c.moveToFirst()) {
                return new FormaPagamento(
                        c.getLong(c.getColumnIndexOrThrow(FLD_ID)),
                        c.getString(c.getColumnIndexOrThrow(FLD_NOME))
                );
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
    public ArrayList<FormaPagamento> recuperaListaFormaPagamentos() {
        ArrayList<FormaPagamento> formaPagamentos = new ArrayList<>();
        try {
            Cursor c = select(DB_FORMA_PAGAMENTO, null, null, null, null, null, null);

            while (c.moveToNext()) {
                FormaPagamento formaPagamento = new FormaPagamento(
                        c.getLong(c.getColumnIndexOrThrow(FLD_ID)),
                        c.getString(c.getColumnIndexOrThrow(FLD_NOME))
                );
                formaPagamentos.add(formaPagamento);
            }
        } catch (Exception e) {
            return null;
        }
        return !formaPagamentos.isEmpty() ? formaPagamentos : null;
    }

}
