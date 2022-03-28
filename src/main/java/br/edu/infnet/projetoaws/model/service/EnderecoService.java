package br.edu.infnet.projetoaws.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.infnet.projetoaws.clients.IEnderecoClient;
import br.edu.infnet.projetoaws.model.domain.Endereco;

@Service
public class EnderecoService {
	
	@Autowired
	private IEnderecoClient enderecoClient;

	public Endereco buscarCep(String cep) {
		
		return enderecoClient.buscarCep(cep);
	}
}
