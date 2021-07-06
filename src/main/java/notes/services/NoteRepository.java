package notes.services;

import notes.domain.Note;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends CrudRepository<Note, Long> {
    Note getOne(long id);
    Note findByName(String name);
}
