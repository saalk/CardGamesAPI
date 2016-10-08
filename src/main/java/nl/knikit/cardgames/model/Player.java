package nl.knikit.cardgames.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.hateoas.core.Relation;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * <H1>Player</H1>
 * The class variables are immutable ('private') so they can't be accessed outside class
 *
 * @author Klaas van der Meulen
 */

//@Entity is deprecated in Hibernate 4 so use JPA annotations directly in the model class
//TODO @DynamicUpdate from Hibernate

@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "PLAYER",
        indexes = {@Index(name = "PLAYER_INDEX", columnList = "PLAYER_ID")})
@Relation(value = "player", collectionRelation = "players")
public class Player implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.TABLE)
    @JsonProperty("id") private int id;

    @Transient
    private static int startId = 1;

    @Column(name = "PLAYER_ID")
    @JsonProperty("playerId") private String playerId;
    @Enumerated(EnumType.STRING)
    @Column(name = "AVATAR")
    @JsonProperty("avatar") private Avatar avatar;
    @Column(name = "ALIAS")
    @JsonProperty("alias") private String alias;
    @Column(name = "HUMAN")
    @JsonProperty("isHuman") private boolean isHuman;
    @Enumerated(EnumType.STRING)
    @Column(name = "AI_LEVEL")
    @JsonProperty("aiLevel") private AiLevel aiLevel;
    @Column(name = "CUBITS")
    @JsonProperty("cubits") private int cubits;
    @Column(name = "SECURED_LOAN")
    @JsonProperty("securedLoan") private int securedLoan;

    @JsonCreator
    public Player() {
        super();
        LocalDateTime localDateAndTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm-ssSSS-nnnnnnnnn");
        String result = localDateAndTime.format(formatter);
        this.playerId = result.substring(2, 25);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Player [id=").append(id).append("]");
        return builder.toString();
    }
}

