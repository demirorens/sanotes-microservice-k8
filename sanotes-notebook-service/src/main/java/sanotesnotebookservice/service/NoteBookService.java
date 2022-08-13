package sanotesnotebookservice.service;


import sanotesnotebookservice.model.NoteBookModel;
import sanotesnotebookservice.payload.ApiResponse;
import sanotesnotebookservice.payload.ByIdRequest;

import java.util.List;
import java.util.UUID;

public interface NoteBookService {

    NoteBookModel saveNoteBook(NoteBookModel noteBookModel);

    NoteBookModel updateNoteBook(NoteBookModel noteBookModel, String userId);

    ApiResponse deleteNoteBook(ByIdRequest byIdRequest, String userId);

    NoteBookModel getNoteBook(UUID id, String userId);

    List<NoteBookModel> getUserNoteBooks(String userId);
}
