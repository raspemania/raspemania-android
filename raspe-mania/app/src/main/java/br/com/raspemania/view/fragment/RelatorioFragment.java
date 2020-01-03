package br.com.raspemania.view.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import br.com.raspemania.R;
import br.com.raspemania.helper.DateHelper;
import br.com.raspemania.helper.SpinnerHelper;
import br.com.raspemania.model.consulta.RelatorioConsulta;
import br.com.raspemania.model.entidade.Cliente;
import br.com.raspemania.model.entidade.Colaborador;
import br.com.raspemania.model.entidade.Rota;
import br.com.raspemania.view.activity.RelatorioActivity;
import br.com.raspemania.viewmodel.ClienteViewModel;
import br.com.raspemania.viewmodel.ColaboradorViewModel;
import br.com.raspemania.viewmodel.RotaViewModel;

public class RelatorioFragment extends BaseFragment {

    public static String TAG = "RelatorioFragment";

    private Context context = getContext();
    private ClienteViewModel mViewModelCliente;
    private RotaViewModel mViewModelRota;
    private ColaboradorViewModel mViewModelColaborador;
    private RelatorioConsulta mFiltros;

    private AppCompatButton mBuscar;
    private Spinner spinnerCliente;
    private Spinner spinnerRota;
    private Spinner spinnerColaborador;
    private TextInputEditText dataFim;
    private TextInputEditText dataInicio;

    private AppCompatImageButton calendar_inicio;
    private AppCompatImageButton calendar_fim;

    private DatePickerDialog picker;

    public static LeituraFragment newInstance() {
        return new LeituraFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_relatorio, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        hideProgressDialog();

        context = getContext();

        //mViewModel = ViewModelProviders.of(this).get(LeituraViewModel.class);
        mViewModelCliente = ViewModelProviders.of(this).get(ClienteViewModel.class);
        mViewModelRota = ViewModelProviders.of(this).get(RotaViewModel.class);
        mViewModelColaborador = ViewModelProviders.of(this).get(ColaboradorViewModel.class);

        doBindings();

        mViewModelCliente.getAll();
        mViewModelRota.getAll();
        mViewModelColaborador.getAll();

        mBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mFiltros = filtros();
                    Intent intent = new Intent(context, RelatorioActivity.class);
                    intent.putExtra(TAG, mFiltros);
                    context.startActivity(intent);
                } catch (ParseException e) {
                    Toast.makeText(context, "Ocorreu um erro inesperado!", Toast.LENGTH_LONG).show();
                }
            }
        });


        //dataFim.setInputType(InputType.TYPE_NULL);
        calendar_fim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dataFim.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        //dataIncio.setInputType(InputType.TYPE_NULL);
        calendar_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dataInicio.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

    }

    private void doBindings() {
        super.onStart();
        super.observeError(mViewModelCliente);
        super.observeError(mViewModelRota);
        super.observeError(mViewModelColaborador);
        observeSucessSpinnerCliente();
        observeSucessSpinnerColaborador();
        observeSucessSpinnerRota();
    }

    private RelatorioConsulta filtros() throws ParseException {

        RelatorioConsulta filtros = new RelatorioConsulta();

        if(spinnerCliente.getSelectedItemPosition() != 0){
            filtros.cliente = (Cliente) spinnerCliente.getSelectedItem();
        }
        if(spinnerColaborador.getSelectedItemPosition() != 0){
            filtros.colaborador = (Colaborador) spinnerColaborador.getSelectedItem();
        }
        if(spinnerRota.getSelectedItemPosition() != 0){
            filtros.rota = (Rota) spinnerRota.getSelectedItem();
        }
        if(!TextUtils.isEmpty(dataFim.getText())){
            filtros.dataFim = DateHelper.addData(dataFim.getText().toString(), 1, context);
        }
        if(!TextUtils.isEmpty(dataInicio.getText())){
            filtros.dataInicio = DateHelper.stringToDate(dataInicio.getText().toString(), context);
        }
        return filtros;
    }

    private void observeSucessSpinnerCliente() {

        mViewModelCliente.mList.observe(this, new Observer<List<Cliente>>() {
            @Override
            public void onChanged(List<Cliente> resultList) {
                ArrayAdapter<Cliente> adapter = new ArrayAdapter<>(context, R.layout.item_spinner_default, SpinnerHelper.spinnerCliente( resultList, getContext()));
                spinnerCliente.setAdapter(adapter);
            }
        });
    }

    private void observeSucessSpinnerRota() {

        mViewModelRota.mList.observe(this, new Observer<List<Rota>>() {
            @Override
            public void onChanged(List<Rota> resultList) {
                ArrayAdapter<Rota> adapter = new ArrayAdapter<>(context, R.layout.item_spinner_default, SpinnerHelper.spinnerRota( resultList, getContext()));
                spinnerRota.setAdapter(adapter);
            }
        });
    }

    private void observeSucessSpinnerColaborador() {

        mViewModelColaborador.mList.observe(this, new Observer<List<Colaborador>>() {
            @Override
            public void onChanged(List<Colaborador> resultList) {
                ArrayAdapter<Colaborador> adapter = new ArrayAdapter<>(context, R.layout.item_spinner_default, SpinnerHelper.spinnerColaborador( resultList, getContext()));
                spinnerColaborador.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBuscar = view.findViewById(R.id.btnBuscar);
        spinnerCliente = view.findViewById(R.id.spinnerCliente);
        spinnerColaborador = view.findViewById(R.id.spinnerColaborador);
        spinnerRota = view.findViewById(R.id.spinnerRota);
        dataFim = view.findViewById(R.id.dataFim);
        dataInicio = view.findViewById(R.id.dataIncio);
        calendar_fim = view.findViewById(R.id.calendar_fim);
        calendar_inicio = view.findViewById(R.id.calendar_inicio);
    }

    @Override
    public void onResume() {
        super.onResume();
        hideProgressDialog();
    }
}
