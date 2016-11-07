/**
 *
 */
package nl.knikit.cardgames.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.hateoas.core.Relation;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@DynamicUpdate
@Table(name = "CASINO",
        indexes = {
                @Index(columnList = "FK_GAME", name = "FK_GAME_INDEX"),
                @Index(columnList = "FK_PLAYER", name = "FK_PLAYER_INDEX"),
                @Index(columnList = "FK_HAND", name = "FK_HAND_INDEX")},
        uniqueConstraints = {
                @UniqueConstraint(name="UC_GAME_PLAYER_HAND", columnNames = {"FK_GAME", "FK_PLAYER", "FK_HAND"})
        })
@Getter
@Setter

@Relation(value = "casino", collectionRelation = "casinos")
public class Casino implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "ID")
    @JsonProperty("id") private int id;

    @Column(name = "CREATED", length = 25)
    @JsonProperty("created") private String created;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_GAME", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_GAME"), insertable = false, updatable = false)
    @JsonProperty("fkGame") private Game fkGame;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_PLAYER", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_PLAYER"), insertable = false, updatable = false)
    @JsonProperty("fkPlayer") private Player fkPlayer;

    @Column(name = "PLAYING_ORDER")
    @JsonProperty("playingOrder") private int playingOrder;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_HAND", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_HAND"), insertable = false, updatable = false)
    @JsonProperty("fkHand") private Hand fkHand;

    @JsonCreator
    public Casino() {
        LocalDateTime localDateAndTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm-ssSSS-nnnnnnnnn");
        String result = localDateAndTime.format(formatter);
        this.created = result.substring(2, 25);
    }

    @JsonCreator
    public Casino(Game fkGame, Player fkPlayer, int playingOrder) {
        super();
        this.fkGame = fkGame;
        this.fkPlayer = fkPlayer;
        this.playingOrder = playingOrder;
        this.fkHand = null;
    }


    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Casino [id=").append(id).append("]");
        return builder.toString();
    }
}
