package pe.edu.tecsup.learnai.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "archive")
public class Archive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "source_code", nullable = false, length = 200)
    private String sourceCode;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    public Archive(String sourceCode, Subject subject) {
        this.sourceCode = sourceCode;
        this.subject = subject;
    }
}
