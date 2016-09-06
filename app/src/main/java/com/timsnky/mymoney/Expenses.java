package com.timsnky.mymoney;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Timsnky on 2/28/2015.
 */
public class Expenses extends Fragment{

    private EditText startDateEdit, stopDateEdit;
    private DatePickerDialog startDatePickerDialog, stopDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private TextView addTextView;
    private Button getTransactionsButton;
    private TableRow addTableRow;
    private String startDate, stopDate;
    private int classInt,tranCategory;
    private ProgressDialog progressDialog;
    public static ArrayList<Transaction> transactions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.expenses, container, false);
        classInt = getArguments().getInt("ClassCategory");
        setupExpensesView(rootView);
        setDateTimeField();
        return rootView;
    }

    public void setupExpensesView(View rootView){
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        addTableRow = (TableRow) rootView.findViewById(R.id.addTableRow);
        addTableRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(classInt == 4){
                    Log.d("My Money", "Add Account");
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    Fragment addTransferFragment = new AddTransfer();
                    Bundle addTransactionArgs = new Bundle();
                    addTransactionArgs.putInt("Category", classInt);
                    addTransferFragment.setArguments(addTransactionArgs);
                    transaction.replace(R.id.frame_container, addTransferFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();

                }else{
                    Log.d("My Money", "Add");
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    Fragment addTransactionFragment = new AddTransaction();
                    Bundle addTransactionArgs = new Bundle();
                    addTransactionArgs.putInt("Category", classInt);
                    addTransactionFragment.setArguments(addTransactionArgs);
                    transaction.replace(R.id.frame_container, addTransactionFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        addTextView = (TextView) rootView.findViewById(R.id.addTextView);

        if(classInt == 1){
            addTextView.setText("Add Expense or Income");
            tranCategory = 10;
        }else if(classInt == 2){
            addTextView.setText("Add Debtor or Creditor");
            tranCategory = 20;
        }else if(classInt == 3){
            addTextView.setText("Add an Investment");
            tranCategory = 30;
        }else{
            addTextView.setText("Add a Transfer");
            tranCategory = 40;
        }
        Calendar today = Calendar.getInstance();

        startDateEdit = (EditText) rootView.findViewById(R.id.startDateEdit);
        startDateEdit.setText(dateFormatter.format(today.getTime()));
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        startDate = Long.toString(today.getTime().getTime());
        startDateEdit.setInputType(InputType.TYPE_NULL);
        startDateEdit.requestFocus();
        startDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDatePickerDialog.show();
            }
        });

        stopDateEdit = (EditText) rootView.findViewById(R.id.endDateEdit);
        stopDateEdit.setText(dateFormatter.format(today.getTime()));
        today.set(Calendar.HOUR_OF_DAY, 23);
        today.set(Calendar.MINUTE, 59);
        today.set(Calendar.SECOND, 59);
        stopDate = Long.toString(today.getTime().getTime());
        stopDateEdit.setInputType(InputType.TYPE_NULL);
        stopDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDatePickerDialog.show();
            }
        });

        getTransactionsButton = (Button) rootView.findViewById(R.id.getTransactionsButton);
        getTransactionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(tranCategory == 40){
                    int tranAccount = 40 + classInt;
                    String[] params = {Integer.toString(tranAccount), startDate, stopDate};
                    new GetAccountTransactions().execute(params);
                }else{
                    String[] params = {Integer.toString(tranCategory), startDate, stopDate};
                    new GetTransactions().execute(params);
                }
            }
        });
    }

    private void showResults(){
        Log.d("My Money", "Results");
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment resultsFragment = new Results();
        transaction.replace(R.id.frame_container, resultsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setDateTimeField(){
        final Calendar newCalendar = Calendar.getInstance();

        startDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                startDateEdit.setText(dateFormatter.format(newDate.getTime()));
                newDate.set(Calendar.HOUR_OF_DAY, 0);
                newDate.set(Calendar.MINUTE, 0);
                newDate.set(Calendar.SECOND, 0);
                startDate = Long.toString(newDate.getTime().getTime());
                Log.d("My Money", "Start" + startDate);

            }
        }, newCalendar.get(Calendar.YEAR),newCalendar.get(Calendar.MONTH),newCalendar.get(Calendar.DAY_OF_MONTH));

        stopDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                stopDateEdit.setText(dateFormatter.format(newDate.getTime()));
                newDate.set(Calendar.HOUR_OF_DAY, 23);
                newDate.set(Calendar.MINUTE, 59);
                newDate.set(Calendar.SECOND, 59);
                stopDate = Long.toString(newDate.getTime().getTime());
                Log.d("My Money", "Stop " + stopDate);
            }
        }, newCalendar.get(Calendar.YEAR),newCalendar.get(Calendar.MONTH),newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private class GetTransactions extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... params) {
            DBConnect dbConnect = new DBConnect(getActivity());
            transactions = dbConnect.getCategoryTransactions(Integer.parseInt(params[0]), Long.parseLong(params[1]), Long.parseLong(params[2]));
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Fetching Transactions...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            showResults();
        }
    }

    private class GetAccountTransactions extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... params) {
            DBConnect dbConnect = new DBConnect(getActivity());
            transactions = dbConnect.getAccountTransactions(Integer.parseInt(params[0]), Long.parseLong(params[1]), Long.parseLong(params[2]));
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Fetching Transactions...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            showResults();
        }
    }
}
