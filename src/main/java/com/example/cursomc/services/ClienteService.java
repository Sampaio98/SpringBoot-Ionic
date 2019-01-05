package com.example.cursomc.services;

import com.example.cursomc.domain.Cidade;
import com.example.cursomc.domain.Cliente;
import com.example.cursomc.domain.Cliente;
import com.example.cursomc.domain.Endereco;
import com.example.cursomc.domain.enums.TipoCliente;
import com.example.cursomc.dto.ClienteDTO;
import com.example.cursomc.dto.ClienteNewDTO;
import com.example.cursomc.repositories.ClienteRepository;
import com.example.cursomc.repositories.EnderecoRepository;
import com.example.cursomc.services.exceptions.DataIntegrityException;
import com.example.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repo;

    @Autowired
    private EnderecoRepository enderecoRepository;

    public Cliente find(Integer id) {
        Optional<Cliente> cliente = repo.findById(id);
        return cliente.orElseThrow(() ->
                new ObjectNotFoundException("Objecto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
    }

    public Cliente update(Cliente cliente) {
        Cliente novoCliente = find(cliente.getId());
        updateData(novoCliente, cliente);
        return repo.save(novoCliente);
    }

    private void updateData(Cliente novoCliente, Cliente cliente) {
        novoCliente.setNome(cliente.getNome());
        novoCliente.setEmail(cliente.getEmail());
    }

    public void delete(Integer id){
        find(id);
        try{
            repo.deleteById(id);
        }
        catch (DataIntegrityViolationException e){
            throw new DataIntegrityException("Não é possível excluir porque há entidades relacionadas");
        }
    }

    public List<Cliente> findAll() {
        return repo.findAll();
    }

    public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        return repo.findAll(pageRequest);
    }

    public Cliente fromDTO(ClienteDTO clienteDTO){
        return new Cliente(clienteDTO.getNome(), clienteDTO.getEmail(), null, null);
    }

    public Cliente fromDTO(ClienteNewDTO clienteNewDTO){
        Cliente cli = new Cliente(clienteNewDTO.getNome(), clienteNewDTO.getEmail(), clienteNewDTO.getCpfOuCnpj(), TipoCliente.toEnum(clienteNewDTO.getTipo()));
        Cidade cid = new Cidade(clienteNewDTO.getCidadeId(), null, null);
        Endereco end = new Endereco(clienteNewDTO.getLogradouro(), clienteNewDTO.getNumero(), clienteNewDTO.getComplemento(), clienteNewDTO.getBairro(), clienteNewDTO.getCep(), cli, cid);
        cli.getEnderecos().add(end);
        cli.getTelefones().add(clienteNewDTO.getTelefone1());

        if(clienteNewDTO.getTelefone2() != null){
            cli.getTelefones().add(clienteNewDTO.getTelefone2());
        }

        if(clienteNewDTO.getTelefone3() != null){
            cli.getTelefones().add(clienteNewDTO.getTelefone3());
        }

        return cli;
    }

    @Transactional
    public Cliente insert(Cliente cliente) {
        cliente.setId(null);
        cliente = repo.save(cliente);
        enderecoRepository.saveAll(cliente.getEnderecos());
        return cliente;
    }
}
