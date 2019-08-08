package com.datasoft_bd.telecashagent.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.datasoft_bd.telecashagent.R;
import com.datasoft_bd.telecashagent.adapter.NothingSelectedSpinnerAdapter;
import com.datasoft_bd.telecashagent.model.UserDetail;
import com.datasoft_bd.telecashagent.service.ApiClient;
import com.datasoft_bd.telecashagent.service.WebConnectionApi;
import com.datasoft_bd.telecashagent.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FundTransferActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.current_balance)
    TextView currentBalanceTV;
    @BindView(R.id.tv_agent_account)
    EditText accountNoET;
    @BindView(R.id.tv_amount)
    TextView amountTV;
    @BindView(R.id.distributorList)
    Spinner distributorListSpinner;
    @BindView(R.id.btn_submit)
    Button submitBTN;
    private Double balance;
    private EditText etPinNumber;
    private ProgressDialog progressDialog;
    private WebConnectionApi webConnectionApi;
    Integer userId = 270;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        //removing title and set full screen activity
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_fund_transfer);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        progressDialog = new ProgressDialog(this);
        webConnectionApi = ApiClient.getClient(WebConnectionApi.BASE_API_URL).create(WebConnectionApi.class);
        balance = getIntent().getDoubleExtra("currentBalance", 0);
        ButterKnife.bind(this);

        currentBalanceTV.setText(String.format("Balance : %s", balance));
        addDistributorListInSpinner();
        submitBTN.setOnClickListener(this);
    }


    private void addDistributorListInSpinner() {
        Utils.showProgressDialog(progressDialog, true);
        Log.e("Send data", String.valueOf(userId));
        final Call<List<UserDetail>> getResponseData = webConnectionApi.getDistributorList(userId);
        getResponseData.enqueue(new Callback<List<UserDetail>>() {
            @Override
            public void onResponse(Call<List<UserDetail>> call, Response<List<UserDetail>> response) {
                Utils.dismissProgressDialog(progressDialog, false);
                final List<UserDetail> distributorList = response.body();

                ArrayAdapter<UserDetail> adapter = new ArrayAdapter<UserDetail>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, distributorList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                distributorListSpinner.setAdapter(adapter);
                distributorListSpinner.setPrompt("----Select-----");
                distributorListSpinner.setAdapter(new NothingSelectedSpinnerAdapter(adapter, R.layout.spinner_row_nothing_selected, getApplicationContext()));
                distributorListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position != 0) {
                            accountNoET.setText(distributorList.get(position - 1).getAccountNumber());
                            accountNoET.setEnabled(false);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onFailure(Call<List<UserDetail>> call, Throwable t) {
                Utils.dismissProgressDialog(progressDialog, false);
                Utils.showToast(getApplicationContext(), "Please Check Your Internet Connection", true);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (Math.round(balance) > Integer.parseInt(amountTV.getText().toString())) {
            Utils.showToast(getApplicationContext(), "Insufficient Balance", true);
        }

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View promptView = layoutInflater.inflate(R.layout.pin_enter_dialogue, null);
        builder.setView(promptView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        LinearLayout layout = (LinearLayout) promptView.findViewById(R.id.lay);
        Button cancelBtn = (Button) promptView.findViewById(R.id.cancel_btn);
        Button okBtn = (Button) promptView.findViewById(R.id.ok_btn);

        etPinNumber = (EditText) promptView.findViewById(R.id.pin_no);
        etPinNumber.setFocusable(true);
        etPinNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        etPinNumber.setTransformationMethod(PasswordTransformationMethod.getInstance());
        // Invalid pin message
        TextView tvErrorMsg = new TextView(this);
        tvErrorMsg.setTextColor(Color.RED);
        tvErrorMsg.setTextSize(17);
        layout.addView(tvErrorMsg);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etPinNumber.getWindowToken(), 0);
                if (etPinNumber.getText().toString().trim().equals("")) {
                    etPinNumber.setError("Required field.");
                } else {
                    Map<String, String> dataMap = new HashMap<>();
                    dataMap.put("pinNumber", String.valueOf(etPinNumber.getText().toString().trim()));
                    Utils.showToast(getApplicationContext(), "My submitted pin no" + etPinNumber.getText().toString().trim(), false);
                }
            }
        });

    }
}
