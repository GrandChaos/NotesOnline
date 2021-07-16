package notes.services;

import notes.domain.Note;
import notes.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends CrudRepository<Note, Long> {

    Note getOne(long id);
}