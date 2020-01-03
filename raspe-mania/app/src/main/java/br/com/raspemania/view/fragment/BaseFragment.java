package br.com.raspemania.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import br.com.raspemania.R;
import br.com.raspemania.viewmodel.BaseViewModel;

public class BaseFragment extends Fragment {

    protected void observeError(BaseViewModel viewModel){
        viewModel.error.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                hideProgressDialog();
            }
        });
    }

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showProgressDialog();
    }

    @Override
    public void onResume() {
        super.onResume();
        showProgressDialog();
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }
}
