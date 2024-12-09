package pe.edu.tecsup.learnai.paq_entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "exported_log")
public class ExportedLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "theme_id", nullable = false, foreignKey = @ForeignKey(name = "fk_exported_log_theme_id"))
    private Theme theme;

    // Constructor para inicializar la relación
    public ExportedLog(Theme theme) {
        this.theme = theme;
    }
}
