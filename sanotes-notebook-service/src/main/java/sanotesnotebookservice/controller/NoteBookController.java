package sanotesnotebookservice.controller;

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
import sanotesnotebookservice.model.NoteBookModel;
import sanotesnotebookservice.payload.*;
import sanotesnotebookservice.security.CurrentUser;
import sanotesnotebookservice.service.NoteBookService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class NoteBookController {

    private final NoteBookService noteBookService;

    private final ModelMapper modelMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('sanotes_user')")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<NoteBookResponse> getNoteBook(
            @RequestParam(value = "id") UUID id,
            @Parameter(hidden = true)
            @CurrentUser OAuth2AuthenticatedPrincipal userPrincipal) {
        NoteBookModel noteBookModel = noteBookService.getNoteBook(id, userPrincipal.getName());
        NoteBookResponse noteBookResponse = modelMapper.map(noteBookModel, NoteBookResponse.class);
        return new ResponseEntity<>(noteBookResponse, HttpStatus.OK);
    }

    @GetMapping("/usernotebooks")
    @PreAuthorize("hasAuthority('sanotes_user')")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<NoteBookResponse>> getUserNoteBooks(
            @Parameter(hidden = true)
            @CurrentUser OAuth2AuthenticatedPrincipal userPrincipal) {
        List<NoteBookModel> noteBooks = noteBookService.getUserNoteBooks(userPrincipal.getName());
        List<NoteBookResponse> noteBookResponses = Arrays.asList(modelMapper.map(noteBooks, NoteBookResponse[].class));
        return new ResponseEntity<>(noteBookResponses, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('sanotes_user')")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<NoteBookResponse> addNoteBook(@Valid @RequestBody NoteBookRequest noteBook) {
        NoteBookModel noteBookModel = modelMapper.map(noteBook, NoteBookModel.class);
        noteBookModel = noteBookService.saveNoteBook(noteBookModel);
        NoteBookResponse noteBookResponse = modelMapper.map(noteBookModel, NoteBookResponse.class);
        return new ResponseEntity<>(noteBookResponse, HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('sanotes_user')")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<NoteBookResponse> updateNoteBook(
            @Valid @RequestBody NoteBookRequest noteBook,
            @Parameter(hidden = true)
            @CurrentUser OAuth2AuthenticatedPrincipal userPrincipal) {
        NoteBookModel noteBookModel = modelMapper.map(noteBook, NoteBookModel.class);
        noteBookModel = noteBookService.updateNoteBook(noteBookModel, userPrincipal.getName());
        NoteBookResponse noteBookResponse = modelMapper.map(noteBookModel, NoteBookResponse.class);
        return new ResponseEntity<>(noteBookResponse, HttpStatus.CREATED);
    }


    @DeleteMapping
    @PreAuthorize("hasAuthority('sanotes_user')")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse> deleteNoteBook(
            @Valid @RequestBody ByIdRequest byIdRequest,
            @Parameter(hidden = true)
            @CurrentUser OAuth2AuthenticatedPrincipal userPrincipal) {
        ApiResponse apiResponse = noteBookService.deleteNoteBook(byIdRequest, userPrincipal.getName());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/owner/{noteBookId}")
    @PreAuthorize("hasAuthority('sanotes_user')")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<BooleanResponse> isNoteBookOwner(
            @PathVariable("noteBookId") UUID id,
            @Parameter(hidden = true)
            @CurrentUser OAuth2AuthenticatedPrincipal userPrincipal) {
        BooleanResponse booleanResponse = new BooleanResponse(true);
        try {
            NoteBookModel noteBookModel = noteBookService.getNoteBook(id, userPrincipal.getName());
        } catch (Exception ue) {
            booleanResponse.setResult(false);
        }
        return new ResponseEntity<>(booleanResponse, HttpStatus.OK);
    }


}
