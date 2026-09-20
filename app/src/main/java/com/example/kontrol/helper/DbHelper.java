package com.example.kontrol.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.ContentValues;
import android.util.Log;

import com.example.kontrol.database.DBFormaPagamento;
import com.example.kontrol.database.DBUsuario;
import com.example.kontrol.database.DBUsuarioLogado;
import com.example.kontrol.database.DBVersao;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "KONTROL";
    private static final int DATABASE_VERSION = DBVersao.VERSAO;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("SQLITE", "onCreate BbHelper");

        criarTabelaSeNaoExistir(db, DBUsuario.DB_USUARIO, DBUsuario.SCRIPT_CREATE_USUARIO, null);
        criarTabelaSeNaoExistir(db, DBUsuarioLogado.DB_USUARIO_LOGADO, DBUsuarioLogado.SCRIPT_CREATE_USUARIO_LOGADO, null);
        criarTabelaSeNaoExistir(db, DBFormaPagamento.DB_FORMA_PAGAMENTO, DBFormaPagamento.SCRIPT_CREATE_FORMA_PAGAMENTO, DBFormaPagamento.SCRIPT_INSERT_FORMA_PAGAMENTO );

        Log.d("SQLITE", "CRIANDO TABELA");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            criarTabelaSeNaoExistir(db, DBUsuario.DB_USUARIO, DBUsuario.SCRIPT_CREATE_USUARIO, null);
            criarTabelaSeNaoExistir(db, DBUsuarioLogado.DB_USUARIO_LOGADO, DBUsuarioLogado.SCRIPT_CREATE_USUARIO_LOGADO, null);
            criarTabelaSeNaoExistir(db, DBFormaPagamento.DB_FORMA_PAGAMENTO, DBFormaPagamento.SCRIPT_CREATE_FORMA_PAGAMENTO, DBFormaPagamento.SCRIPT_INSERT_FORMA_PAGAMENTO );
        }

    }

//--------------------------------------------------------------------------------------------------

    //CRIAR TABELAS SE NAO EXISTIR
    private void criarTabelaSeNaoExistir(SQLiteDatabase db, String nomeTabela, String scriptCreate, String scriptInsert) {
        if (!tabelaExiste(db, nomeTabela)) {
            db.execSQL(scriptCreate);
            if (scriptInsert != null)
                db.execSQL(scriptInsert);
        }
    }

    private boolean tabelaExiste(SQLiteDatabase db, String nomeTabela) {
        Cursor cursor = db.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name=?",
                new String[]{nomeTabela}
        );

        boolean existe = cursor.moveToFirst();
        cursor.close();

        return existe;
    }

    //CRIAR COLUNAS PARA TABELAS EXISTENTES
    private void adicionarColunaSeNaoExistir(SQLiteDatabase db, String tabela, String coluna, String sql) {
        if (!colunaExiste(db, tabela, coluna)) {
            db.execSQL(sql);
        }
    }

    private boolean colunaExiste(SQLiteDatabase db, String tabela, String coluna) {
        Cursor cursor = db.rawQuery(
                "PRAGMA table_info(" + tabela + ")",
                null
        );

        while (cursor.moveToNext()) {
            String nomeColuna = cursor.getString(cursor.getColumnIndexOrThrow("name"));

            if (nomeColuna.equalsIgnoreCase(coluna)) {
                cursor.close();
                return true;
            }
        }

        cursor.close();
        return false;
    }
}