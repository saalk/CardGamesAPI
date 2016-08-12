cardGamesApp.service('PlayerService', function(){

// TODO: fetch from cookie and server
var players = [
{id: 0, name: 'stranger', aiLevel: 'Human', fiches: '0000'},
{id: 1, name: 'first', aiLevel: 'Average', fiches: '0000'},
{id: 2, name: 'second', aiLevel: 'Dumb', fiches: '0000'}
];

this.savePlayer = function(player){
if (player.id == null) {
// new player
players.push(player);
} else {
// existing player
for (var i in players) {
if (players[i].id == player.id)
players[i] = player;}
}

};

this.getPlayer = function(id){
// existing player
for (var i in players) {
if (players[i].id == id)
return players[i];}
};

this.deletePlayer = function(id){
// existing player
for (var i in players) {
if (players[i].id == id)
players.splice(i, 1);}

}

this.listPlayers = function(){
return players;
}
});

cardGamesApp.service('GameService', function(){

// TODO: fetch from cookie and server
var games = [
{id: 0, name: 'high-low', ante: '50', wonId: 0}
];

this.saveGame = function(game){
if (game.id == null) {
// new player
games.push(game);
} else {
// existing player
for (var i in games) {
if (games[i].id == game.id)
games[i] = game;}
}

};

this.getGame = function(id){
// existing player
for (var i in games) {
if (games[i].id == id)
return games[i];}
};

this.deleteGame = function(id){
// existing player
for (var i in games) {
if (games[i].id == id)
games.splice(i, 1);}

}

this.listGames = function(){
return games;
}
});