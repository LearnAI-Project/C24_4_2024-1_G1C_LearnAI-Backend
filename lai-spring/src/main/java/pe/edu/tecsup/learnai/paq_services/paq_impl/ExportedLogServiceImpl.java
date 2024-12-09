package pe.edu.tecsup.learnai.paq_services.paq_impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.tecsup.learnai.paq_entity.ExportedLog;
import pe.edu.tecsup.learnai.paq_repository.ExportedLogRepository;
import pe.edu.tecsup.learnai.paq_services.ExportedLogService;

@RequiredArgsConstructor
@Service
public class ExportedLogServiceImpl implements ExportedLogService {

    private final ExportedLogRepository exportedLogRepository;

    @Override
    public void saveExportedLog(ExportedLog exportedLog) {
        exportedLogRepository.save(exportedLog);
    }

}
