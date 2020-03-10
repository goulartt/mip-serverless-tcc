package br.edu.utfpr.cp.emater.midmipsystem.service.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.edu.utfpr.cp.emater.midmipsystem.entity.base.City;
import br.edu.utfpr.cp.emater.midmipsystem.entity.base.Region;
import br.edu.utfpr.cp.emater.midmipsystem.entity.security.Authority;
import br.edu.utfpr.cp.emater.midmipsystem.entity.security.MIPUser;
import br.edu.utfpr.cp.emater.midmipsystem.exception.AnyPersistenceException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityAlreadyExistsException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityInUseException;
import br.edu.utfpr.cp.emater.midmipsystem.exception.EntityNotFoundException;
import br.edu.utfpr.cp.emater.midmipsystem.repository.security.MIPUserRepository;
import br.edu.utfpr.cp.emater.midmipsystem.service.ICRUDService;
import br.edu.utfpr.cp.emater.midmipsystem.service.base.CityService;
import br.edu.utfpr.cp.emater.midmipsystem.service.base.RegionService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MIPUserService implements ICRUDService<MIPUser> {

    private final MIPUserRepository mipUserRepository;
    private final CityService cityService;
    private final RegionService regionService;
    private final AuthorityService authorityService;

    public List<MIPUser> readAll() {
        return List.copyOf(mipUserRepository.findAll());
    }

    public MIPUser readById(Long anId) throws EntityNotFoundException {
        return mipUserRepository.findById(anId).orElseThrow(EntityNotFoundException::new);
    }

    public void create(MIPUser aMIPUser) throws EntityAlreadyExistsException, AnyPersistenceException {

        if (mipUserRepository.findAll().stream().anyMatch(currentUser -> currentUser.equals(aMIPUser))) {
            throw new EntityAlreadyExistsException();
        }

        try {
            aMIPUser.setPassword(new BCryptPasswordEncoder().encode(aMIPUser.getPassword()));
            
            mipUserRepository.save(aMIPUser);

        } catch (Exception e) {
            throw new AnyPersistenceException();
        }
    }

    public void update(MIPUser aMIPUser) throws EntityAlreadyExistsException, EntityNotFoundException, AnyPersistenceException {

        var existentEntity = mipUserRepository.findById(aMIPUser.getId()).orElseThrow(EntityNotFoundException::new);
        
        var allUsersWithoutExistentUser = new ArrayList<MIPUser>(mipUserRepository.findAll());
        allUsersWithoutExistentUser.remove(existentEntity);

        if (allUsersWithoutExistentUser.stream().anyMatch(currentUser -> currentUser.equals(aMIPUser))) {
            throw new EntityAlreadyExistsException();
        }
                
        try {
            existentEntity.setAccountNonExpired(aMIPUser.isAccountNonExpired());
            existentEntity.setAccountNonLocked(aMIPUser.isAccountNonLocked());
            existentEntity.setAuthorities(aMIPUser.getAuthorities());
            existentEntity.setCity(cityService.readById(aMIPUser.getCityId()));
            existentEntity.setCredentialsNonExpired(aMIPUser.isCredentialsNonExpired());
            existentEntity.setEmail(aMIPUser.getEmail());
            existentEntity.setEnabled(aMIPUser.isEnabled());
            existentEntity.setFullName(aMIPUser.getFullName());
            existentEntity.setPassword(aMIPUser.getPassword());
            existentEntity.setRegion(regionService.readById(aMIPUser.getRegionId()));
            existentEntity.setUsername(aMIPUser.getUsername());
            
            existentEntity.setPassword(new BCryptPasswordEncoder().encode(existentEntity.getPassword()));
            
            mipUserRepository.saveAndFlush(existentEntity);

        } catch (Exception e) {
            throw new AnyPersistenceException();
        }
    }

    public void delete(Long anId) throws EntityNotFoundException, EntityInUseException, AnyPersistenceException {
        
        var existentEntity = mipUserRepository.findById(anId).orElseThrow(EntityNotFoundException::new);
                
        try {
            mipUserRepository.delete(existentEntity);
            
        } catch (DataIntegrityViolationException cve) {
            throw new EntityInUseException();
            
        } catch (Exception e) {
            throw new AnyPersistenceException();
        }
    }

    public List<Region> readAllRegions() {
        return regionService.readAll();
    }

    public List<City> readAllCitiesInRegion(Long anRegionId) throws EntityNotFoundException {
        return new ArrayList<>(regionService.readById(anRegionId).getCities());
    }

    public City readCityById(Long selectedCityId) throws EntityNotFoundException {
        return cityService.readById(selectedCityId);
    }

    public Region readRegionById(Long selectedRegionId) throws EntityNotFoundException {
        return regionService.readById(selectedRegionId);
    }

    public Authority readAuthorityByName(String aName) {
        return authorityService.readByName(aName);
    }

    public List<Authority> readAllUserTypes() {
        return authorityService.readAll();
    }

    public Optional<Authority> readAuthorityById(Long id) {
        return authorityService.readById (id);
    }
}
