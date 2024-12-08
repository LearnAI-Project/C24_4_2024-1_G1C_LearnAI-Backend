package pe.edu.tecsup.learnai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.tecsup.learnai.entity.Syllable;

@Repository
public interface SyllableRepository extends JpaRepository<Syllable, Long> {
}
