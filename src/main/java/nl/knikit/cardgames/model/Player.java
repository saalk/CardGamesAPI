/**
 *
 */
package nl.knikit.cardgames.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.hateoas.core.Relation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


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
 *
 * @author Klaas van der Meulen
 */

//@Entity is deprecated in Hibernate 4 so use JPA annotations directly in the model class
//TODO @DynamicUpdate from Hibernate
@Entity
@Table(name = "PLAYERS")
@Getter
@Setter
@Relation(value="player", collectionRelation="players")
public final class Player {

    @Transient
    private static int startId = 0;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "PLAYER_ID")
    private long id;
    @Column(name = "CREATED")
    private String created;
    @Column(name = "SEQUENCE")
    private int sequence;
    @Embedded
    @Enumerated(EnumType.STRING)
    private Origin origin;
    @Column(name = "ALIAS")
    private String alias;
    @Column(name = "HUMAN")
    private boolean isHuman;
    @Embedded
    @Enumerated(EnumType.STRING)
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
            @JsonProperty("id") long id,
            @JsonProperty("created") String created,
            @JsonProperty("sequence") int sequence,
            @JsonProperty("origin") Origin origin,
            @JsonProperty("alias") String alias,
            @JsonProperty("isHuman") boolean isHuman,
            @JsonProperty("aiLevel") AiLevel aiLevel,
            @JsonProperty("cubits") int cubits,
            @JsonProperty("securedLoan") int securedLoan)
    {
        this.id = id;
        this.created = created;
        this.sequence = sequence;
        this.origin = origin;
        this.alias = alias;
        this.isHuman = isHuman;
        this.aiLevel = aiLevel;
        this.cubits = cubits;
        this.securedLoan = securedLoan;
    }

    private void setSequence() {
        this.sequence = startId++;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public static void setSequenceBackWithOne() {
        if (startId > 0) {
            startId--;
        }
    }

    public static void setSequenceToZero() {
        if (startId > 0) {
            startId = 0;
        }
    }

    private void setCreated() {

        // java 8 has java.time and java.time.format instead of java.util.Date
        // get local date and a format use format() to store the result into id
        LocalDateTime localDateAndTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm-ssSSS");
        String result = localDateAndTime.format(formatter);
        this.created = result.substring(2,17);
    }

    // static builder class generated with plugin Builder Generator and ALT+SHIFT+B
    // when seperate class then builder and build class should both be protected instead of private
    public static final class PlayerBuilder {
        private long id;
        private String created;
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

        public PlayerBuilder withId(long id) {
            this.id = id;
            return this;
        }

        public PlayerBuilder withCreated(String created) {
            this.created = created;
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
            Player player = new Player(id, created, sequence, origin, alias, isHuman, aiLevel,
                    cubits, securedLoan);
            player.setCreated();
            // when -1 use set sequence to auto generated a sequence otherwise it is a update
            if (sequence >= 0) {
                player.setSequence(sequence);
            } else {
                player.setSequence();
            }
            return player;
        }
    }
}
