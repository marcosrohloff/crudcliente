package br.com.services;

import br.com.dto.CustomerDTO;
import br.com.entities.Customer;
import br.com.exceptions.DatabaseException;
import br.com.exceptions.ResourceNotFoundException;
import br.com.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;


@Service
public class CustomerService {

    @Autowired
    private CustomerRepository repository;

    @Transactional(readOnly = true)
    public Page<CustomerDTO> findAllPaged(PageRequest pageRequest) {
        Page<Customer> list = repository.findAll(pageRequest);
        return list.map(x -> new CustomerDTO(x));
    }

    @Transactional(readOnly = true)
    public CustomerDTO findById(Long id) {
        Optional<Customer> obj = repository.findById(id);
        Customer entity = obj.orElseThrow(() -> new ResourceNotFoundException("Error: Entity not found ;("));
        return new CustomerDTO(entity);
    }

    @Transactional
    public CustomerDTO insert(CustomerDTO dto) {
        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setCpf(dto.getCpf());
        customer.setIncome(dto.getIncome());
        customer.setBirthDate(dto.getBirthDate());
        customer.setChildren(dto.getChildren());

        customer = repository.save(customer);
        return new CustomerDTO(customer);

    }

    @Transactional
    public CustomerDTO update(Long id, CustomerDTO dto) {
        try {
            Customer customer = repository.getOne(id);
            customer.setName(dto.getName());
            customer.setCpf(dto.getCpf());
            customer.setIncome(dto.getIncome());
            customer.setBirthDate(dto.getBirthDate());
            customer.setChildren(dto.getChildren());

            customer = repository.save(customer);
            return new CustomerDTO(customer);

         } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("ID " + id + " not found :(");
        }
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("ID " + id + " not foud :(");
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity Violation");
        }
    }
}
