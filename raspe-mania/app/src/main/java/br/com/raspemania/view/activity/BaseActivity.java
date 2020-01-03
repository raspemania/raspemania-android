package br.com.raspemania.view.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import br.com.raspemania.R;
import br.com.raspemania.helper.ConstantHelper;
import br.com.raspemania.viewmodel.BaseViewModel;

public class BaseActivity extends AppCompatActivity {

    String[] mStatus = {ConstantHelper.SELECIONE_STR, ConstantHelper.ATIVO_STR, ConstantHelper.INATIVO_STR};
    String[] mPerfil = {ConstantHelper.PERFIL_SELECIONE_STR, ConstantHelper.PERFIL_ADM_STR, ConstantHelper.PERFIL_COLABORADOR_STR};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        if(!hasConnection()) { alertNoConnection(); }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!hasConnection()) { alertNoConnection(); }
    }

    public void closeApplication() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    public void alertNoConnection() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Atenção");
        alertDialog.setMessage("Sem conexão com internet!");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Tentar de novo",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onResume();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Entendi",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        closeApplication();
                    }
                });
        alertDialog.show();
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    protected void observeError(BaseViewModel viewModel) {
        viewModel.error.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Boolean hasConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connectivityManager.getActiveNetworkInfo();

        if (activeInfo != null && activeInfo.isConnected()) {
            if (activeInfo.getType() == ConnectivityManager.TYPE_WIFI)
                return true;
            if(activeInfo.getType() == ConnectivityManager.TYPE_MOBILE)
                return true;
        }
        return false;
    }

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void spinnerStatus(Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), R.layout.item_spinner_default, mStatus);
        spinner.setAdapter(adapter);
    }

    public void spinnerPerfil(Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), R.layout.item_spinner_default, mPerfil);
        spinner.setAdapter(adapter);
    }

    public int getStatusSpinner(String itemSelected) {
        if (itemSelected.equalsIgnoreCase(ConstantHelper.ATIVO_STR)) {
            return ConstantHelper.ATIVO;
        } else {
            return ConstantHelper.INATIVO;
        }
    }

    public int getPerfilSpinner(String itemSelected) {
        if (itemSelected.equalsIgnoreCase(ConstantHelper.PERFIL_ADM_STR)) {
            return ConstantHelper.PERFIL_ADM;
        } else {
            return ConstantHelper.PERFIL_COLABORADOR;
        }
    }

    public int setSpinner(long itemSelected) {

        if (itemSelected == ConstantHelper.ATIVO || itemSelected == ConstantHelper.PERFIL_ADM) {
            return 1;
        } else {
            if (itemSelected == ConstantHelper.INATIVO || itemSelected == ConstantHelper.PERFIL_COLABORADOR) {
                return 2;
            }else{
                return 0;
            }

        }

    }

    public boolean validStatus(Spinner mStatus) {

        if (mStatus == null) {
            return false;
        } else if (mStatus.getSelectedItemPosition() == 0) {
            return false;
        }
        return true;
    }

}
