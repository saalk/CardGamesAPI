/**
 *
 */
package nl.knikit.cardgames.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.hateoas.core.Relation;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

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
 *
 * @author Klaas van der Meulen
 */

//@Entity is deprecated in Hibernate 4 so use JPA, use @DynamicUpdate from Hibernate
@Entity
@Table(name = "PLAYERS", catalog = "catalog")
@Getter
@Setter
@Relation(value="player", collectionRelation="players")
public final class Player {

    /**
     * startId static maken ivm onthouden ophogen in constructor
     */
    private static int startId = 0;

    /**
     * id is final, initialization in constructor and no setter
     */
    @Id
    private String uid;
    private int sequence;
    private Origin origin;
    private String alias;
    private boolean isHuman;
    private AiLevel aiLevel;
    private int cubits;
    private int securedLoan;

    public Player() {
    }

    @JsonCreator
    public Player(
            @JsonProperty("uid") String uid,
            @JsonProperty("sequence") int sequence,
            @JsonProperty("origin") Origin origin,
            @JsonProperty("alias") String alias,
            @JsonProperty("isHuman") boolean isHuman,
            @JsonProperty("aiLevel") AiLevel aiLevel,
            @JsonProperty("cubits") int cubits,
            @JsonProperty("securedLoan") int securedLoan) {
        this.uid = uid;
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

    private void setUid() {

        // java 8 has java.time and java.time.format instead of java.util.Date
        // get local date and a format use format() to store the result into uid
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LocalDateTime localDateAndTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");
        String result = localDateAndTime.format(formatter);
        this.uid = result.substring(2,19);
    }

    public int getSequence() {
        return this.sequence;
    }

    public String getUid() {
        return uid;
    }

    // static builder class generated with plugin Builder Generator and ALT+SHIFT+B
    // when seperate class then builder and buil class should both be protected instead of private

    public static final class PlayerBuilder {
        private String uid;
        private int sequence;
        private Origin origin;
        private String alias;
        private boolean isHuman;
        private AiLevel aiLevel;
        private int cubits;
        private int securedLoan;

        public PlayerBuilder() {
        }

        public static PlayerBuilder aPlayer() {
            return new PlayerBuilder();
        }

        public PlayerBuilder withUid(String uid) {
            this.uid = uid;
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
            Player player = new Player(uid, sequence, origin, alias, isHuman, aiLevel, cubits,
                    securedLoan);
            // do not use input uid
            player.setUid();
            if (sequence >= 0) {
                player.setSequence(sequence);
            } else {
                player.setSequence();
            }
            return player;
        }
    }
}
