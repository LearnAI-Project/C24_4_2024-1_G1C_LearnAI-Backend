package pe.edu.tecsup.learnai.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "options")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "options", nullable = false, length = 150)
    private String options;

    @Column(name = "answer", nullable = false, length = 50)
    private String answer;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    public Option(String options, String answer, Question question) {
        this.options = options;
        this.answer = answer;
        this.question = question;
    }
}
