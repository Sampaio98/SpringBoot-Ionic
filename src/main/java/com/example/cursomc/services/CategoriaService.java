package com.example.cursomc.services;

import java.util.List;
import java.util.Optional;

import com.example.cursomc.services.exceptions.DataIntegrityException;
import com.example.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.cursomc.domain.Categoria;
import com.example.cursomc.repositories.CategoriaRepository;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repo;

    public Categoria find(Integer id) {
        Optional<Categoria> categoria = repo.findById(id);
        return categoria.orElseThrow(() ->
                new ObjectNotFoundException("Objeto não encontrado! id: " + id + ", Tipo: " + Categoria.class.getName()));
    }

    public Categoria insert(Categoria categoria) {
        categoria.setId(null);
        return repo.save(categoria);
    }

    public Categoria update(Categoria categoria) {
        find(categoria.getId());
        return repo.save(categoria);
    }

    public void delete(Integer id){
        find(id);
        try{
            repo.deleteById(id);
        }
        catch (DataIntegrityViolationException e){
            throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos");
        }
    }

    public List<Categoria> findAll() {
        return repo.findAll();
    }

    public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        return repo.findAll(pageRequest);
    }
}
