package by.library.mylib.repository;

import by.library.mylib.entity.Note;
import by.library.mylib.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note,Long> {
    Optional<List<Note>> getAllByUserId(Long id);
    Optional<List<Note>> getAllByOrderByNameDesc();
    Optional<Note> getByIdAndUser(Long id, User User);
}
