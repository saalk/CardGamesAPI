package nl.knikit.cardgames.testdata;


import nl.knikit.cardgames.DTO.CardDto;
import nl.knikit.cardgames.DTO.CasinoDto;
import nl.knikit.cardgames.DTO.DeckDto;
import nl.knikit.cardgames.DTO.GameDto;
import nl.knikit.cardgames.DTO.PlayerDto;
import nl.knikit.cardgames.model.AiLevel;
import nl.knikit.cardgames.model.Avatar;
import nl.knikit.cardgames.model.Card;
import nl.knikit.cardgames.model.CardLocation;
import nl.knikit.cardgames.model.Casino;
import nl.knikit.cardgames.model.Deck;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.GameType;
import nl.knikit.cardgames.model.Player;
import nl.knikit.cardgames.model.Rank;
import nl.knikit.cardgames.model.Suit;
import nl.knikit.cardgames.model.state.CardGameStateMachine;

import java.util.ArrayList;
import java.util.List;

public class TestData {
	
	protected static Player MakePlayerEntityWithIdAndGamesWon(int id, int gamesWon) {
		
		Player playerFixture = new Player();
		
		// a player having 9 fields
		playerFixture.setPlayerId(id);
		playerFixture.setCreated("1");
		playerFixture.setAlias("John 'Test' Doe");
		playerFixture.setHuman(true);
		playerFixture.setAiLevel(AiLevel.HUMAN);
		playerFixture.setAvatar(Avatar.ELF);
		playerFixture.setCubits(1);
		playerFixture.setSecuredLoan(1);
		ArrayList<Game> games = new ArrayList<>();
		if (gamesWon>0) {
			games.add(MakeGameEntityWithIdAndDecksAndWinner(id+1,0,0));
			games.add(MakeGameEntityWithIdAndDecksAndWinner(id+2,0,0));
			games.add(MakeGameEntityWithIdAndDecksAndWinner(id+3,0,0));
			playerFixture.setGames(games);
		} else {
			playerFixture.setGames(null);
		}
		
		return playerFixture;
	}
	
	protected static PlayerDto MakePlayerDtoWithIdAndGamesWon(int id, int gamesWon) throws Exception {
		
		PlayerDto playerDtoFixture = new PlayerDto();
		
		// a playerDto having 9 + 2 fields
		playerDtoFixture.setPlayerId(id);
		playerDtoFixture.setAlias("John 'DtoTest' Doe");
		playerDtoFixture.setHuman(false);
		playerDtoFixture.setAiLevel(AiLevel.DUMB);
		playerDtoFixture.setAvatar(Avatar.GOBLIN);
		playerDtoFixture.setName(); // extra field "Script Joe(Human|Smart) [Elf]"
		playerDtoFixture.setCubits(2);
		playerDtoFixture.setSecuredLoan(2);
		
		ArrayList<GameDto> gamesDto;
		if (gamesWon>0) {
			gamesDto = new ArrayList<>();
			gamesDto.add(MakeGameDtoWithIdAndDecksAndWinner(id+1,0,0));
			gamesDto.add(MakeGameDtoWithIdAndDecksAndWinner(id+2,0,0));
			gamesDto.add(MakeGameDtoWithIdAndDecksAndWinner(id+3,0,0));
			playerDtoFixture.setGameDtos(gamesDto);
		} else {
			playerDtoFixture.setGameDtos(null);
		}
		playerDtoFixture.setWinCount();  // extra field
		
		return playerDtoFixture;
	}
	
	protected static Game MakeGameEntityWithIdAndDecksAndWinner(int id, int decks, int winner) {
		
		Game gameFixture = new Game();
		
		// Given a gameDto having 14 fields
		gameFixture.setGameId(id);
		gameFixture.setCreated("1");
		gameFixture.setState(CardGameStateMachine.State.IS_SHUFFLED);
		gameFixture.setGameType(GameType.HIGHLOW);
		gameFixture.setAnte(50);
		gameFixture.setCurrentRound(1);
		if (winner!=0) {
			gameFixture.setPlayer(MakePlayerEntityWithIdAndGamesWon(winner, 0));
		} else {
			gameFixture.setPlayer(null);
		}
		List<Deck> decksList = new ArrayList<>();
		if (decks!=0) {
			decksList.add(MakeDeckEntityWithIdAndDealtTo(decks,1));
			decksList.add(MakeDeckEntityWithIdAndDealtTo(decks+1,2));
			decksList.add(MakeDeckEntityWithIdAndDealtTo(decks+2,3));
			decksList.add(MakeDeckEntityWithIdAndDealtTo(decks+3,0));
			gameFixture.setDecks(decksList);
		} else {
			gameFixture.setDecks(null);
		}
		
		return gameFixture;
	}
	
