package pe.edu.tecsup.learnai.paq_services;

import pe.edu.tecsup.learnai.paq_entity.Theme;

import java.util.Optional;

public interface ThemeService {

    void saveTheme(Theme theme);

    Optional<Theme> findThemeById(Integer id);
}
