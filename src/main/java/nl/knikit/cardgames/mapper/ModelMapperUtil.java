package nl.knikit.cardgames.mapper;

import nl.knikit.cardgames.DTO.CardDto;
import nl.knikit.cardgames.DTO.DeckDto;
import nl.knikit.cardgames.DTO.GameDto;
import nl.knikit.cardgames.DTO.PlayerDto;
import nl.knikit.cardgames.model.Card;
import nl.knikit.cardgames.model.Deck;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.Player;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
	
	public GameDto convertToDto(Game game) {
		GameDto gameDto = modelMapper.map(game, GameDto.class);
		modelMapper.addMappings(new GameMapFromEntity()); // customer mapping
		
		gameDto.setName();
		
		return gameDto;
	}
	public Game convertToEntity(GameDto gameDto) throws ParseException {
		Game game = modelMapper.map(gameDto, Game.class);
		//modelMapper.addMappings(new GameMapFromDto()); // customer mapping
		
		return game;
	}
	
	public DeckDto convertToDto(Deck deck) throws Exception {
		DeckDto deckDto = modelMapper.map(deck, DeckDto.class);
		
		deckDto.setName();
		
		return deckDto;
	}
	public Deck convertToEntity(DeckDto deckDto) throws ParseException {
		Deck deck = modelMapper.map(deckDto, Deck.class);
		
		return deck;
	}
	
	public CardDto convertToDto(Card card) throws Exception {
		CardDto cardDto = modelMapper.map(card, CardDto.class);
		
		return cardDto;
	}
	public Card convertToEntity(CardDto cardDto) throws ParseException {
		Card card = modelMapper.map(cardDto, Card.class);
		
		return card;
	}
	
}
