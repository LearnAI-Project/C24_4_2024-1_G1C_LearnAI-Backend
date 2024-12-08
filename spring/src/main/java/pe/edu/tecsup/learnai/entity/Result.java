package pe.edu.tecsup.learnai.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "results")
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username", nullable = false, length = 150)
    private String username;

    @Column(name = "email", nullable = false, length = 320)
    private String email;

    @Column(name = "correct", nullable = false)
    private int correct;

    @Column(name = "incorrect", nullable = false)
    private int incorrect;

    @Column(name = "score", nullable = false)
    private int score;

    @ManyToOne
    @JoinColumn(name = "evaluation_id", nullable = false)
    private Evaluation evaluation;

    @Column(name = "publication_date", nullable = false)
    private Date publicationDate;

    public Result(String username, String email, int correct, int incorrect, int score, Evaluation evaluation, Date publicationDate) {
        this.username = username;
        this.email = email;
        this.correct = correct;
        this.incorrect = incorrect;
        this.score = score;
        this.evaluation = evaluation;
        this.publicationDate = publicationDate;
    }
}
