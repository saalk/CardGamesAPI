alter table GAMES drop foreign key FKtg9q8bax7vmlmv4uwbgv1e79p;
alter table HANDS drop foreign key FK1jhk79wxddkhoi1hlar1rw4s5;
alter table HANDS drop foreign key FKehkfhpnvblsn18sdo2o65vfo9;
alter table TABLES drop foreign key FKsodqulyexnftlbr8nyu79ihva;
alter table TABLES drop foreign key FKm96viptaps6dnmly1oxhgjcvh;
alter table TABLES drop foreign key FKby4cdqq243q7t1ne2jbeafg0i;
drop table if exists GAMES;
drop table if exists HANDS;
drop table if exists hibernate_sequence;
drop table if exists PLAYERS;
drop table if exists TABLES;
