package nl.knikit.cardgames.model;

import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

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
@Table(name = "PLAYER", indexes = {@Index(name = "PLAYER_INDEX", columnList = "PLAYER_ID")})
//@Table(name = "PLAYER")
//@Relation(value = "player", collectionRelation = "players")
//@JsonIdentityInfo(generator=JSOGGenerator.class)
// - this annotation adds @Id to prevent chain loop
// - you could also use @JsonManagedReference and @JsonBackReference
public class Player implements Serializable {
    
    // 9 fields
    @Id
    @Column(name = "PLAYER_ID")
    @GeneratedValue(strategy = GenerationType.TABLE)
    ////@JsonProperty("playerId")
    private int playerId;

    @Transient
    private static int startId = 1;

    @Column(name = "CREATED", length = 25)
    ////@JsonProperty("created")
    private String created;

    @Enumerated(EnumType.STRING)
    @Column(name = "AVATAR", nullable = false)
    ////@JsonProperty("avatar")
    private Avatar avatar;
	
	@Column(name = "ALIAS")
	////@JsonProperty("alias")
	private String alias;
	
    @Column(name = "HUMAN")
    @Setter(AccessLevel.NONE)
    ////@JsonProperty("human")
    private boolean human;

    @Enumerated(EnumType.STRING)
    @Column(name = "AI_LEVEL", nullable = false)
    ////@JsonProperty("aiLevel")
    private AiLevel aiLevel;

    @Column(name = "CUBITS")
    ////@JsonProperty("cubits")
    private int cubits;
    
    @Column(name = "SECURED_LOAN")
    ////@JsonProperty("securedLoan")
    private int securedLoan;
    
    // Cascade = any change happened on this entity must cascade to the parent/child as well
    // since this is the parent Player: delete Game when Player is deleted (no other actions!)
    //@JsonIgnore
    @OneToMany(cascade=CascadeType.REMOVE)
    @JoinColumn(name = "PLAYER_ID", referencedColumnName = "PLAYER_ID", foreignKey = @ForeignKey(name = "PLAYER_ID"))
    ////@JsonProperty("gameDtos")
    private List<Game> games;


    /* if you want to get all gameDtos for a specific winner...
    @OneToMany(mappedBy="player",targetEntity=Game.class,
            fetch=FetchType.EAGER)
    private Collection gameDtos;
    */

    /*
    Get
    - The number of gameDtos a player (top ten) has attended
    - The gameDtos won by that player

    Query query = em.createQuery("SELECT player FROM PLAYER player WHERE playerId = x");
    List list= query.getResultList();

    for(Player player:list){
        List gameList = new ArrayList();
        List gameListWon = new ArrayList();
        // get the casinos attended, filters players not in a casino (todo)
        if(player.getCasinos()!=null){
            for(Casinos allCasinos: player.getCasinos()){
                if(allCasinos.getGame().getGameWon() == null){
                    //Find out how many gameDtos each player has
                    gameList.addAll(allCasinos.getGamesList());
                }else{
                    //Find out how many gameDtos won by the player
                    gameListWon.addAll(allCasinos.getGamesList());
                }
            }
        }
    }
    */
    public Player() {
        LocalDateTime localDateAndTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm-ssSSS-nnnnnnnnn");
        String result = localDateAndTime.format(formatter);
        this.created = result.substring(2, 20);
    }
	
	public void setHuman(boolean human) {
		this.human = human;
	}
	
	public boolean getHuman() {
		return this.human;
	}
	
}

