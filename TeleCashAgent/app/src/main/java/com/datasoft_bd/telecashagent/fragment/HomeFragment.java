package com.datasoft_bd.telecashagent.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.datasoft.mfs.commons.model.Transactions;
import com.datasoft_bd.telecashagent.R;
import com.datasoft_bd.telecashagent.activity.FundTransferActivity;
import com.datasoft_bd.telecashagent.service.ApiClient;
import com.datasoft_bd.telecashagent.service.WebConnectionApi;
import com.datasoft_bd.telecashagent.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.fund_transferBTN)
    LinearLayout btnInstantPay;
    private ProgressDialog progressDialog;
    private WebConnectionApi webConnectionApi;
    private Integer userId = 270;


    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_home_fragment, container, false);
        progressDialog = new ProgressDialog(getActivity());
        webConnectionApi = ApiClient.getClient(WebConnectionApi.BASE_API_URL).create(WebConnectionApi.class);
        ButterKnife.bind(this, view);
        btnInstantPay.setOnClickListener(this);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fund_transferBTN:
                getCurrentBalance(userId);
                break;
        }
    }

    private void getTestCall() {
        Transactions transactions = new Transactions();
        transactions.setFromAccount("019161740061");
        transactions.setToAccount("01916170061");

        Utils.showProgressDialog(progressDialog, true);
        final Call<String> getResponseData = webConnectionApi.sendRequest(transactions);
        getResponseData.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("{}", response.body().toString());
                Utils.dismissProgressDialog(progressDialog, false);
                Intent intent = new Intent(getActivity(), FundTransferActivity.class);
                intent.putExtra("currentBalance", String.valueOf(response.body()));
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Utils.dismissProgressDialog(progressDialog, false);
                Utils.showToast(getActivity(), "Please Check Your Internet Connection", true);
            }
        });
    }

    public void getCurrentBalance(Integer userId) {
        Utils.showProgressDialog(progressDialog, true);
        Log.e("Send data", String.valueOf(userId));
        final Call<Double> getResponseData = webConnectionApi.getCurrentBalance(userId);
        getResponseData.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                Utils.dismissProgressDialog(progressDialog, false);
                Intent intent = new Intent(getActivity(), FundTransferActivity.class);
                intent.putExtra("currentBalance", response.body());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_out,R.anim.slide_in);
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {
                Utils.dismissProgressDialog(progressDialog, false);
                Utils.showToast(getActivity(), "Please Check Your Internet Connection", true);
            }
        });
    }
}
