package sanotesnoteservice.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import sanotesnoteservice.model.NoteVersionModel;

import java.util.List;
import java.util.UUID;


public interface NoteVersionRepository extends CrudRepository<NoteVersionModel, UUID> {
    @Query("select n from NoteVersionModel n where n.noteContainerId = ?1")
    List<NoteVersionModel> findByNoteContainerIdEquals(UUID noteContainerId);

}
