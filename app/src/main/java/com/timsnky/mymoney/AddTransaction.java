package com.timsnky.mymoney;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.PriorityQueue;

/**
 * Created by Timsnky on 3/1/2015.
 */
public class AddTransaction extends Fragment {

    private EditText dateEditText, amountEditText, descriptionEditText;
    private Button saveTransactionButton;
    private RadioButton radioButton1, radioButton2;
    private Spinner accountSpinner;
    private ArrayAdapter<String> accountAdapter;
    private TextView currencyTextView;
    private DatePickerDialog transactionDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private String tranDescription;
    private int tranAccount, tranCategory;
    private long tranDate;
    private float tranAmount;
    private int categoryInt;
    private RadioGroup categoryGroup;
    private String[] accountNames = {"Bank", "Cash", "Mobile", "Online"};
    private Transaction transaction;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.addtransaction, container, false);
        categoryInt = getArguments().getInt("Category");
        setupAddTransactionView(rootView);
        setDateTimeField();
        return rootView;
    }

    public void setupAddTransactionView(View rootView){
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Calendar date = Calendar.getInstance();

        dateEditText = (EditText) rootView.findViewById(R.id.dateEditText);
        dateEditText.setInputType(InputType.TYPE_NULL);
        dateEditText.setText(dateFormatter.format(date.getTime()));
        tranDate = date.getTime().getTime();
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transactionDatePickerDialog.show();
            }
        });

        radioButton1 = (RadioButton) rootView.findViewById(R.id.categoryRadioButton1);
        radioButton1.setChecked(true);
        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tranCategory = (categoryInt * 10);
            }
        });
        tranCategory = (categoryInt * 10);
        radioButton2 = (RadioButton) rootView.findViewById(R.id.categoryRadioButton2);
        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tranCategory = (categoryInt * 10) + 1;
            }
        });
        if(categoryInt == 1){
            radioButton1.setText("Expenses");
            radioButton2.setText("Income");
        }else if(categoryInt == 2){
            radioButton1.setText("Debtors");
            radioButton2.setText("Creditors");
        }else{
            radioButton1.setText("Investment");
            radioButton2.setVisibility(View.INVISIBLE);
            radioButton2.setEnabled(false);
        }
        categoryGroup = (RadioGroup) rootView.findViewById(R.id.categoryRadioGroup);

        accountSpinner = (Spinner) rootView.findViewById(R.id.accountSpinner);
        accountAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, accountNames);
        accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountSpinner.setAdapter(accountAdapter);
        accountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tranAccount = 45 + position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tranAccount = 45;
            }
        });

        currencyTextView = (TextView) rootView.findViewById(R.id.currencyTextView);
        currencyTextView.setText("Ksh");

        amountEditText = (EditText) rootView.findViewById(R.id.amountEditText);
        descriptionEditText = (EditText) rootView.findViewById(R.id.descriptionEditText);

        saveTransactionButton = (Button) rootView.findViewById(R.id.saveTransactionButton);
        saveTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tranAmount = Float.parseFloat(amountEditText.getText().toString());
                tranDescription = descriptionEditText.getText().toString();
                transaction = new Transaction(tranCategory,tranDate,tranAmount,tranDescription,tranAccount);
                Transaction[] params = {transaction};
                new AddTransactionThread().execute(params);
                Log.d("My Money", transaction.showTransaction());
            }
        });
    }

    private void setDateTimeField(){
        final Calendar newCalendar = Calendar.getInstance();

        transactionDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tranDate = newDate.getTime().getTime();
                dateEditText.setText(dateFormatter.format(newDate.getTime()));
                Log.d("My Money", "Start" + tranDate);

            }
        }, newCalendar.get(Calendar.YEAR),newCalendar.get(Calendar.MONTH),newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private class AddTransactionThread extends AsyncTask<Transaction, Void, Void>{

        @Override
        protected Void doInBackground(Transaction... params) {
            DBConnect connect = new DBConnect(getActivity());
            connect.addTransaction(params[0]);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Adding Transaction...");
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
