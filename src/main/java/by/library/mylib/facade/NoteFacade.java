package by.library.mylib.facade;

import by.library.mylib.dto.NoteDTO;
import by.library.mylib.entity.Note;
import org.springframework.stereotype.Component;

@Component
public class NoteFacade {
    public NoteDTO noteToNoteDTO(Note note) {
        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setId(note.getId());
        noteDTO.setName(note.getName());
        noteDTO.setDesc(note.getDesc());
        noteDTO.setUsername(note.getUser().getUsername());
        return noteDTO;
    }
}
