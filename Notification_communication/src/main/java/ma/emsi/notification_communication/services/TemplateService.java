package ma.emsi.notification_communication.services;

import lombok.RequiredArgsConstructor;
import ma.emsi.notification_communication.entite.EmailTemplate;
import ma.emsi.notification_communication.Repositories.EmailTemplateRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TemplateService {
    private final EmailTemplateRepository templateRepository;

    public EmailTemplate saveTemplate(EmailTemplate template) {
        return templateRepository.save(template);
    }

    public List<EmailTemplate> getAllTemplates() {
        return templateRepository.findAll();
    }

    public Optional<EmailTemplate> getTemplateByName(String name) {
        return templateRepository.findByName(name);
    }

    public Optional<EmailTemplate> getTemplateByCode(String code) {
        return templateRepository.findByCode(code);
    }

    public Optional<EmailTemplate> getActiveTemplateByCode(String code) {
        return templateRepository.findByCodeAndActiveTrue(code);
    }

    public String processTemplate(String template, Map<String, Object> variables) {
        String result = template;
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            result = result.replace("${" + entry.getKey() + "}", 
                                 entry.getValue() != null ? entry.getValue().toString() : "");
        }
        return result;
    }

    public void deleteTemplate(Long id) {
        templateRepository.deleteById(id);
    }
    
    public boolean existsByName(String name) {
        return getTemplateByName(name).isPresent();
    }
    
    public boolean existsByCode(String code) {
        return getTemplateByCode(code).isPresent();
    }
}