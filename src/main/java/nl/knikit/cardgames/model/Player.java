/**
 *
 */
package nl.knikit.cardgames.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.ToString;
import org.springframework.hateoas.core.Relation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.*;


import lombok.Getter;
import lombok.Setter;

/**
 * <H1>Player</H1>
 * Do not use googles @AutoValue library here to see the difference with Deck
 * Do not use lombok @Builder here to see the difference with ..
 * AutoValue:
 * - class is final so it can't be extended so inheritance is prohibited!
 * - the class variables are immutable ('private') so they can't be accessed outside class
 *
 * @author Klaas van der Meulen
 */

//@Entity is deprecated in Hibernate 4 so use JPA annotations directly in the model class
//TODO @DynamicUpdate from Hibernate

@Getter
@Setter
@ToString
@Entity
@Table(name = "PLAYERS",
        indexes = {@Index(name = "PLAYERS_INDEX", columnList = "PLAYER_ID")})
@Relation(value = "player", collectionRelation = "players")
public final class Player {

    @Transient
    private static int startId = 1;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "PLAYER_ID")
    private String playerId;
    @Column(name = "SEQUENCE")
    private int sequence;
    @Enumerated(EnumType.STRING)
    @Column(name = "ORIGIN")
    private Origin origin;
    @Column(name = "ALIAS")
    private String alias;
    @Column(name = "HUMAN")
    private boolean isHuman;
    @Enumerated(EnumType.STRING)
    @Column(name = "AI_LEVEL")
    private AiLevel aiLevel;
    @Column(name = "CUBITS")
    private int cubits;
    @Column(name = "SECURED_LOAN")
    private int securedLoan;

    // TODO make private
    public Player() {
    }

    @JsonCreator
    public Player(
            @JsonProperty("id") Long id,
            @JsonProperty("playerId") String playerId,
            @JsonProperty("sequence") int sequence,
            @JsonProperty("origin") Origin origin,
            @JsonProperty("alias") String alias,
            @JsonProperty("isHuman") boolean isHuman,
            @JsonProperty("aiLevel") AiLevel aiLevel,
            @JsonProperty("cubits") int cubits,
            @JsonProperty("securedLoan") int securedLoan) {
        this.id = id;
        this.playerId = playerId;
        this.sequence = sequence;
        this.origin = origin;
        this.alias = alias;
        this.isHuman = isHuman;
        this.aiLevel = aiLevel;
        this.cubits = cubits;
        this.securedLoan = securedLoan;
    }

    public void setSequence(int sequence) {
        if (sequence == 0) {
            this.sequence = startId++;
        } else {
            this.sequence = sequence;
        }
    }

    public static void setSequenceBackWithOne() {
        if (startId > 1) {
            startId--;
        }
    }

    public static void setSequenceToFirst() {
        if (startId > 1) {
            startId = 1;
        }
    }

    private void setPlayerId(String playerId) {
        if (playerId == "" || playerId == null) {
            // java 8 has java.time and java.time.format instead of java.util.Date
            // get local date and a format use format() to store the result into id
            LocalDateTime localDateAndTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm-ssSSS-nnnnnnnnn");
            String result = localDateAndTime.format(formatter);
            this.playerId = result.substring(2, 25);
        } else {
            this.playerId = playerId;
        }
    }


    // static builder class generated with plugin Builder Generator and ALT+SHIFT+B
    // when seperate class then builder and build class should both be protected instead of private
    @ToString
    public static final class PlayerBuilder {
        private Long id;
        private String playerId;
        private int sequence;
        private Origin origin;
        private String alias;
        private boolean isHuman;
        private AiLevel aiLevel;
        private int cubits;
        private int securedLoan;

        // TODO  only receive the required attributes
        public PlayerBuilder() {
        }

        public static PlayerBuilder aPlayer() {
            return new PlayerBuilder();
        }

        public PlayerBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public PlayerBuilder withPlayerId(String playerId) {
            this.playerId = playerId;
            return this;
        }

        public PlayerBuilder withSequence(int sequence) {
            this.sequence = sequence;
            return this;
        }

        public PlayerBuilder withOrigin(Origin origin) {
            this.origin = origin;
            return this;
        }

        public PlayerBuilder withAlias(String alias) {
            this.alias = alias;
            return this;
        }

        public PlayerBuilder withIsHuman(boolean isHuman) {
            this.isHuman = isHuman;
            return this;
        }

        public PlayerBuilder withAiLevel(AiLevel aiLevel) {
            this.aiLevel = aiLevel;
            return this;
        }

        public PlayerBuilder withCubits(int cubits) {
            this.cubits = cubits;
            return this;
        }

        public PlayerBuilder withSecuredLoan(int securedLoan) {
            this.securedLoan = securedLoan;
            return this;
        }

        public Player build() {
            Player player = new Player(id, playerId, sequence, origin, alias, isHuman, aiLevel,
                    cubits, securedLoan);
            player.setPlayerId(playerId);
            player.setSequence(sequence);

            return player;
        }
    }
}
