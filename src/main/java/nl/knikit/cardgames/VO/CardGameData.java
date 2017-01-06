package nl.knikit.cardgames.VO;

import nl.knikit.cardgames.model.Player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardGameData {

	private String cardGameId;

	private Player currentPlayer;


}
