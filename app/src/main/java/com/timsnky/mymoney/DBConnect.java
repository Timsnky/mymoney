package com.timsnky.mymoney;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.security.PublicKey;
import java.util.ArrayList;

/**
 * Created by Timsnky on 3/2/2015.
 */
public class DBConnect extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "my_money";
    private static final String TABLE_BANK = "bank";
    private static final String TABLE_CASH = "cash";
    private static final String TABLE_MOBILE = "mobile";
    private static final String TABLE_ONLINE = "online";
    private static final String TRAN_DATE = "date";
    private static final String TRAN_DESCRIPTION = "description";
    private static final String TRAN_CATEGORY = "category";
    private static final String TRAN_AMOUNT = "amount";
    private static final String TRAN_BALANCE = "balance";


    public DBConnect(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createBankTable = "CREATE TABLE " + TABLE_BANK + "("
                + TRAN_DATE + " LONG NOT NULL, "
                + TRAN_DESCRIPTION + " TEXT NOT NULL, "
                + TRAN_CATEGORY + " INTEGER NOT NULL, "
                + TRAN_AMOUNT + " FLOAT NOT NULL, "
                + TRAN_BALANCE + " FLOAT NOT NULL)";
        Log.d("My Money", createBankTable);
        db.execSQL(createBankTable);

        String createCashTable = "CREATE TABLE " + TABLE_CASH + "("
                + TRAN_DATE + " LONG NOT NULL, "
                + TRAN_DESCRIPTION + " TEXT NOT NULL, "
                + TRAN_CATEGORY + " INTEGER NOT NULL, "
                + TRAN_AMOUNT + " FLOAT NOT NULL, "
                + TRAN_BALANCE + " FLOAT NOT NULL)";
        Log.d("My Money", createCashTable);
        db.execSQL(createCashTable);
        String createMobileTable = "CREATE TABLE " + TABLE_MOBILE + "("
                + TRAN_DATE + " LONG NOT NULL, "
                + TRAN_DESCRIPTION + " TEXT NOT NULL, "
                + TRAN_CATEGORY + " INTEGER NOT NULL, "
                + TRAN_AMOUNT + " FLOAT NOT NULL, "
                + TRAN_BALANCE + " FLOAT NOT NULL)";
        Log.d("My Money", createMobileTable);
        db.execSQL(createMobileTable);
        String createOnlineTable = "CREATE TABLE " + TABLE_ONLINE + "("
                + TRAN_DATE + " LONG NOT NULL, "
                + TRAN_DESCRIPTION + " TEXT NOT NULL, "
                + TRAN_CATEGORY + " INTEGER NOT NULL, "
                + TRAN_AMOUNT + " FLOAT NOT NULL, "
                + TRAN_BALANCE + " FLOAT NOT NULL)";
        Log.d("My Money", createOnlineTable);
        db.execSQL(createOnlineTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CASH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BANK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOBILE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ONLINE);
        onCreate(db);
    }

    public void clearTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CASH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BANK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOBILE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ONLINE);
        onCreate(db);
    }

    public String chooseTable(int tranAccount){
        if(tranAccount == 45){
            return TABLE_BANK;
        }else if(tranAccount == 46){
            return TABLE_CASH;
        }else if(tranAccount == 47){
            return TABLE_MOBILE;
        }else{
            return TABLE_ONLINE;
        }
    }

    public float getBalance(float tranAmount, int tranCategory){
        SQLiteDatabase db = this.getReadableDatabase();
        if((tranCategory % 10) == 0){
            tranAmount = -tranAmount;
        }
        String query = "SELECT LAST(" + TRAN_BALANCE + ") FROM " + chooseTable(tranCategory);
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        float amount = 0;
        while (cursor.isAfterLast() == false){
            amount = Float.parseFloat(cursor.getString(0));
            cursor.moveToNext();
        }
        return (amount + tranAmount);
    }

    public void startAccount(Transaction transaction){
        Log.d("My Money", transaction.showTransaction());
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TRAN_DATE, transaction.getTranDate());
        values.put(TRAN_DESCRIPTION, transaction.getTranDescription());
        values.put(TRAN_CATEGORY, transaction.getTranCategory());
        values.put(TRAN_AMOUNT, transaction.getTranAmount());
        values.put(TRAN_BALANCE, 0);

        db.insert(chooseTable(transaction.getTranAccount()), null, values);
        db.close();
        Log.d("My Money", "Added Successfully");
    }

    public void addTransaction(Transaction transaction){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TRAN_DATE, transaction.getTranDate());
        values.put(TRAN_DESCRIPTION, transaction.getTranDescription());
        values.put(TRAN_CATEGORY, transaction.getTranCategory());
        values.put(TRAN_AMOUNT, transaction.getTranAmount());
        values.put(TRAN_BALANCE, getBalance(transaction.getTranAmount(), transaction.getTranCategory()));

        db.insert(chooseTable(transaction.getTranAccount()), null, values);
        db.close();
        Log.d("My Money", "Added Successfully");
    }

    public ArrayList<Transaction> getCategoryTransactions(int tranCategory, long startDate, long stopDate){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_BANK + " WHERE ("
                + TRAN_DATE + " BETWEEN " + startDate + " AND " + stopDate + ") AND "
                + TRAN_CATEGORY + " IN (" + tranCategory + "," + (tranCategory + 1) + ")";
        Log.d("My Money", query);

        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false){
            Transaction transaction = new Transaction();
            transaction.setTranDate(Long.parseLong(cursor.getString(0)));
            transaction.setTranDescription(cursor.getString(1));
            transaction.setTranCategory(Integer.parseInt(cursor.getString(2)));
            transaction.setTranAmount(Float.parseFloat(cursor.getString(3)));
            transactions.add(transaction);
            cursor.moveToNext();
        }

        query = "SELECT * FROM " + TABLE_CASH + " WHERE ("
                + TRAN_DATE + " BETWEEN " + startDate + " AND " + stopDate + ") AND "
                + TRAN_CATEGORY + " IN (" + tranCategory + "," + (tranCategory + 1) + ")";
        Log.d("My Money", query);

        cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false){
            Transaction transaction = new Transaction();
            transaction.setTranDate(Long.parseLong(cursor.getString(0)));
            transaction.setTranDescription(cursor.getString(1));
            transaction.setTranCategory(Integer.parseInt(cursor.getString(2)));
            transaction.setTranAmount(Float.parseFloat(cursor.getString(3)));
            transactions.add(transaction);
            cursor.moveToNext();
        }

        query = "SELECT * FROM " + TABLE_MOBILE + " WHERE ("
                + TRAN_DATE + " BETWEEN " + startDate + " AND " + stopDate + ") AND "
                + TRAN_CATEGORY + " IN (" + tranCategory + "," + (tranCategory + 1) + ")";
        Log.d("My Money", query);

        cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false){
            Transaction transaction = new Transaction();
            transaction.setTranDate(Long.parseLong(cursor.getString(0)));
            transaction.setTranDescription(cursor.getString(1));
            transaction.setTranCategory(Integer.parseInt(cursor.getString(2)));
            transaction.setTranAmount(Float.parseFloat(cursor.getString(3)));
            transactions.add(transaction);
            cursor.moveToNext();
        }

        query = "SELECT * FROM " + TABLE_ONLINE + " WHERE ("
                + TRAN_DATE + " BETWEEN " + startDate + " AND " + stopDate + ") AND "
                + TRAN_CATEGORY + " IN (" + tranCategory + "," + (tranCategory + 1) + ")";
        Log.d("My Money", query);

        cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false){
            Transaction transaction = new Transaction();
            transaction.setTranDate(Long.parseLong(cursor.getString(0)));
            transaction.setTranDescription(cursor.getString(1));
            transaction.setTranCategory(Integer.parseInt(cursor.getString(3)));
            transaction.setTranAmount(Float.parseFloat(cursor.getString(4)));
            transactions.add(transaction);
            cursor.moveToNext();
        }
        return transactions;
    }

    public ArrayList<Transaction> getAccountTransactions(int tranAccount, long startDate, long stopDate){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + chooseTable(tranAccount) + " WHERE "
                + TRAN_DATE + " BETWEEN " + startDate + " and " + stopDate + "";
        Log.d("My Money", query);

        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        Cursor cursor = db.rawQuery(query, null);
        Log.d("My Money", cursor.getCount()+ "");
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false){
            Transaction transaction = new Transaction();
            transaction.setTranDate(Long.parseLong(cursor.getString(0)));
            transaction.setTranDescription(cursor.getString(1));
            transaction.setTranCategory(Integer.parseInt(cursor.getString(2)));
            transaction.setTranAmount(Float.parseFloat(cursor.getString(3)));
            transactions.add(transaction);
            cursor.moveToNext();
        }
        Log.d("My Money", "Acc Retrieved Successfully");
        return transactions;
    }
}
