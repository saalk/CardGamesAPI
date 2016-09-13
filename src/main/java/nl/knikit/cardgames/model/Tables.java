/**
 * 
 */
package nl.knikit.cardgames.model;

import org.springframework.hateoas.core.Relation;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(	name = "TABLES",
		indexes = {
			@Index(columnList = "FK_GAME_ID", name = "FK_GAME_ID_INDEX"),
			@Index(columnList = "FK_PLAYER_ID", name = "FK_PLAYER_ID_INDEX"),
			@Index(columnList = "FK_HAND_ID", name = "FK_HAND_ID_INDEX")},
		uniqueConstraints = {
			@UniqueConstraint(columnNames = {"FK_GAME_ID", "FK_PLAYER_ID","FK_HAND_ID"})
		})
@Getter
@Setter
@ToString
@Relation(value="table", collectionRelation="tables")
public class Tables {

	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE)
	@Column(name = "TABLE_ID")
	private long id;
	@OneToOne
	@JoinColumn(name = "FK_GAME_ID", referencedColumnName = "GAME_ID")
	Game game;
	@OneToOne
	@JoinColumn(name = "FK_PLAYER_ID", referencedColumnName = "PLAYER_ID")
	Player player;
    @Column(name = "PLAYER_ORDER")
    private int playerOrder;
    @OneToOne
	@JoinColumn(name = "FK_HAND_ID", referencedColumnName = "HAND_ID")
	Hand hand;

}
