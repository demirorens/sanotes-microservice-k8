package sanotesnoteservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import sanotesnoteservice.model.NoteContainerModel;

import java.util.List;
import java.util.UUID;


public interface NoteContainerRepository extends CrudRepository<NoteContainerModel, UUID> {
    List<NoteContainerModel> findByNoteBookIdEquals(UUID noteBookId);

    @Query(value = "select n.* from notes n join note_tags nt on n.id = nt.id where nt.tag = ?1",
            nativeQuery = true)
    List<NoteContainerModel> getByTagId(UUID tag);


}
