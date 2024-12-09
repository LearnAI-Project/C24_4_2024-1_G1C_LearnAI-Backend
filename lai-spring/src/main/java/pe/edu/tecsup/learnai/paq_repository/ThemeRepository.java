package pe.edu.tecsup.learnai.paq_repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.tecsup.learnai.paq_entity.Theme;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {
}
