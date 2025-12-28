package ma.emsi.inscription_et_reinscription.dtos;



import ma.emsi.inscription_et_reinscription.entities.TypeDocument;

public class DocumentDTO {
    private Long id;
    private String nomFichier;
    private TypeDocument typeDocument;
    private String base64Content;
    private String contentType;

    // Constructeurs
    public DocumentDTO() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFichier() { return nomFichier; }
    public void setNomFichier(String nomFichier) { this.nomFichier = nomFichier; }

    public TypeDocument getTypeDocument() { return typeDocument; }
    public void setTypeDocument(TypeDocument typeDocument) { this.typeDocument = typeDocument; }

    public String getBase64Content() { return base64Content; }
    public void setBase64Content(String base64Content) { this.base64Content = base64Content; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }
}
