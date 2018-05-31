package by.tryput.moneyman.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Embeddable
public class Action {

    @Enumerated(EnumType.STRING)
    private Type type;
}
