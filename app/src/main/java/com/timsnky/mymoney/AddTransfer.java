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

/**
 * Created by Timsnky on 3/1/2015.
 */
public class AddTransfer extends Fragment {
    private EditText dateEditText, amountEditText, descriptionEditText;
    private Button saveTransactionButton;
    private RadioButton radioButton1, radioButton2;
    private Spinner fromAccountSpinner, toAccountSpinner;
    private ArrayAdapter<String> toAccountAdapter, fromAccountAdapter;
    private TextView currencyTextView;
    private DatePickerDialog transactionDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private String tranDescription;
    private long tranDate;
    private int fromTranAccount, toTranAccount, tranCategory;
    private float tranAmount;
    private int categoryInt;
    private RadioGroup categoryGroup;
    private String[] accountNames = {"Bank", "Cash", "Mobile", "Online"};
    private Transaction transaction1, transaction2;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.addtransfer, container, false);
        categoryInt = getArguments().getInt("Category");
        setupAddTransferView(rootView);
        setDateTimeField();
        return rootView;
    }

    public void setupAddTransferView(View rootView){
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

        fromAccountSpinner = (Spinner) rootView.findViewById(R.id.fromAccountSpinner);
        fromAccountAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, accountNames);
        fromAccountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromAccountSpinner.setAdapter(fromAccountAdapter);
        fromAccountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fromTranAccount = 45 + position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                fromTranAccount = 45;
            }
        });

        toAccountSpinner = (Spinner) rootView.findViewById(R.id.toAccountSpinner);
        toAccountAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, accountNames);
        toAccountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toAccountSpinner.setAdapter(toAccountAdapter);
        toAccountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                toTranAccount = 45 + position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                toTranAccount = 45;
            }
        });

        currencyTextView = (TextView) rootView.findViewById(R.id.currencyTextView);
        currencyTextView.setText("Ksh");

        amountEditText = (EditText) rootView.findViewById(R.id.amountEditText);
        descriptionEditText = (EditText) rootView.findViewById(R.id.descriptionEditText);

        saveTransactionButton = (Button) rootView.findViewById(R.id.saveTransferButton);
        saveTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tranAmount = Float.parseFloat(amountEditText.getText().toString());
                tranDescription = descriptionEditText.getText().toString();
                tranCategory = 40;
                transaction1 = new Transaction(tranCategory, tranDate, tranAmount,tranDescription,fromTranAccount);
                Log.d("My Money",transaction1.showTransaction());
                //Upload then add second Transaction*/
                tranCategory = 41;
                transaction2 = new Transaction(tranCategory, tranDate, tranAmount,tranDescription,toTranAccount);
                Log.d("My Money",transaction2.showTransaction());
                Transaction[] params = {transaction1, transaction2};
                new AddTransferThread().execute(params);
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

    private class AddTransferThread extends AsyncTask<Transaction, Void, Void> {

        @Override
        protected Void doInBackground(Transaction... params) {
            DBConnect connect = new DBConnect(getActivity());
            connect.addTransaction(params[0]);
            connect.addTransaction(params[1]);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Adding Transfer...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
