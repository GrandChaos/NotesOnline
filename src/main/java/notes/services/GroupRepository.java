package notes.services;

import notes.domain.Group;
import notes.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends CrudRepository<Group, Long> {

    Group getOne(long id);
    List<Group> findByUser(User user);

}