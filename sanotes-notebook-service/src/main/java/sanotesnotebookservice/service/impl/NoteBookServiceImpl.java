package sanotesnotebookservice.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sanotesnotebookservice.exception.ResourceNotFoundException;
import sanotesnotebookservice.exception.UnauthorizedException;
import sanotesnotebookservice.model.NoteBookModel;
import sanotesnotebookservice.payload.ApiResponse;
import sanotesnotebookservice.payload.ByIdRequest;
import sanotesnotebookservice.repository.NoteBookRepository;
import sanotesnotebookservice.service.NoteBookService;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class NoteBookServiceImpl implements NoteBookService {

    private final NoteBookRepository noteBookRepository;

    private static final String USER_DONT_HAVE_PERMISSION = "User don't have permission for this request";

    public NoteBookModel saveNoteBook(NoteBookModel noteBookModel) {
        return noteBookRepository.save(noteBookModel);
    }

    public NoteBookModel updateNoteBook(NoteBookModel noteBookModel, String userId) {
        NoteBookModel oldNoteBookModel = noteBookRepository.findById(noteBookModel.getId())
                .orElseThrow(() -> new ResourceNotFoundException("NoteBook", "by id", noteBookModel.getId().toString()));
        if (!oldNoteBookModel.getCreatedBy().toString().equals(userId))
            throw new UnauthorizedException(USER_DONT_HAVE_PERMISSION);
        oldNoteBookModel.setName(noteBookModel.getName());
        oldNoteBookModel.setDescription(noteBookModel.getDescription());
        return noteBookRepository.save(oldNoteBookModel);
    }

    public ApiResponse deleteNoteBook(ByIdRequest byIdRequest, String userId) {
        NoteBookModel noteBook = noteBookRepository.findById(byIdRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException("NoteBook", "by id", byIdRequest.getId().toString()));
        if (!noteBook.getCreatedBy().toString().equals(userId))
            throw new UnauthorizedException(USER_DONT_HAVE_PERMISSION);
        noteBookRepository.delete(noteBook);
        return new ApiResponse(Boolean.TRUE, "You successfully delete notebook ");
    }

    public NoteBookModel getNoteBook(UUID id, String userId) {
        NoteBookModel noteBookModel = noteBookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notebook", "by id", id.toString()));
        if (!noteBookModel.getCreatedBy().toString().equals(userId))
            throw new UnauthorizedException(USER_DONT_HAVE_PERMISSION);
        return noteBookModel;
    }

    public List<NoteBookModel> getUserNoteBooks(String userId) {
        List<NoteBookModel> noteBookModels = noteBookRepository.findByCreatedByEquals(UUID.fromString(userId));
        return noteBookModels;
    }

}
