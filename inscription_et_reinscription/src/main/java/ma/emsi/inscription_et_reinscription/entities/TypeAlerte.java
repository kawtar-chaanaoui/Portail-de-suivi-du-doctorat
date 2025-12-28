package ma.emsi.inscription_et_reinscription.entities;

public enum TypeAlerte {
    LIMITE_3_ANS_APPROCHEE,      // À 2.5 ans - alerte pour préparer dérogation
    LIMITE_3_ANS_ATTEINTE,       // À 3 ans - dérogation obligatoire
    LIMITE_6_ANS_APPROCHEE,      // À 5 ans - alerte critique
    LIMITE_6_ANS_ATTEINTE,       // À 6 ans - limite maximale
    REINSCRIPTION_OBLIGATOIRE    // Période de réinscription ouverte
}
