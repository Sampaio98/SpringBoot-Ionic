package com.example.cursomc.services;

import com.example.cursomc.domain.Cliente;
import com.example.cursomc.repositories.ClienteRepository;
import com.example.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repo;

    public Cliente find(Integer id) {
        Optional<Cliente> cliente = repo.findById(id);
        return cliente.orElseThrow(() ->
                new ObjectNotFoundException("Objecto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
    }
}
