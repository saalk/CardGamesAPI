package nl.knikit.cardgames.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
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
@ToString
@Entity
@DynamicUpdate
// @Table(name = "PLAYER", indexes = {@Index(name = "PLAYER_INDEX", columnList = "PLAYER_ID")})
@Table(name = "PLAYER")
@Relation(value = "player", collectionRelation = "players")
public class Player implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.TABLE)
    @JsonProperty("id") private int id;

    @Transient
    private static int startId = 1;

    @Column(name = "CREATED", length = 25)
    @JsonProperty("created") private String created;

    @Enumerated(EnumType.STRING)
    //@Type(type = "nl.knikit.cardgames.model.enumlabel.LabeledEnumType")
    @Column(name = "AVATAR", nullable = false)
    @JsonProperty("avatar") private Avatar avatar;

    @Column(name = "ALIAS")
    @JsonProperty("alias") private String alias;
    @Column(name = "HUMAN")
    @JsonProperty("isHuman") private boolean isHuman;

    @Enumerated(EnumType.STRING)
    //@Type(type = "nl.knikit.cardgames.model.enumlabel.LabeledEnumType")
    @Column(name = "AI_LEVEL", nullable = false)
    @JsonProperty("aiLevel") private AiLevel aiLevel;

    @Column(name = "CUBITS")
    @JsonProperty("cubits") private int cubits;
    @Column(name = "SECURED_LOAN")
    @JsonProperty("securedLoan") private int securedLoan;

    /* if you want to get all games for a specific winner...
    @OneToMany(mappedBy="player",targetEntity=Game.class,
            fetch=FetchType.EAGER)
    private Collection games;
    */

    /*
    Get
    - The number of games a player (top ten) has attended
    - The games won by that player

    Query query = em.createQuery("SELECT player FROM PLAYER player WHERE playerId = x");
    List list= query.getResultList();

    for(Player player:list){
        List gameList = new ArrayList();
        List gameListWon = new ArrayList();
        // get the casinos attended, filters players not in a casino (todo)
        if(player.getCasinos()!=null){
            for(Casinos allCasinos: player.getCasinos()){
                if(allCasinos.getGame().getGameWon() == null){
                    //Find out how many games each player has
                    gameList.addAll(allCasinos.getGamesList());
                }else{
                    //Find out how many games won by the player
                    gameListWon.addAll(allCasinos.getGamesList());
                }
            }
        }
    }
    */
    @JsonCreator
    public Player() {
        LocalDateTime localDateAndTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm-ssSSS-nnnnnnnnn");
        String result = localDateAndTime.format(formatter);
        this.created = result.substring(2, 20);
    }
}

