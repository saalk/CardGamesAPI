create table GAMES (GAME_ID bigint not null, ANTE integer, CURRENT_PLAYER integer, CURRENT_ROUND integer, CURRENT_TURN integer, CARDS tinyblob, DEALED_TO tinyblob, MINIMAL_TURNS_BEFORE_PASS integer, MINIMAL_TURNS_TO_WIN integer, ROUNDS integer, TURNS integer, WINNER_ID bigint, primary key (GAME_ID));
create table HANDS (HAND_ID bigint not null, FK_PLAYER_ID bigint, FK_TABLES_ID bigint, primary key (HAND_ID));
create table hibernate_sequence (next_val bigint);
insert into hibernate_sequence values ( 1 );
insert into hibernate_sequence values ( 1 );
insert into hibernate_sequence values ( 1 );
insert into hibernate_sequence values ( 1 );
create table PLAYERS (PLAYER_ID bigint not null, ALIAS varchar(255), CREATED varchar(255), CUBITS integer, HUMAN bit, SECURED_LOAN integer, SEQUENCE integer, primary key (PLAYER_ID));
create table TABLES (TABLE_ID bigint not null, PLAYER_ORDER integer, FK_GAME_ID bigint, FK_HAND_ID bigint, FK_PLAYER_ID bigint, primary key (TABLE_ID));
create index WINNER_ID_INDEX on GAMES (WINNER_ID);
alter table GAMES add constraint FKtg9q8bax7vmlmv4uwbgv1e79p foreign key (WINNER_ID) references PLAYERS (PLAYER_ID);
create index FK_PLAYER_ID_INDEX on HANDS (FK_PLAYER_ID);
create index FK_TABLES_ID_INDEX on HANDS (FK_TABLES_ID);
alter table HANDS add constraint FK1jhk79wxddkhoi1hlar1rw4s5 foreign key (FK_PLAYER_ID) references PLAYERS (PLAYER_ID);
alter table HANDS add constraint FKehkfhpnvblsn18sdo2o65vfo9 foreign key (FK_TABLES_ID) references TABLES (TABLE_ID);
create index FK_GAME_ID_INDEX on TABLES (FK_GAME_ID);
create index FK_PLAYER_ID_INDEX on TABLES (FK_PLAYER_ID);
create index FK_HAND_ID_INDEX on TABLES (FK_HAND_ID);
/*
alter table TABLES add constraint UK8x010rio87eg7qfb62lilrmou unique (FK_GAME_ID, FK_PLAYER_ID, FK_HAND_ID);
alter table TABLES add constraint FKsodqulyexnftlbr8nyu79ihva foreign key (FK_GAME_ID) references GAMES (GAME_ID);
alter table TABLES add constraint FKm96viptaps6dnmly1oxhgjcvh foreign key (FK_HAND_ID) references HANDS (HAND_ID);
alter table TABLES add constraint FKby4cdqq243q7t1ne2jbeafg0i foreign key (FK_PLAYER_ID) references PLAYERS (PLAYER_ID);
*/
