package sanotesnotebookservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import sanotesnotebookservice.model.NoteBookModel;

import java.util.List;
import java.util.UUID;

public interface NoteBookRepository extends CrudRepository<NoteBookModel, UUID> {
    @Query("select n from NoteBookModel n where n.name = ?1")
    List<NoteBookModel> findByName(String name);

    List<NoteBookModel> findByCreatedByEquals(UUID createdBy);


}
