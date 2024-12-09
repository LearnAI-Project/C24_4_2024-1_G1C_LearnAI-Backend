package pe.edu.tecsup.learnai.paq_services.paq_impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.tecsup.learnai.paq_entity.Theme;
import pe.edu.tecsup.learnai.paq_repository.ThemeRepository;
import pe.edu.tecsup.learnai.paq_services.ThemeService;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ThemeServiceImpl implements ThemeService {

    private final ThemeRepository themeRepository;

    @Override
    public void saveTheme(Theme theme) {
        themeRepository.save(theme);
    }

    @Override
    public Optional<Theme> findThemeById(Integer id) { return themeRepository.findById(id.longValue()); }
}
