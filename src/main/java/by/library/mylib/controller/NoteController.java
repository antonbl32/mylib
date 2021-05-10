package by.library.mylib.controller;

import by.library.mylib.dto.NoteDTO;
import by.library.mylib.entity.Note;
import by.library.mylib.facade.NoteFacade;
import by.library.mylib.payload.response.MessageResponse;
import by.library.mylib.service.NoteService;
import by.library.mylib.validation.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/note")
@CrossOrigin
public class NoteController {

    private NoteService noteService;
    private NoteFacade noteFacade;
    private ResponseErrorValidation responseErrorValidation;
    @Autowired
    public void setNoteService(NoteService noteService) {
        this.noteService = noteService;
    }
    @Autowired
    public void setNoteFacade(NoteFacade noteFacade) {
        this.noteFacade = noteFacade;
    }
    @Autowired
    public void setResponseErrorValidation(ResponseErrorValidation responseErrorValidation) {
        this.responseErrorValidation = responseErrorValidation;
    }

    /**
     * This method add new note for current user in db
     * @param noteDTO it facade from note object
     * @param bindingResult it validation
     * @param principal
     * @return
     */
    @PostMapping("/create")
    public ResponseEntity<Object> createNote(@Valid @RequestBody NoteDTO noteDTO,
                                             BindingResult bindingResult, Principal principal) {
        ResponseEntity<Object> error = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(error)) return error;
        Note note = noteService.createNote(noteDTO, principal);
        NoteDTO createdNote = noteFacade.noteToNoteDTO(note);
        return new ResponseEntity<>(createdNote, HttpStatus.OK);
    }

    /**
     * @return All notes in db
     */
    @GetMapping("/all")
    public ResponseEntity<List<NoteDTO>> getAllNote() {
        List<NoteDTO> noteDTOList = noteService.getAllNote().stream()
                .map(noteFacade::noteToNoteDTO).collect(Collectors.toList());
        return new ResponseEntity<>(noteDTOList, HttpStatus.OK);
    }

    /**
     *
     * @param principal user details from client
     * @return all notes for current user
     */
    @GetMapping("/user/notes")
    public ResponseEntity<List<NoteDTO>> getAllNotesByUser(Principal principal) {
        List<NoteDTO> noteDTOList = noteService.getNotesByUser(principal).stream()
                .map(noteFacade::noteToNoteDTO).collect(Collectors.toList());
        return new ResponseEntity<>(noteDTOList, HttpStatus.OK);
    }

    /**
     *
     * @param noteId id for note that can be delete
     * @param principal user details from client
     * @return String with status 200
     */
    @PostMapping("/{id}/delete")
    public ResponseEntity<MessageResponse> deleteNote(@PathVariable("id") String noteId, Principal principal) {
        noteService.deleteNote(Long.parseLong(noteId), principal);
        return new ResponseEntity<>(new MessageResponse("Post was deleted " + noteId), HttpStatus.OK);
    }
}
