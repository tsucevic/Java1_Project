-- region Database setup
use master
go
drop database if exists Java1Project
go
create database Java1Project
go
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
drop table if exists Movies
go
-- endregion
-- region Content tables
CREATE TABLE [People] (
	IDPerson int NOT NULL identity CONSTRAINT [PK_PEOPLE] PRIMARY KEY nonCLUSTERED,
	FullName nvarchar(128) NOT NULL

)
GO
CREATE TABLE [Actors] (
	IDActor int NOT NULL identity CONSTRAINT [PK_ACTORS] PRIMARY KEY nonCLUSTERED,
	PersonID int NOT NULL ,
	Alias nvarchar(128),
  constraint FK_PersonActor foreign key (PersonID)
	references People

)
GO
CREATE TABLE [Directors] (
	IDDirector int NOT NULL identity CONSTRAINT [PK_DIRECTORS] PRIMARY KEY nonCLUSTERED,
	PersonID int NOT NULL,
	Alias nvarchar(128),
    constraint FK_PersonDirector foreign key (PersonID)
	references People
)
GO
CREATE TABLE [Movies] (
	IDMovie int NOT NULL identity CONSTRAINT [PK_MOVIES] PRIMARY KEY nonCLUSTERED,
	Title nvarchar(256) NOT NULL,
	OriginalTitle nvarchar(256),
	DescriptionHTML nvarchar(4000),
	Length int,
	Genre nvarchar(64),
	PosterLink nvarchar(512),
	TrailerLink nvarchar(512),
	Link nvarchar(512),
	GUID nvarchar(512) UNIQUE,
	StartsPlaying nvarchar(512),

)
GO
CREATE TABLE [MovieActor] (
	MovieID int NOT NULL,
	ActorID int NOT NULL,
	constraint FK_MovieActor_Movies foreign key (MovieID)
	    references Movies,
    constraint FK_MovieActor_Actors foreign key (ActorID)
        references Actors
)
GO
CREATE TABLE [MovieDirector] (
	MovieID int NOT NULL,
	DirectorID int NOT NULL,
	constraint FK_MovieDirector_Movies foreign key (MovieID)
	    references Movies,
    constraint FK_MovieDirector_Actors foreign key (DirectorID)
        references Directors
)
GO
-- endregion

-- region User database
CREATE TABLE [Roles] (
	IDRole int NOT NULL identity CONSTRAINT [PK_ROLES] PRIMARY KEY nonCLUSTERED,
	Title nvarchar(64) NOT NULL UNIQUE,
	AccessLevel int NOT NULL
)
GO

create unique index Roles_AccessLevel_uindex
	on Roles(AccessLevel)

go
CREATE TABLE [Users] (
	IDUser int NOT NULL identity CONSTRAINT [PK_USERS] PRIMARY KEY nonCLUSTERED,
	RoleID int NOT NULL,
	Username nvarchar(16) NOT NULL UNIQUE,
	Password nvarchar(128) NOT NULL,
    constraint FK_UsersRoles foreign key (RoleID)
        references Roles
)
GO

create unique index Users_Username_uindex
	on Users(Username)

go
-- endregion

-- region Init Procedure
drop proc if exists proc_init_default_settings
go
create proc proc_init_default_settings
as
    begin
        if not exists(select * from Roles where Title = 'Admin' or Title = 'User')
            begin
                insert into Roles
                values ('Admin', 100),
                       ('User', 50)
            end
        if not exists(select * from Users where Username = 'Admin')
            begin
                insert into Users
                values ((select IDRole from Roles where Title = 'Admin'), 'Admin', '1234')
            end
    end
go
-- endregion
-- region Reset to default Procedure
drop proc if exists proc_reset_to_default
go
create proc proc_reset_to_default
as
    begin
        -- datagrip was being dumb
        delete from Users where null is null
        delete from Roles where null is null
        delete from MovieActor where null is null
        delete from MovieDirector where null is null
        delete from Actors where null is null
        delete from Movies where null is null
        delete from Directors where null is null
        delete from People where null is null
        exec proc_init_default_settings
    end
go
-- endregion

-- region Admin create and get user
drop proc if exists proc_create_user
go
create proc proc_create_user
    @Username nvarchar(16),
    @Password nvarchar(128),
    @UserID int output
as
    begin
        insert into Users
        values (@Username,@Password,(select top 1 IDRole from Roles where Title = 'User'))
        set @UserID = scope_identity()
    end
