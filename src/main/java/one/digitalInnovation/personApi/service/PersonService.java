package one.digitalInnovation.personApi.service;

import lombok.AllArgsConstructor;
import one.digitalInnovation.personApi.dto.request.PersonDTO;
import one.digitalInnovation.personApi.dto.response.MessageResponseDTO;
import one.digitalInnovation.personApi.entity.Person;
import one.digitalInnovation.personApi.exception.PersonNotFoundException;
import one.digitalInnovation.personApi.mapper.PersonMapper;
import one.digitalInnovation.personApi.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service // Indica que essa classe irá colocar as formas de funcionamento/transações
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PersonService {

    private PersonRepository personRepository;

    private final PersonMapper personMapper = PersonMapper.INSTANCE;

    public MessageResponseDTO createPerson(PersonDTO personDTO){
        Person personSave = personMapper.toModel(personDTO);
        Person savedPerson = personRepository.save(personSave);
        return createMessageResponse(savedPerson.getId(), "Create person with ID ");
    }

    public List<PersonDTO> listAll(){
        List<Person> allPeople = personRepository.findAll();
        return allPeople.stream()
                .map(personMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PersonDTO findById(Long id) throws PersonNotFoundException {
        Person person = verifyIfExists(id);

        return personMapper.toDTO(person);
    }

    public void delete(Long id) throws PersonNotFoundException {
        verifyIfExists(id);

        personRepository.deleteById(id);
    }

    public MessageResponseDTO updateById(Long id, PersonDTO personDTO) throws PersonNotFoundException {
        verifyIfExists(id);

        Person personUpdate = personMapper.toModel(personDTO);

        Person updatedPerson = personRepository.save(personUpdate);
        return createMessageResponse(updatedPerson.getId(), "Updated person with ID ");
    }

    private Person verifyIfExists(Long id) throws PersonNotFoundException {
        return personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));
    }

    private MessageResponseDTO createMessageResponse(Long id, String message) {
        return MessageResponseDTO
                .builder()
                .message(message + id)
                .build();
    }
}
