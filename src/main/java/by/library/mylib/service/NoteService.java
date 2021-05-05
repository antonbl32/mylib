package by.library.mylib.service;

import by.library.mylib.dto.NoteDTO;
import by.library.mylib.entity.Note;
import by.library.mylib.entity.User;
import by.library.mylib.exceptions.NoteNotFoundException;
import by.library.mylib.repository.NoteRepository;
import by.library.mylib.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class NoteService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    public Note createNote(NoteDTO noteDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        Note note = new Note();
        note.setName(noteDTO.getName());
        note.setUser(user);
        note.setDesc(noteDTO.getDesc());
        LOG.info("Note {} is saving", note.getName());
        return noteRepository.save(note);
    }

    public List<Note> getAllNote() {
        LOG.info("Get all posts");
        return noteRepository.getAllByOrderByNameDesc().orElseThrow(() -> new NoteNotFoundException("No post found"));
    }

    public Note getNoteById(Long id, Principal principal) {
        User user = getUserByPrincipal(principal);
        LOG.info("Get post by Id " + id);
        return noteRepository.getByIdAndUser(id, user).orElseThrow(() -> new NoteNotFoundException("No post found"));
    }

    public List<Note> getNotesByUser(Principal principal) {
        User user = getUserByPrincipal(principal);
        return noteRepository.getAllByUserId(user.getId()).orElseThrow(() -> new NoteNotFoundException("No post found"));
    }

    public void deleteNote(Long id,Principal principal){
        User user=getUserByPrincipal(principal);
        Optional<Note> note=noteRepository.getByIdAndUser(id,user);
        if(note.isPresent()){
            noteRepository.delete(note.get());
            LOG.info("Post "+note.get().getName()+" is deleting");
        }else{
            new NoteNotFoundException("Post not deleted");
        }
    }


    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        User user = userRepository.getUserByUsername(username).orElseThrow(() -> new NoteNotFoundException("No user " + username));
        return user;
    }
}
