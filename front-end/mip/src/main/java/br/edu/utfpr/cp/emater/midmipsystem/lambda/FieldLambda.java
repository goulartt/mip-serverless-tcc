package br.edu.utfpr.cp.emater.midmipsystem.lambda;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.utfpr.cp.emater.midmipsystem.dto.base.FieldDTO;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Field;
import br.edu.utfpr.cp.emater.midmipsystem.entity.security.MIPUserPrincipal;
import br.edu.utfpr.cp.emater.midmipsystem.exception.AnyPersistenceException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityAlreadyExistsException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityInUseException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.SupervisorNotAllowedInCity;
import br.edu.utfpr.cp.emater.midmipsystem.service.ICRUDService;
import kong.unirest.GenericType;
import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FieldLambda implements ICRUDService<Field> {

	@Value("${mip.gateway.url}")
	private String ENDPOINT_GATEWAY;
	
	public List<Field> readAll() {

		var response = Unirest.get(ENDPOINT_GATEWAY+"/field/all").asObject((new GenericType<List<Field>>() {
		})).getBody();

		return response;
	}

	@Override
	public Field readById(Long anId) throws EntityNotFoundException {
		try {
			var response = Unirest.get(ENDPOINT_GATEWAY+"/field")
					.queryString("id", anId)
					.asJson();
			
			if (response.getStatus() != 200)
				throw new EntityNotFoundException();

			return new ObjectMapper().readValue(response.getBody().getObject().toString(), Field.class);

		} catch (Exception e) {
			throw new EntityNotFoundException();
		}

	}

	public void create(FieldDTO newField) throws SupervisorNotAllowedInCity, EntityAlreadyExistsException,
			AnyPersistenceException, EntityNotFoundException {

		try {
			var response = Unirest.post(ENDPOINT_GATEWAY+"/field").header("Content-Type", "application/json")
					.body(new ObjectMapper().writeValueAsString(newField)).asJson();
			switch (response.getStatus()) {
			case (201):
				break;
			case (409):
				throw new EntityAlreadyExistsException();
			case (405):
				throw new SupervisorNotAllowedInCity();
			default:
				throw new AnyPersistenceException();
			}
		} catch (JsonProcessingException e) {
			throw new AnyPersistenceException();
		}
	}

	public void update(FieldDTO newField) throws SupervisorNotAllowedInCity, EntityAlreadyExistsException,
			EntityNotFoundException, AnyPersistenceException {

		try {

			var response = Unirest.put(ENDPOINT_GATEWAY+"/field").header("Content-Type", "application/json")
					.body(new ObjectMapper().writeValueAsString(newField)).asJson();

			switch (response.getStatus()) {
			case (201):
				break;
			case (405):
				throw new AccessDeniedException("Usuário não autorizado para atualizar essa unidade!");
			case (404):
				throw new EntityNotFoundException();
			default:
				throw new AnyPersistenceException();
			}

		} catch (Exception e) {
			throw new AnyPersistenceException();
		}
	}

	public void delete(Long anId) throws EntityNotFoundException, EntityInUseException, AnyPersistenceException {

		var loggedUser = ((MIPUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getUser();

		var response = Unirest
				.delete(ENDPOINT_GATEWAY+"/field")
				.queryString("fieldId", anId)
				.queryString("userId", loggedUser.getId())
				.asJson();

		switch (response.getStatus()) {
		case (204):
			break;
		case (405):
			throw new AccessDeniedException("Usuário não autorizado para essa exclusão!");
		case (404):
			throw new EntityNotFoundException();
		default:
			throw new AnyPersistenceException();
		}

	}


	@Override
	public void create(Field aField) throws SupervisorNotAllowedInCity, EntityAlreadyExistsException,
			AnyPersistenceException, EntityNotFoundException {

	}

	@Override
	public void update(Field entity) throws SupervisorNotAllowedInCity, EntityAlreadyExistsException,
			EntityNotFoundException, AnyPersistenceException {
		// TODO Auto-generated method stub
	}

}
