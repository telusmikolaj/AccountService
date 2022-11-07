package account.Repository;

import account.Model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    @Autowired
    private final GroupRepository groupRepository;

    @Autowired
    public DataLoader(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
        createRoles();
    }

    private void createRoles() {

        try {
            groupRepository.save(new Group("ROLE_ADMINISTRATOR"));
            groupRepository.save(new Group("ROLE_ACCOUNTANT"));
            groupRepository.save(new Group("ROLE_USER"));

        } catch (Exception e) {

        }

    }
}
