package com.example.kontrol.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.kontrol.helper.DbHelper;

public class SQL {
    private final SQLiteDatabase escreve;
    private final SQLiteDatabase le;

    public SQL(Context context) {
        DbHelper db = new DbHelper(context);
        escreve = db.getWritableDatabase();
        le = db.getReadableDatabase();
    }

    public void inserir(String table, String nullColumnHack, ContentValues values) {
        escreve.insert(table, nullColumnHack, values);
    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgsString ) {
        return escreve.update(table, values, whereClause, whereArgsString);
    }

    public Cursor select(String table, String[] columns, String selection, String[] selectionArg, String groupBy, String having, String orderBy) {
        return le.query(table, columns,selection,selectionArg, groupBy, having, orderBy);
    }
}
