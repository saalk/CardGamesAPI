package nl.knikit.cardgames.mapper;

import nl.knikit.cardgames.DTO.PlayerDto;
import nl.knikit.cardgames.model.Player;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Component
public class ModelMapperUtil{
	
	
	@Autowired
	private ModelMapper modelMapper;
	
	public PlayerDto convertToDto(Player player) {
		PlayerDto playerDto = modelMapper.map(player, PlayerDto.class);
		
		playerDto.setName();
		playerDto.setWinCount();
		
		return playerDto;
	}
	
	public Player convertToEntity(PlayerDto playerDto) throws ParseException {
		Player player = modelMapper.map(playerDto, Player.class);
		
		return player;
	}
}
