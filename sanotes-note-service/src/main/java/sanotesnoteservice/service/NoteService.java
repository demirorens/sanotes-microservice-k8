package sanotesnoteservice.service;

import sanotesnoteservice.model.NoteContainerModel;
import sanotesnoteservice.model.NoteVersionModel;
import sanotesnoteservice.payload.ApiResponse;
import sanotesnoteservice.payload.ByIdRequest;

import java.util.List;
import java.util.UUID;

public interface NoteService {
    NoteContainerModel saveNote(NoteContainerModel note, String userId);

    NoteContainerModel updateNote(NoteContainerModel note, String userId);

    NoteContainerModel getNote(UUID id, String userId);

    List<NoteVersionModel> getNoteVersions(UUID id, String userId);

    ApiResponse deleteNote(ByIdRequest byIdRequest, String userId);

    List<NoteContainerModel> getNotesByNoteBookId(UUID id, String userId);

    List<NoteContainerModel> getNotesByTagId(UUID id, String userId);
}
