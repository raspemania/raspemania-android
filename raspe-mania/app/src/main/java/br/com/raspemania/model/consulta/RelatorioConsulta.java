package br.com.raspemania.model.consulta;

import java.io.Serializable;
import java.util.Date;

import br.com.raspemania.model.BaseModel;
import br.com.raspemania.model.entidade.Cliente;
import br.com.raspemania.model.entidade.Colaborador;
import br.com.raspemania.model.entidade.Rota;

public class RelatorioConsulta extends BaseModel implements Serializable {

    public Cliente cliente;
    public Colaborador colaborador;
    public Rota rota;
    public Date dataInicio;
    public Date dataFim;
}
