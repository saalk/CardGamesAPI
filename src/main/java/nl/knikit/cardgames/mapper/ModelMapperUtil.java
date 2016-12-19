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
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ModelMapperUtil {
	
	public PlayerDto convertToDto(Player player) {
		ModelMapper modelMapper = new ModelMapper();
		PlayerDto playerDto = modelMapper.map(player, PlayerDto.class);
		
		List<GameDto> gameDtos = new ArrayList<>();
		if (player.getGames() != null) {
			for (Game game: player.getGames()) {
				// gameDtos.add(convertToDto(game)); this created a loop
				modelMapper = new ModelMapper();
				GameDto gameDto = modelMapper.map(game, GameDto.class);
				modelMapper.addMappings(new GameMapFromEntity()); // customer mapping
				gameDto.setName();
				gameDtos.add(gameDto);
			}
			playerDto.setGameDtos(gameDtos);
		} else {
			playerDto.setGameDtos(null);
		}
		
		playerDto.setName();
		playerDto.setWinCount();
		
		return playerDto;
	}
	
	public Player convertToEntity(PlayerDto playerDto) throws ParseException {
		ModelMapper modelMapper = new ModelMapper();
		Player player = modelMapper.map(playerDto, Player.class);
		
		player.setGames(null);
		
		return player;
	}
	
	public GameDto convertToDto(Game game) {
		ModelMapper modelMapper = new ModelMapper();
		GameDto gameDto = modelMapper.map(game, GameDto.class);
		modelMapper.addMappings(new GameMapFromEntity()); // customer mapping
		
		gameDto.setName();
		if (game.getPlayer() != null) {
			// gameDto.setWinner(convertToDto(game.getPlayer())); // this created a loop...
			modelMapper = new ModelMapper();
			PlayerDto playerDto = modelMapper.map(game.getPlayer(), PlayerDto.class);
			playerDto.setGameDtos(null);
			playerDto.setName();
			playerDto.setWinCount();
			gameDto.setWinner(playerDto);
		} else {
			gameDto.setWinner(null);
		}
		return gameDto;
	}
	
	public Game convertToEntity(GameDto gameDto) throws ParseException {
		ModelMapper modelMapper = new ModelMapper();
		Game game = modelMapper.map(gameDto, Game.class);
		modelMapper.addMappings(new GameMapFromDto()); // customer mapping
		if (gameDto.getWinner() != null) {
			
			// game.setPlayer(convertToEntity(gameDto.getWinner())); // this creates a loop ..
			modelMapper = new ModelMapper();
			game.setPlayer(modelMapper.map(gameDto.getWinner(), Player.class));
		} else {
			game.setPlayer(null);
		}
		return game;
	}
	
	public DeckDto convertToDto(Deck deck) throws Exception {
		ModelMapper modelMapper = new ModelMapper();
		DeckDto deckDto = modelMapper.map(deck, DeckDto.class);
		
		if (deck.getDealtTo() != null) {
			deckDto.setDealtToDto(convertToDto(deck.getDealtTo())); // does this create a loop ?
			//deckDto.setDealtToDto(modelMapper.map(deck.getDealtTo(), PlayerDto.class));
		} else {
			deckDto.setDealtToDto(null);
		}
		if (deck.getGame() != null) {
			deckDto.setGameDto(convertToDto(deck.getGame()));
		} else {
			deckDto.setGameDto(null);
		}
		if (deck.getCard() != null) {
			deckDto.setCardDto(convertToDto(deck.getCard()));
		} else {
			deckDto.setCardDto(null);
		}
		
		deckDto.setName();
		return deckDto;
	}
	
	public Deck convertToEntity(DeckDto deckDto) throws ParseException {
		ModelMapper modelMapper = new ModelMapper();
		Deck deck = modelMapper.map(deckDto, Deck.class);
		
		if (deckDto.getDealtToDto() != null) {
			deck.setDealtTo(convertToEntity(deckDto.getDealtToDto())); // this creates a loop ..
		} else {
			deck.setDealtTo(null);
		}
		if (deckDto.getGameDto() != null) {
			deck.setGame(convertToEntity(deckDto.getGameDto())); // this creates a loop ..
		} else {
			deck.setGame(null);
		}
		if (deckDto.getCardDto() != null) {
			deck.setCard(convertToEntity(deckDto.getCardDto())); // this creates a loop ..
		} else {
			deck.setCard(null);
		}
		
		deck.setDealtTo(convertToEntity(deckDto.getDealtToDto())); // this creates a loop ..
		deck.setGame(convertToEntity(deckDto.getGameDto())); // this creates a loop ..
		deck.setCard(convertToEntity(deckDto.getCardDto())); // this creates a loop ..
		
		return deck;
	}
	
	public CardDto convertToDto(Card card) throws Exception {
		ModelMapper modelMapper = new ModelMapper();
		
		return modelMapper.map(card, CardDto.class);
	}
	
	public Card convertToEntity(CardDto cardDto) throws ParseException {
		ModelMapper modelMapper = new ModelMapper();
		
		return modelMapper.map(cardDto, Card.class);
	}
	
}
