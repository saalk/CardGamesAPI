package nl.knikit.cardgames.DTO;

import nl.knikit.cardgames.model.AiLevel;
import nl.knikit.cardgames.model.Avatar;
import nl.knikit.cardgames.model.Player;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerDto {
	
	// Player has 9 fields, PlayerDto has 2 more
	
	// discard lombok setter for this field -> make your own
	@Setter(AccessLevel.NONE)
	private String name; // extra field "Script Joe(Human|Smart) [Elf]"
	private int playerId;
	private String created;
	private String alias;
	@Setter(AccessLevel.NONE)
	private String human; // changed field for boolean
	private String aiLevel;
	private String avatar;
	private int cubits;
	private int securedLoan;
	private List<GameDto> games;
	@Setter(AccessLevel.NONE)
	private int winCount; // extra field
	
	public Avatar getAvatarConvertedFromLabel(String avatarLabel) throws Exception {
		Avatar converted = Avatar.fromLabel(avatar);
		if (converted == null) {
			throw new Exception("AvatarParseLabelException");
		}
		return converted;
	}
	
	public void setAvatar(Avatar avatar) {
		// static Eum methods:
		// - valueOf() - returns enum instance taking a String
		// - values()  - returns all enum instances
		// instance Enum method:
		// - name()    - returns name of enum constant
		// -> better use toString() to get the user-friendly name
		this.avatar = (String.valueOf(avatar));
	}
	
	public AiLevel getAiLevelConvertedFromLabel(String aiLevelLabel) throws Exception {
		AiLevel converted = AiLevel.fromLabel(aiLevelLabel);
		if (converted == null) {
			throw new Exception("AiLevelParseLabelException");
		}
		return converted;
	}
	
	public void setAiLevel(AiLevel aiLevel) {
		// static Eum methods:
		// - valueOf() - returns enum instance taking a String
		// - values()  - returns all enum instances
		// instance Enum method:
		// - name()    - returns name of enum constant
		// -> better use toString() to get the user-friendly name
		this.aiLevel = (String.valueOf(aiLevel));
	}
	
	public Player getNameConverted(String name) {
		// "Script Joe(Human|Smart) [Elf]"
		
		String[] splitName = StringUtils.split(StringUtils.remove(StringUtils.remove(name, "]"), " ["), "()");
		if (splitName.length != 3 ||
				    splitName[0].isEmpty() || splitName[1].isEmpty() || splitName[2].isEmpty()) {
			Player newPlayer = new Player();
			newPlayer.setAlias("John 'Dto' Doe");
			newPlayer.setAiLevel(AiLevel.HUMAN);
			newPlayer.setHuman(Boolean.TRUE);
			newPlayer.setAvatar(Avatar.ELF);
			return newPlayer;
		}
		
		Player newPlayer = new Player();
		newPlayer.setAlias(splitName[0]);
		newPlayer.setAiLevel(AiLevel.fromLabel(splitName[1]));
		newPlayer.setHuman(aiLevel.equalsIgnoreCase("human") ? Boolean.TRUE : Boolean.FALSE);
		newPlayer.setAvatar(Avatar.fromLabel(splitName[2]));
		return newPlayer;
	}
	
	public void setName() {
		// "Script Joe(Human|Smart) [Elf]"
		this.name = alias + "(" + WordUtils.capitalizeFully(aiLevel) + ") [" + WordUtils.capitalizeFully(avatar) + "]";
	}
	
	public void setWinCount() {
		if (games!=null) {
			this.winCount = games.size();
		} else {
			this.winCount = 0;
		}
	}
	
	public void setHuman(boolean human) {
		this.human = String.valueOf(human);
	}
	
}
