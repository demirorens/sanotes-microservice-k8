package sanotesnoteservice.service.impl;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Service;
import sanotesnoteservice.client.ApiGatewayClient;
import sanotesnoteservice.exeption.ResourceNotFoundException;
import sanotesnoteservice.exeption.UnauthorizedException;
import sanotesnoteservice.model.NoteContainerModel;
import sanotesnoteservice.model.NoteModel;
import sanotesnoteservice.model.NoteVersionModel;
import sanotesnoteservice.payload.ApiResponse;
import sanotesnoteservice.payload.BooleanResponse;
import sanotesnoteservice.payload.ByIdRequest;
import sanotesnoteservice.repository.NoteContainerRepository;
import sanotesnoteservice.repository.NoteRepository;
import sanotesnoteservice.repository.NoteVersionRepository;
import sanotesnoteservice.service.NoteService;

import java.util.List;
import java.util.UUID;


@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    private final NoteContainerRepository noteContainerRepository;

    private final NoteVersionRepository noteVersionRepository;

    private final ModelMapper modelMapper;

    private final ApiGatewayClient apiGatewayClient;


    private static final String USER_DONT_HAVE_PERMISSION = "User don't have permission for this request";

    public NoteServiceImpl(NoteRepository noteRepository, NoteContainerRepository noteContainerRepository, NoteVersionRepository noteVersionRepository, ModelMapper modelMapper, ApiGatewayClient apiGatewayClient) {
        this.noteRepository = noteRepository;
        this.noteContainerRepository = noteContainerRepository;
        this.noteVersionRepository = noteVersionRepository;
        this.modelMapper = modelMapper;
        TypeMap<NoteContainerModel, NoteVersionModel> propertyMapper =
                this.modelMapper.createTypeMap(NoteContainerModel.class, NoteVersionModel.class);
        propertyMapper.addMapping(NoteContainerModel::getId, NoteVersionModel::setNoteContainerId);
        this.apiGatewayClient = apiGatewayClient;
    }


    public NoteContainerModel saveNote(NoteContainerModel noteContainerModel, String userId) {
        NoteModel noteModel = NoteModel.builder()
                .topic(noteContainerModel.getTopic())
                .text(noteContainerModel.getText())
                .build();
        BooleanResponse booleanResponse = apiGatewayClient.getIsUserOwner(noteContainerModel.getNoteBookId().toString());
        if (booleanResponse.getResult() == null)
            throw new ResourceNotFoundException(booleanResponse.getFallbackMessage());
        if (!booleanResponse.getResult())
            throw new UnauthorizedException(USER_DONT_HAVE_PERMISSION);
        for (UUID tagId : noteContainerModel.tags) {
            try {
                apiGatewayClient.getTag(tagId.toString());
            } catch (ResourceNotFoundException exception) {
                throw new ResourceNotFoundException("Tag", "by id", tagId.toString());
            }
        }
        noteModel = noteRepository.save(noteModel);
        noteContainerModel.setNoteId(noteModel.getId());
        noteContainerModel = noteContainerRepository.save(noteContainerModel);
        noteContainerModel.setTopic(noteModel.getTopic());
        noteContainerModel.setText(noteModel.getText());
        return noteContainerModel;
    }

    public NoteContainerModel updateNote(NoteContainerModel noteContainerModel, String userId) {
        UUID noteId = noteContainerModel.getId();
        NoteContainerModel oldNoteContainerModel = noteContainerRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "by id", noteId.toString()));
        BooleanResponse booleanResponse = apiGatewayClient.getIsUserOwner(noteContainerModel.getNoteBookId().toString());
        if (booleanResponse.getResult() == null)
            throw new ResourceNotFoundException(booleanResponse.getFallbackMessage());
        if (!booleanResponse.getResult())
            throw new UnauthorizedException(USER_DONT_HAVE_PERMISSION);
        for (UUID tagId : noteContainerModel.tags) {
            try {
                apiGatewayClient.getTag(tagId.toString());
            } catch (ResourceNotFoundException exception) {
                throw new ResourceNotFoundException("Tag", "by id", tagId.toString());
            }
        }
        NoteVersionModel notesVersionModel = modelMapper.map(oldNoteContainerModel, NoteVersionModel.class);
        notesVersionModel.setNoteContainerId(oldNoteContainerModel.getId());
        NoteModel noteModel = NoteModel.builder()
                .topic(noteContainerModel.getTopic())
                .text(noteContainerModel.getText())
                .build();
        noteModel = noteRepository.save(noteModel);
        noteContainerModel.setNoteId(noteModel.getId());
        noteContainerModel = noteContainerRepository.save(noteContainerModel);
        noteContainerModel.setTopic(noteModel.getTopic());
        noteContainerModel.setText(noteModel.getText());
        //  Save old version begin
        noteVersionRepository.save(notesVersionModel);
        //Save old version end
        return noteContainerModel;
    }

    public NoteContainerModel getNote(UUID id, String userId) {
        NoteContainerModel noteContainerModel = noteContainerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "by id", id.toString()));
        NoteModel noteModel = noteRepository.findById(noteContainerModel.getNoteId())
                .orElseThrow(() -> new ResourceNotFoundException("Note", "by id", id.toString()));
        BooleanResponse booleanResponse = apiGatewayClient.getIsUserOwner(noteContainerModel.getNoteBookId().toString());
        if (booleanResponse.getResult() == null)
            throw new ResourceNotFoundException(booleanResponse.getFallbackMessage());
        if (!booleanResponse.getResult())
            throw new UnauthorizedException(USER_DONT_HAVE_PERMISSION);
        noteContainerModel.setNoteId(noteModel.getId());
        noteContainerModel.setTopic(noteModel.getTopic());
        noteContainerModel.setText(noteModel.getText());
        return noteContainerModel;
    }

    public List<NoteVersionModel> getNoteVersions(UUID id, String userId) {
        List<NoteVersionModel> noteVersionModels = noteVersionRepository.findByNoteContainerIdEquals(id);
        if (!noteVersionModels.isEmpty()) {
            BooleanResponse booleanResponse = apiGatewayClient.getIsUserOwner(noteVersionModels.get(0).getNoteBookId().toString());
            if (booleanResponse.getResult() == null)
                throw new ResourceNotFoundException(booleanResponse.getFallbackMessage());
            if (!booleanResponse.getResult())
                throw new UnauthorizedException(USER_DONT_HAVE_PERMISSION);
        }
        return noteVersionModels;
    }


    public ApiResponse deleteNote(ByIdRequest byIdRequest, String userId) {
        NoteContainerModel noteContainerModel = noteContainerRepository.findById(byIdRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Note", "by id", byIdRequest.getId().toString()));
        BooleanResponse booleanResponse = apiGatewayClient.getIsUserOwner(noteContainerModel.getNoteBookId().toString());
        if (booleanResponse.getResult() == null)
            throw new ResourceNotFoundException(booleanResponse.getFallbackMessage());
        if (!booleanResponse.getResult())
            throw new UnauthorizedException(USER_DONT_HAVE_PERMISSION);
        noteContainerRepository.delete(noteContainerModel);
        NoteModel noteModel = NoteModel.builder().id(noteContainerModel.getNoteId()).build();
        noteRepository.delete(noteModel);
        return new ApiResponse(Boolean.TRUE, "You successfully delete note ");
    }

    public List<NoteContainerModel> getNotesByNoteBookId(UUID id, String userId) {
        List<NoteContainerModel> noteContainerModel = noteContainerRepository.findByNoteBookIdEquals(id);
        if (!noteContainerModel.isEmpty()) {
            BooleanResponse booleanResponse = apiGatewayClient.getIsUserOwner(noteContainerModel.get(0).getNoteBookId().toString());
            if (booleanResponse.getResult() == null)
                throw new ResourceNotFoundException(booleanResponse.getFallbackMessage());
            if (!booleanResponse.getResult())
                throw new UnauthorizedException(USER_DONT_HAVE_PERMISSION);
        }
        return noteContainerModel;
    }

    public List<NoteContainerModel> getNotesByTagId(UUID id, String userId) {
        List<NoteContainerModel> noteContainerModel = noteContainerRepository.getByTagId(id);
        if (!noteContainerModel.isEmpty()) {
            BooleanResponse booleanResponse = apiGatewayClient.getIsUserOwner(noteContainerModel.get(0).getNoteBookId().toString());
            if (booleanResponse.getResult() == null)
                throw new ResourceNotFoundException(booleanResponse.getFallbackMessage());
            if (!booleanResponse.getResult())
                throw new UnauthorizedException(USER_DONT_HAVE_PERMISSION);
        }
        return noteContainerModel;
    }
}