go
drop proc if exists proc_get_user
go
create proc proc_get_user @Username nvarchar(16)
as
    begin
        select IDUser, Username, Password, Title as Role, AccessLevel
        from Users U
            inner join Roles R2 on R2.IDRole = U.RoleID
        where Username like @Username
    end
go
-- endregion

-- region Movies CRUD
drop proc if exists proc_create_movie
go
create proc proc_create_movie
    @Title nvarchar(256),
    @OriginalTitle nvarchar(256),
    @DescriptionHTML nvarchar(4000),
    @Length int,
    @Genre nvarchar(64),
    @PosterLink nvarchar(512),
    @TrailerLink nvarchar(512),
    @Link nvarchar(512),
    @GUID nvarchar(512),
    @StartsPlaying date,
    @MovieID int output
as
    begin
        insert into Movies
        values (@Title,
                @OriginalTitle,
                @DescriptionHTML,
                @Length,
                @Genre,
                @PosterLink,
                @TrailerLink,
                @Link,
                @GUID,
                @StartsPlaying)
        set @MovieID = scope_identity()
    end
go
drop proc if exists proc_read_movie
go
create proc proc_read_movie @IDMovie int
as
    begin
        select IDMovie,
               Title,
               OriginalTitle,
               DescriptionHTML,
               Length,
               Genre,
               PosterLink,
               TrailerLink,
               Link,
               GUID,
               StartsPlaying
        from Movies
        where IDMovie = @IDMovie
    end
go
drop proc if exists proc_read_movies
go
create proc proc_read_movies
as
    begin
        select IDMovie,
               Title,
               OriginalTitle,
               DescriptionHTML,
               Length,
               Genre,
               PosterLink,
               TrailerLink,
               Link,
               GUID,
               StartsPlaying
        from Movies
    end
go
drop procedure if exists proc_update_movie
go
create proc proc_update_movie
    @IDMovie int,
    @Title nvarchar(256),
    @OriginalTitle nvarchar(256),
    @DescriptionHTML nvarchar(4000),
    @Length int,
    @Genre nvarchar(64),
    @PosterLink nvarchar(512),
    @TrailerLink nvarchar(512),
    @Link nvarchar(512),
    @GUID nvarchar(512),
    @StartsPlaying date
as
    begin
        update Movies
            set Title           = @Title,
                OriginalTitle   = @OriginalTitle,
                DescriptionHTML = @DescriptionHTML,
                Length          = @Length,
                Genre           = @Genre,
                PosterLink      = @PosterLink,
                TrailerLink     = @TrailerLink,
                Link            = @Link,
                GUID            = @GUID,
                StartsPlaying   = @StartsPlaying
        where IDMovie = @IDMovie
    end
go
drop procedure if exists proc_delete_movie
go
create proc proc_delete_movie @IDMovie int
as
    begin
        delete from Movies where IDMovie = @IDMovie
    end
go
-- endregion

-- region Actor CRUD
drop procedure if exists proc_create_actor
go
create proc proc_create_actor
    @Name nvarchar(128),
    @ActorID int output
as
    begin
        if exists(select IDPerson
                  from People
                  where FullName = @Name)
            begin
                declare @PersonID int = (select IDPerson
                                         from People
                                         where FullName = @Name)
                if exists(select IDActor from Actors where PersonID = @PersonID)
                    begin
                        set @ActorID = (select IDActor from Actors where PersonID = @PersonID)
                    end
                else
                    begin
                        insert into Actors (PersonID)
                        values (@PersonID)
                        set @ActorID = scope_identity()
                    end
            end
        else
            begin
                insert into People
                values (@Name)
                insert into Actors (PersonID)
                values (scope_identity())
                set @ActorID = scope_identity()
            end

    end
go
drop procedure if exists proc_read_actor
go
create proc proc_read_actor @IDActor int
as
    begin
        select A.*, P.FullName
        from Actors A
                 inner join People P on P.IDPerson = A.PersonID
        where IDActor = @IDActor
    end
go
drop procedure if exists proc_read_actors
go
create proc proc_read_actors
as
    begin
        select A.*, P.FullName
        from People P
                 inner join Actors A on P.IDPerson = A.PersonID
    end
go
drop procedure if exists proc_update_actor
go
create proc proc_update_actor
    @IDActor int,
    @FullName nvarchar(128),
    @Alias nvarchar(128) = null
