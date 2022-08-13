package sanotesnoteservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.*;
import sanotesnoteservice.model.NoteContainerModel;
import sanotesnoteservice.model.NoteVersionModel;
import sanotesnoteservice.payload.ApiResponse;
import sanotesnoteservice.payload.ByIdRequest;
import sanotesnoteservice.payload.NoteRequest;
import sanotesnoteservice.payload.NoteResponse;
import sanotesnoteservice.security.CurrentUser;
import sanotesnoteservice.service.NoteService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class NoteController {

    private final NoteService noteService;

    private final ModelMapper modelMapper;

    @GetMapping("/cbtest/{num}")
    @PreAuthorize("hasAuthority('sanotes_user')")
    @Operation(hidden = true)
    public ResponseEntity<String> getcbTest(
            @PathVariable(value = "num") int num,
            @Parameter(hidden = true)
            @CurrentUser OAuth2AuthenticatedPrincipal userPrincipal) throws InterruptedException {
        if (num == 1) {
            Thread.sleep(500);
        }
        return new ResponseEntity<>("cbTest", HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('sanotes_user')")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<NoteResponse> addNote(
            @Valid @RequestBody NoteRequest noteRequest,
            @Parameter(hidden = true)
            @CurrentUser OAuth2AuthenticatedPrincipal userPrincipal) {
        NoteContainerModel noteContainerModel = modelMapper.map(noteRequest, NoteContainerModel.class);
        noteContainerModel = noteService.saveNote(noteContainerModel, userPrincipal.getName());
        NoteResponse noteResponse = modelMapper.map(noteContainerModel, NoteResponse.class);
        return new ResponseEntity<>(noteResponse, HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('sanotes_user')")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<NoteResponse> updateNote(
            @Valid @RequestBody NoteRequest noteRequest,
            @Parameter(hidden = true)
            @CurrentUser OAuth2AuthenticatedPrincipal userPrincipal) {
        NoteContainerModel noteContainerModel = modelMapper.map(noteRequest, NoteContainerModel.class);
        noteContainerModel = noteService.updateNote(noteContainerModel, userPrincipal.getName());
        NoteResponse noteResponse = modelMapper.map(noteContainerModel, NoteResponse.class);
        return new ResponseEntity<>(noteResponse, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('sanotes_user')")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<NoteResponse> getNote(
            @RequestParam(value = "id") UUID id,
            @Parameter(hidden = true)
            @CurrentUser OAuth2AuthenticatedPrincipal userPrincipal) {
        NoteContainerModel noteContainerModel = noteService.getNote(id, userPrincipal.getName());
        NoteResponse noteResponse = modelMapper.map(noteContainerModel, NoteResponse.class);
        return new ResponseEntity<>(noteResponse, HttpStatus.OK);
    }

    @GetMapping("/versions")
    @PreAuthorize("hasAuthority('sanotes_user')")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<NoteResponse>> getNoteVersions(
            @RequestParam(value = "id") UUID id,
            @Parameter(hidden = true)
            @CurrentUser OAuth2AuthenticatedPrincipal userPrincipal) {
        List<NoteVersionModel> noteVersionModels = noteService.getNoteVersions(id, userPrincipal.getName());
        List<NoteResponse> noteResponses = Arrays.asList(modelMapper.map(noteVersionModels, NoteResponse[].class));
        return new ResponseEntity<>(noteResponses, HttpStatus.OK);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('sanotes_user')")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse> deleteNote(
            @Valid @RequestBody ByIdRequest byIdRequest,
            @Parameter(hidden = true)
            @CurrentUser OAuth2AuthenticatedPrincipal userPrincipal) {
        ApiResponse apiResponse = noteService.deleteNote(byIdRequest, userPrincipal.getName());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/bynotebookid")
    @PreAuthorize("hasAuthority('sanotes_user')")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<NoteResponse>> getNotesByNoteBookId(
            @RequestParam(value = "id") UUID id,
            @Parameter(hidden = true)
            @CurrentUser OAuth2AuthenticatedPrincipal userPrincipal) {
        List<NoteContainerModel> noteContainerModels = noteService.getNotesByNoteBookId(id, userPrincipal.getName());
        List<NoteResponse> noteResponses = Arrays.asList(modelMapper.map(noteContainerModels, NoteResponse[].class));
        return new ResponseEntity<>(noteResponses, HttpStatus.OK);
    }

    @GetMapping("/bytagid")
    @PreAuthorize("hasAuthority('sanotes_user')")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<NoteResponse>> getNotesByTagId(
            @RequestParam(value = "id") UUID id,
            @Parameter(hidden = true)
            @CurrentUser OAuth2AuthenticatedPrincipal userPrincipal) {
        List<NoteContainerModel> noteContainerModels = noteService.getNotesByTagId(id, userPrincipal.getName());
        List<NoteResponse> noteResponses = Arrays.asList(modelMapper.map(noteContainerModels, NoteResponse[].class));
        return new ResponseEntity<>(noteResponses, HttpStatus.OK);
    }

}
