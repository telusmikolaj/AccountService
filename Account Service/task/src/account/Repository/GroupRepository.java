package account.Repository;

import account.Model.Group;
import org.springframework.data.repository.CrudRepository;

public interface GroupRepository extends CrudRepository<Group, Long> {

    Group findGroupByName(String name);
    boolean existsGroupByName(String name);
}
