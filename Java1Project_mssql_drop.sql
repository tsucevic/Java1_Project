use Java1Project
go
drop index if exists Roles_AccessLevel_uindex on Roles
drop index if exists Users_Username_uindex on Users
drop table if exists Users
drop table if exists Roles
drop table if exists MovieActor
drop table if exists MovieDirector
drop table if exists Actors
drop table if exists Directors
drop table if exists People
drop table if exists Movie
go
use master
go
drop database if exists Java1Project