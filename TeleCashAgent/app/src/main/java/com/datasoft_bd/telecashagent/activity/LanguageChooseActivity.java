package com.datasoft_bd.telecashagent.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.datasoft_bd.telecashagent.R;
import com.datasoft_bd.telecashagent.utils.LocaleHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LanguageChooseActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPref;

    @BindView(R.id.txt_language)
    TextView txtChangeLanguage;

    @BindView(R.id.btn_save_language)
    TextView btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        //removing title and set full screen activity
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_language_choose);
        sharedPref = getPreferences(MODE_PRIVATE);
        editor = sharedPref.edit();
        ButterKnife.bind(this);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.language_chn_radioGroup);
        radioGroup.setOnCheckedChangeListener(this);
        updateViews("en");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @OnClick(R.id.btn_save_language)
    public void onButtonClick() {
        startActivity(new Intent(getBaseContext(), LoginActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        editor.putInt("LANGUAGE", radioGroup.getCheckedRadioButtonId());
        editor.commit();
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.english:
                updateViews("en");
                break;
            case R.id.bangla:
                updateViews("bn");
                break;
        }
    }

    private void updateViews(String languageCode) {
        Context context = LocaleHelper.setLocale(this, languageCode);
        txtChangeLanguage.setText(context.getResources().getString(R.string.choose_language));
        btnSave.setText(context.getResources().getString(R.string.txt_save));
    }
}
