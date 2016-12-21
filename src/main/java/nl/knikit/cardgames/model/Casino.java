package nl.knikit.cardgames.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;

@Entity
@DynamicUpdate
@Table(name = "CASINO",
        indexes = {
                @Index(columnList = "GAME_ID", name = "GAME_ID_INDEX"),
                @Index(columnList = "PLAYER_ID", name = "PLAYER_ID_INDEX")},
        uniqueConstraints = {
                @UniqueConstraint(name="UC_GAME_PLAYER", columnNames = {"GAME_ID", "PLAYER_ID"})
        })
//@Relation(value = "casino", collectionRelation = "casinos")
@Getter
@Setter
//@JsonIdentityInfo(generator=JSOGGenerator.class)
// - this annotation adds @Id to prevent chain loop
// - you could also use @JsonManagedReference and @JsonBackReference
public class Casino implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "CASINO_ID")
    ////@JsonProperty("casinoId")
    public int casinoId;

    @Column(name = "CREATED", length = 25)
    ////@JsonProperty("created")
    private String created;

    //@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "GAME_ID", referencedColumnName = "GAME_ID", foreignKey = @ForeignKey(name = "GAME_ID"))
    ////@JsonProperty("game")
    private Game game;
    
    //@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PLAYER_ID", referencedColumnName = "PLAYER_ID", foreignKey = @ForeignKey(name = "PLAYER_ID"))
    ////@JsonProperty("player")
    private Player player;
    
    // Cascade = any change happened on this entity must cascade to the parent/child as well
    // since this is the parent Player: delete Game when Player is deleted (no other actions!)
    //@JsonIgnore
    @OneToMany(cascade=CascadeType.REMOVE)
    @JsonIgnore
    @JoinColumn(name = "CASINO_ID", referencedColumnName = "CASINO_ID", foreignKey = @ForeignKey(name = "CASINO_ID"))
    private List<Hand> hands;

    @Column(name = "PLAYING_ORDER")
    ////@JsonProperty("playingOrder")
    private int playingOrder;

    public Casino() {
        LocalDateTime localDateAndTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm-ssSSS-nnnnnnnnn");
        String result = localDateAndTime.format(formatter);
        this.created = result.substring(2, 20);
    }
}
