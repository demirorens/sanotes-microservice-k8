package sanotestagservice.repository;

import org.springframework.data.repository.CrudRepository;
import sanotestagservice.model.TagModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TagRepository extends CrudRepository<TagModel, UUID> {
    List<TagModel> findByCreatedByEquals(UUID createdBy);
    Optional<TagModel> findByName(String name);


}
