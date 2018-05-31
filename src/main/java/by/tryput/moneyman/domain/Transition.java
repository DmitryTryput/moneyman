package by.tryput.moneyman.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "task_transitions")
public class Transition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "from_task")
    private String fromTask;
    @Column(name = "to_task")
    private String toTask;

    public Transition(String fromTask, String toTask) {
        this.fromTask = fromTask;
        this.toTask = toTask;
    }
}



