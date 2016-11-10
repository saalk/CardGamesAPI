/**
 *
 */
package nl.knikit.cardgames.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Proxy;
import org.springframework.hateoas.core.Relation;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@DynamicUpdate
@Table(name = "CASINO",
        indexes = {
                @Index(columnList = "GAME_ID", name = "GAME_ID_INDEX"),
                @Index(columnList = "PLAYER_ID", name = "PLAYER_ID_INDEX"),
                @Index(columnList = "HAND_ID", name = "HAND_ID_INDEX")},
        uniqueConstraints = {
                @UniqueConstraint(name="UC_GAME_PLAYER_HAND", columnNames = {"GAME_ID", "PLAYER_ID", "HAND_ID"})
        })
@Relation(value = "casino", collectionRelation = "casinos")
@Getter
@Setter
@ToString
@JsonIdentityInfo(generator=JSOGGenerator.class)
public class Casino implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "CASINO_ID")
    @JsonProperty("casinoId") private int casinoId;

    @Column(name = "CREATED", length = 25)
    @JsonProperty("created") private String created;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "GAME_ID", referencedColumnName = "GAME_ID", foreignKey = @ForeignKey(name = "GAME_ID"))
    @JsonProperty("gameObj") private Game gameObj;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PLAYER_ID", referencedColumnName = "PLAYER_ID", foreignKey = @ForeignKey(name = "PLAYER_ID"))
    @JsonProperty("playerObj") private Player playerObj;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "HAND_ID", referencedColumnName = "HAND_ID", foreignKey = @ForeignKey(name = "HAND_ID"))
    @JsonProperty("handObj") private Hand handObj;

    @Column(name = "PLAYING_ORDER")
    @JsonProperty("playingOrder") private int playingOrder;

    public Casino() {
        LocalDateTime localDateAndTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm-ssSSS-nnnnnnnnn");
        String result = localDateAndTime.format(formatter);
        this.created = result.substring(2, 20);
    }

    public Casino(Game gameObj, Player playerObj, Hand handObj, int playingOrder) {
        this();
        this.gameObj = gameObj;
        this.playerObj = playerObj;
        this.handObj = handObj;
        this.playingOrder = playingOrder;
    }
}
