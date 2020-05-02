package br.edu.utfpr.cp.emater.midmipsystem.service;

import br.edu.utfpr.cp.emater.midmipsystem.exception.AnyPersistenceException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityAlreadyExistsException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityInUseException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.SupervisorNotAllowedInCity;
import java.util.List;

public interface ICRUDService<T> {

    public List<T> readAll();
    public void create(T entity) throws SupervisorNotAllowedInCity, EntityAlreadyExistsException, AnyPersistenceException, EntityNotFoundException;
    public T readById(Long anId) throws EntityNotFoundException;
    public void update(T entity) throws SupervisorNotAllowedInCity, EntityAlreadyExistsException, EntityNotFoundException, AnyPersistenceException;
    public void delete(Long anId) throws EntityNotFoundException, EntityInUseException, AnyPersistenceException;

}