	protected static GameDto MakeGameDtoWithIdAndDecksAndWinner(int id, int decksDto, int winner) throws Exception {
		
		GameDto gameDtoFixture = new GameDto();
		
		// Given a gameDto having 14 + 3 fields
		gameDtoFixture.setGameId(id);
		gameDtoFixture.setState(CardGameStateMachine.State.TURN_STARTED);
		gameDtoFixture.setGameType(GameType.BLACKJACK);
		gameDtoFixture.setAnte(100);
		gameDtoFixture.setName(); // extra fields "Highlow:0005 (Ante:100) [GameSelected]"
		gameDtoFixture.setCardsLeft(); // extra fields "Highlow:0005 (Ante:100) [GameSelected]"
		gameDtoFixture.setCardsDealt(); // extra fields "Highlow:0005 (Ante:100) [GameSelected]"
		gameDtoFixture.setCurrentRound(3);
		if (winner!=0) {
			gameDtoFixture.setWinner(MakePlayerDtoWithIdAndGamesWon(winner, 0));
		} else {
			gameDtoFixture.setWinner(null);
		}
		ArrayList<DeckDto> decksDtoList = new ArrayList<>();
		if (decksDto>0) {
			decksDtoList.add(MakeDeckDtoWithId(decksDto));
			decksDtoList.add(MakeDeckDtoWithId(decksDto+1));
			decksDtoList.add(MakeDeckDtoWithId(decksDto+2));
			gameDtoFixture.setDeckDtos(decksDtoList); // "10C  Ten of Clubs"
		} else {
			gameDtoFixture.setDeckDtos(null);
		}
		
		return gameDtoFixture;
	}
	
	protected static Card MakeCardEntityWithId(String id) {
		
		// Given a cardDto having 4 fields
		Card cardFixture = new Card();
		cardFixture.setCardId(id);
		cardFixture.setRank(Rank.FOUR);
		cardFixture.setSuit(Suit.HEARTS);
		cardFixture.setValue(1);
		
		return cardFixture;
	}
	
	protected static CardDto MakeCardDtoWithId(String id) {
		
		// Given a cardDto having 4 fields
		CardDto cardDtoFixture = new CardDto();
		cardDtoFixture.setCardId(id);
		cardDtoFixture.setRank(Rank.FIVE);
		cardDtoFixture.setValue(2);
		cardDtoFixture.setSuit(Suit.CLUBS);
		
		return cardDtoFixture;
	}
	
	protected static Deck MakeDeckEntityWithIdAndDealtTo(int id, int dealtTo) {
		
		// Given a deck having 5 fields
		Deck deckFixture = new Deck();
		deckFixture.setDeckId(id);
		deckFixture.setGame(MakeGameEntityWithIdAndDecksAndWinner(id+1,0, 0));
		deckFixture.setCard(MakeCardEntityWithId("10C"));
		deckFixture.setDealtTo(MakeCasinoEntityWithIdAndDealtTo(5,2));
		
		deckFixture.setCardOrder(6);
		
		return deckFixture;
	}
	
	protected static DeckDto MakeDeckDtoWithId(int id) throws Exception {
		
		DeckDto deckDtoFixture = new DeckDto();
		deckDtoFixture.setDeckId(id);
		// Given a deckDto having 5 + 1 fields
		deckDtoFixture.setGameDto(MakeGameDtoWithIdAndDecksAndWinner(id+1,0,0));
		deckDtoFixture.setCardDto(MakeCardDtoWithId("KH"));
		deckDtoFixture.setDealtToDto(MakeCasinoDtoWithId(id+2));
		deckDtoFixture.setCardOrder(12);
		deckDtoFixture.setCardLocation(CardLocation.STACK);
		deckDtoFixture.setName(); // extra field "(03) 10C  John 'DeckDtoTest' Doe [Medium]"
		
		return deckDtoFixture;
	}
	
	protected static Casino MakeCasinoEntityWithIdAndDealtTo(int id, int dealtTo) {
		
		// Given a casino having 5 fields
		Casino casinoFixture = new Casino();
		casinoFixture.setCasinoId(id);
		casinoFixture.setGame(MakeGameEntityWithIdAndDecksAndWinner(id+1,0, 0));
		casinoFixture.setHands(null);
		
		if (dealtTo>0) {
			casinoFixture.setPlayer(MakePlayerEntityWithIdAndGamesWon(dealtTo, 1));
		} else {
			casinoFixture.setPlayer(null);
		}
		casinoFixture.setPlayingOrder(6);
		
		return casinoFixture;
	}
	
	protected static CasinoDto MakeCasinoDtoWithId(int id) throws Exception {
		
		CasinoDto casinoDtoFixture = new CasinoDto();
		casinoDtoFixture.setCasinoId(id);
		casinoDtoFixture.setPlayerDto(MakePlayerDtoWithIdAndGamesWon(id+2,0));
		casinoDtoFixture.setPlayingOrder(12);
		// Given a casinoDto having 5 + 1 fields
		casinoDtoFixture.setGameDto(MakeGameDtoWithIdAndDecksAndWinner(id+1,0,0));
		casinoDtoFixture.setHandDtos(null);
		casinoDtoFixture.setName(); // extra field "(03) 10C  John 'CasinoDtoTest' Doe [Medium]"
		
		return casinoDtoFixture;
	}

}
	
	
