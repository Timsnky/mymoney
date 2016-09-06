package com.timsnky.mymoney;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Timsnky on 3/7/2015.
 */
public class Settings extends Fragment {
    private TableRow addAccountTableRow, clearDatabaseTableRow;
    private String[] accountNames = {"Bank", "Cash", "Mobile", "Online"};
    private ProgressDialog progressDialog;
    private int account, category;
    private long tranDate;
    private float amount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings, container, false);
        setupSettingsView(rootView);
        return rootView;
    }

    private void setupSettingsView(View rootView){
        addAccountTableRow = (TableRow) rootView.findViewById(R.id.addAccountTableRow);
        addAccountTableRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAccount();
            }
        });

        clearDatabaseTableRow = (TableRow) rootView.findViewById(R.id.clearDatabaseTableRow);
        clearDatabaseTableRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBConnect connect = new DBConnect(getActivity());
                connect.clearTable();
            }
        });
    }

    public void addAccount(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.addaccount);
        dialog.setTitle("Add an Account:");

        final Spinner accountSpinner = (Spinner) dialog.findViewById(R.id.addAccountSpinner);
        final ArrayAdapter accountAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, accountNames);
        accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountSpinner.setAdapter(accountAdapter);
        accountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                account = 45 + position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                account = 45;
            }
        });


        final EditText amountEditText = (EditText) dialog.findViewById(R.id.addAmountEditText);

        Button button = (Button) dialog.findViewById(R.id.addAccountButton);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.d("My Money", account + "");
                Calendar date = Calendar.getInstance();
                tranDate = date.getTime().getTime();
                amount = Float.parseFloat(amountEditText.getText().toString());
                Transaction transaction = new Transaction(41, tranDate, amount, "Start", account);
                Log.d("My Money", transaction.showTransaction());
                Transaction params[] = {transaction};
                new SetupAccountThread().execute(params);
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private class SetupAccountThread extends AsyncTask<Transaction, Void, Void> {

        @Override
        protected Void doInBackground(Transaction... params) {
            DBConnect connect = new DBConnect(getActivity());
            connect.startAccount(params[0]);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Setting Up Account...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
    }

}