as
    begin
        update People
        set FullName = @FullName
        where IDPerson = (select PersonID from Actors where IDActor = @IDActor)
        if @Alias not like null
            begin
                update Actors
                set Alias = @Alias
                where IDActor = @IDActor
            end
    end
go
drop procedure if exists proc_delete_actor
go
create proc proc_delete_actor @IDActor int
as
    begin
        delete
        from MovieActor
        where ActorID = @IDActor
        delete
        from Actors
        where IDActor = @IDActor
    end
go
-- endregion

-- region Director CRUD
drop procedure if exists proc_create_director
go
create proc proc_create_director
    @Name nvarchar(128),
    @DirectorID int output
as
    begin
        if exists(select IDPerson
                  from People
                  where FullName = @Name)
            begin
                declare @PersonID int = (select IDPerson
                                         from People
                                         where FullName = @Name)
                if exists(select IDDirector from Directors where PersonID = @PersonID)
                    begin
                        set @DirectorID = (select IDDirector from Directors where PersonID = @PersonID)
                    end
                else
                    begin
                        insert into Directors (PersonID)
                        values (@PersonID)
                        set @DirectorID = scope_identity()
                    end
            end
        else
            begin
                insert into People
                values (@Name)
                insert into Directors (PersonID)
                values (scope_identity())
                set @DirectorID = scope_identity()
            end

    end
go
drop procedure if exists proc_read_director
go
create proc proc_read_director @IDDirector int
as
    begin
        select D.*, P.FullName
        from Directors D
                 inner join People P on D.PersonID = P.IDPerson
        where IDDirector = @IDDirector
    end
go
drop procedure if exists proc_read_directors
go
create proc proc_read_directors
as
    begin
        select D.*, P.FullName
        from People P
                 inner join Directors D on P.IDPerson = D.PersonID
    end
go
drop procedure if exists proc_update_director
go
create proc proc_update_director
    @IDDirector int,
    @FullName nvarchar(128),
    @Alias nvarchar(128)
as
    begin
        update People
        set FullName = @FullName
        where IDPerson = (select PersonID from Directors where IDDirector = @IDDirector)
        if @Alias not like null
            begin
                update Directors
                set Alias = @Alias
                where IDDirector = @IDDirector
            end
    end
go
drop procedure if exists proc_delete_director
go
create proc proc_delete_director @IDDirector int
as
    begin
        delete
        from MovieDirector
        where DirectorID = @IDDirector
        delete
        from Directors
        where IDDirector = @IDDirector
    end
go
-- endregion

-- region Movie Actor m2m CRD
drop procedure if exists proc_create_movie_actor
go
create proc proc_create_movie_actor
    @MovieID int,
    @ActorID int
as
    begin
        insert into MovieActor
        values (@MovieID, @ActorID)
    end
go
drop procedure if exists proc_read_movie_actor
go
create proc proc_read_movie_actor @MovieID int
as
    begin
        select A.*, P.FullName
        from Actors A
                 inner join People P on P.IDPerson = A.PersonID
                 inner join MovieActor MA on A.IDActor = MA.ActorID
        where MovieID = @MovieID
    end
go
drop procedure if exists proc_delete_movie_actor
go
create proc proc_delete_movie_actor
    @MovieID int,
    @ActorID int
as
    begin
        delete
        from MovieActor
        where MovieID = @MovieID
          and ActorID = @ActorID
    end
go
-- endregion

-- region Movie Director m2m CRD
drop procedure if exists proc_create_movie_director
go
create proc proc_create_movie_director
    @MovieID int,
    @DirectorID int
as
    begin
        insert into MovieDirector
        values (@MovieID, @DirectorID)
    end
go
drop procedure if exists proc_read_movie_director
go
create proc proc_read_movie_director @MovieID int
as
    begin
        select A.*, P.FullName
        from Directors A
                 inner join People P on P.IDPerson = A.PersonID
                 inner join MovieDirector MA on A.IDDirector = MA.DirectorID
        where MovieID = @MovieID
    end
go
drop procedure if exists proc_delete_movie_director
go
create proc proc_delete_movie_director
    @MovieID int,
    @DirectorID int
as
    begin
        delete
        from MovieDirector
        where MovieID = @MovieID
          and DirectorID = @DirectorID
    end
go
-- endregion

-- region Initialization
exec proc_init_default_settings
-- endregion


-- region Run this to reset database
/*
go
use Java1Project
go
execute proc_reset_to_default
go
*/
-- endregion