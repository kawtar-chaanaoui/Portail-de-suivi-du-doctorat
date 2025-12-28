package ma.emsi.inscription_et_reinscription.services;



import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

// Service temporaire pour simuler l'intégration avec le module user
// À remplacer par les appels Feign réels une fois le module user développé
@Service
public class UserIntegrationService {

    // Simulation des données utilisateur
    public Object getUserById(Long userId, String token) {
        // Simulation - à remplacer par appel Feign
        return new Object() {
            public Long getId() { return userId; }
            public String getCin() { return "CIN_" + userId; }
            public String getNom() { return "Nom_" + userId; }
            public String getPrenom() { return "Prenom_" + userId; }
            public String getEmail() { return "user" + userId + "@emsi.ma"; }
            public String getRole() { return "DOCTORANT"; }
        };
    }

    public Object getUserByCin(String cin, String token) {
        // Simulation - à remplacer par appel Feign
        return new Object() {
            public Long getId() { return 1L; }
            public String getCin() { return cin; }
            public String getNom() { return "Nom_" + cin; }
            public String getPrenom() { return "Prenom_" + cin; }
            public String getEmail() { return cin + "@emsi.ma"; }
            public String getRole() { return "DOCTORANT"; }
        };
    }

    public List<Object> getDirecteurs(String token) {
        // Simulation - à remplacer par appel Feign
        return Arrays.asList(
                createUserSimulation(101L, "DIR001", "Prof", "Ahmed", "ahmed@emsi.ma", "DIRECTEUR"),
                createUserSimulation(102L, "DIR002", "Prof", "Fatima", "fatima@emsi.ma", "DIRECTEUR"),
                createUserSimulation(103L, "DIR003", "Prof", "Karim", "karim@emsi.ma", "DIRECTEUR")
        );
    }

    public boolean isUserDoctorant(Long userId, String token) {
        // Simulation - à remplacer par appel Feign
        return true;
    }

    public boolean isUserDirecteur(Long userId, String token) {
        // Simulation - à remplacer par appel Feign
        return userId >= 100L; // Simulation: les IDs >= 100 sont des directeurs
    }

    private Object createUserSimulation(Long id, String cin, String nom, String prenom, String email, String role) {
        return new Object() {
            public Long getId() { return id; }
            public String getCin() { return cin; }
            public String getNom() { return nom; }
            public String getPrenom() { return prenom; }
            public String getEmail() { return email; }
            public String getRole() { return role; }
        };
    }
}
