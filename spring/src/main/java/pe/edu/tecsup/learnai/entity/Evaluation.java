package pe.edu.tecsup.learnai.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "evaluation")
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "code", nullable = false)
    private int code;

    @Column(name = "final_time", nullable = false)
    private int finalTime;

    @Column(name = "duration", nullable = false)
    private int duration;

    @Column(name = "enable", nullable = false)
    private boolean enable;

    @ManyToOne
    @JoinColumn(name = "archive_id", nullable = false)
    private Archive archive;

    public Evaluation(int code, int finalTime, int duration, boolean enable, Archive archive) {
        this.code = code;
        this.finalTime = finalTime;
        this.duration = duration;
        this.enable = enable;
        this.archive = archive;
    }
}
