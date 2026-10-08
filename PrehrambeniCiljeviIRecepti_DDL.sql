use PrehrambeniCiljeviIRecepti;

create table korisnik
(
    ID_Korisnika INT auto_increment primary key,
    Ime VARCHAR(50) not null,
    Prezime VARCHAR(50) not null,
    Email VARCHAR(50) not null unique,
    BrojTelefona VARCHAR(20),
    KorisnickoIme VARCHAR(45) unique,
    Lozinka VARCHAR(255) not null,
    Status TINYINT default 1
);

create table klijent
(
    ID_Korisnika INT primary key,
    Starost INT,
    Visina DECIMAL(5,2),
    Tezina DECIMAL(5,2),
    NivoFizickeAktivnosti VARCHAR(45),
    CiljanaTezina DECIMAL(5,2),
    DatumPostizanjaCiljaneTezine DATE,
    foreign key (ID_Korisnika) references korisnik(ID_Korisnika)
);

create table administrator
(
    ID_Korisnika INT primary key,
    foreign key (ID_Korisnika) references korisnik(ID_Korisnika)
);

create table sastojak
(
    ID_Sastojka INT auto_increment primary key,
    Naziv VARCHAR(45) not null,
    Status TINYINT default 1,
    Administrator_ID_Korisnika INT,
    foreign key (Administrator_ID_Korisnika) references administrator(ID_Korisnika)
);

create table recept
(
    ID_Recepta INT auto_increment primary key,
    Naziv VARCHAR(45) not null,
    Opis VARCHAR(50),
    Detaljno_uputstvo TEXT,
    Datum_kreiranja DATETIME,
    Status TINYINT default 1,
    Klijent_ID_Korisnika INT,
    foreign key (Klijent_ID_Korisnika) references klijent(ID_Korisnika)
);

create table recept_sadrzi_sastojak
(
    Sastojak_ID_Sastojka INT,
    Recept_ID_Recepta INT,
    Kolicina DECIMAL(5,2),
    primary key (Sastojak_ID_Sastojka, Recept_ID_Recepta),
    foreign key (Sastojak_ID_Sastojka) references sastojak(ID_Sastojka),
    foreign key (Recept_ID_Recepta) references recept(ID_Recepta)
);

create table komentar
(
    ID_Komentara INT auto_increment primary key,
    Komentar TEXT,
    Datum_objave DATETIME,
    Klijent_ID_Korisnika INT,
    Recept_ID_Recepta INT,
    Komentar_ID_Komentara INT,
    Status TINYINT default 1,
    foreign key (Klijent_ID_Korisnika) references klijent(ID_Korisnika),
    foreign key (Recept_ID_Recepta) references recept(ID_Recepta),
    foreign key (Komentar_ID_Komentara) references komentar(ID_Komentara)
);

create table klijent_prijavljuje_komentar
(
    Klijent_Korisnik_ID_Korisnika INT,
    Komentar_ID_Komentara INT,
    Datum DATETIME,
    Razlog TEXT,
    primary key (Klijent_Korisnik_ID_Korisnika, Komentar_ID_Komentara),
    foreign key (Klijent_Korisnik_ID_Korisnika) references klijent(ID_Korisnika),
    foreign key (Komentar_ID_Komentara) references komentar(ID_Komentara)
);

create table administrator_uklanja_komentar
(
    Komentar_ID_Komentara INT,
    Datum DATETIME,
    Obrazlozenje TEXT,
    Administrator_Korisnik_ID_Korisnika INT,
    primary key (Komentar_ID_Komentara, Datum),
    foreign key (Komentar_ID_Komentara) references komentar(ID_Komentara),
    foreign key (Administrator_Korisnik_ID_Korisnika) references administrator(ID_Korisnika)
);

create table korisnik_ocjenjuje_recept
(
    Recept_ID_Recepta INT,
    Klijent_Korisnik_ID_Korisnika INT,
    Ocjena INT,
    primary key (Recept_ID_Recepta, Klijent_Korisnik_ID_Korisnika),
    foreign key (Recept_ID_Recepta) references recept(ID_Recepta),
    foreign key (Klijent_Korisnik_ID_Korisnika) references klijent(ID_Korisnika),
    check (Ocjena between 1 and 5)
);

create table korisnik_cuva_recept
(
    Recept_ID_Recepta INT,
    Klijent_Korisnik_ID_Korisnika INT,
    primary key (Recept_ID_Recepta, Klijent_Korisnik_ID_Korisnika),
    foreign key (Recept_ID_Recepta) references recept(ID_Recepta),
    foreign key (Klijent_Korisnik_ID_Korisnika) references klijent(ID_Korisnika)
);

create table administrator_uklanja_recept
(
    Recept_ID_Recepta INT,
    Datum DATETIME,
    Obrazlozenje TEXT,
    Administrator_Korisnik_ID_Korisnika INT,
    primary key (Recept_ID_Recepta, Datum),
    foreign key (Recept_ID_Recepta) references recept(ID_Recepta),
    foreign key (Administrator_Korisnik_ID_Korisnika) references administrator(ID_Korisnika)
);

create table administrator_uklanja_klijenta
(
    Klijent_Korisnik_ID_Korisnika INT,
    Datum DATETIME,
    Obrazlozenje TEXT,
    Administrator_Korisnik_ID_Korisnika INT,
    primary key (Klijent_Korisnik_ID_Korisnika, Datum),
    foreign key (Klijent_Korisnik_ID_Korisnika) references klijent(ID_Korisnika),
    foreign key (Administrator_Korisnik_ID_Korisnika) references administrator(ID_Korisnika)
);

create table administrator_uklanja_sastojak
(
    Sastojak_ID_Sastojka INT,
    Datum DATETIME,
    Obrazlozenje TEXT,
    Administrator_Korisnik_ID_Korisnika INT,
    primary key (Sastojak_ID_Sastojka, Datum),
    foreign key (Sastojak_ID_Sastojka) references sastojak(ID_Sastojka),
    foreign key (Administrator_Korisnik_ID_Korisnika) references administrator(ID_Korisnika)
);

create table nutritivni_podaci
(
    Klijent_ID_Korisnika INT,
    Datum DATETIME,
    Kalorije INT,
    Proteini INT,
    Ugljeni_hidrati INT,
    Masti INT,
    primary key (Klijent_ID_Korisnika, Datum),
    foreign key (Klijent_ID_Korisnika) references klijent(ID_Korisnika)
);
create index idx_recept_korisnik on recept (Klijent_ID_Korisnika);
create index idx_nutritivni_podaci on nutritivni_podaci (Klijent_ID_Korisnika);
alter table klijent add column pol ENUM('M','Z');
alter table klijent modify column nivofizickeaktivnosti ENUM('MALO','SREDNJE','PUNO');
alter table nutritivni_podaci modify column datum DATETIME;

delimiter //
create trigger trg_recept_datum
before insert on recept
for each row
begin
    set new.Datum_kreiranja = now();
end//
delimiter ;

delimiter //
create trigger trg_komentar_datum
before insert on komentar
for each row
begin
    set new.Datum_objave = now();
end//
delimiter ;

delimiter //
create procedure dodaj_recept(
    in p_naziv varchar(45),
    in p_opis varchar(50),
    in p_uputstvo text,
    in p_klijent int
)
begin
    insert into recept
    (naziv,opis,detaljno_uputstvo,klijent_id_korisnika)
    values
    (p_naziv,p_opis,p_uputstvo,p_klijent);
    select last_insert_id() as id;
end//
delimiter ;

delimiter //
drop procedure if exists provjeri_aktivnost_korisnika//
create procedure provjeri_aktivnost_korisnika(in p_id_korisnika int)
begin
    declare statusKorisnika int;
    select status
    into statusKorisnika
    from korisnik
    where ID_Korisnika = p_id_korisnika;
    if statusKorisnika = 0 then
        signal sqlstate '45000'
        set message_text =
        'Vas korisnicki nalog je deaktiviran';
    end if;
end//
delimiter ;

delimiter //
create trigger trg_blokiraj_recept
before insert on recept
for each row
begin
	call provjeri_aktivnost_korisnika(new.Klijent_ID_Korisnika);
end//
delimiter ;

delimiter //
create trigger trg_blokiraj_komentar
before insert on komentar
for each row
begin
	call provjeri_aktivnost_korisnika(new.Klijent_ID_Korisnika);
end//
delimiter ;

delimiter //
create trigger trg_blokiraj_prijavu_komentara
before insert on Klijent_Prijavljuje_Komentar
for each row
begin
	call provjeri_aktivnost_korisnika(new.Klijent_Korisnik_ID_Korisnika);
end//
delimiter ;

delimiter //
create trigger trg_blokiraj_izmjenu_podataka
before update on Klijent
for each row
begin
	call provjeri_aktivnost_korisnika(new.ID_Korisnika);
end//
delimiter ;

delimiter //
create trigger trg_blokiraj_izmjenu_podataka_korisnik
before update on Korisnik
for each row
begin
    if old.Ime != new.Ime
    or old.Prezime != new.Prezime
    or old.Email != new.Email
    or old.BrojTelefona != new.BrojTelefona
    or old.KorisnickoIme != new.KorisnickoIme
    or old.Lozinka != new.Lozinka then
        call provjeri_aktivnost_korisnika(old.ID_Korisnika);
    end if;
end//
delimiter ;

delimiter //
create trigger trg_blokiraj_update_recepta
before update on recept
for each row
begin
	call provjeri_aktivnost_korisnika(new.Klijent_ID_Korisnika);
end//
delimiter ;

delimiter //
create trigger trg_blokiraj_ocjena_recepta
before insert on korisnik_ocjenjuje_recept
for each row
begin
	call provjeri_aktivnost_korisnika(new.Klijent_Korisnik_ID_Korisnika);
end//
delimiter ;

delimiter //
create trigger trg_blokiraj_cuvanje_recepta
before insert on korisnik_cuva_recept
for each row
begin
	call provjeri_aktivnost_korisnika(new.Klijent_Korisnik_ID_Korisnika);
end//
delimiter ;

delimiter //
create trigger trg_zabrani_ocjenu_sopstvenog_recepta
before insert on korisnik_ocjenjuje_recept
for each row
begin
    declare vlasnik int;
    select Klijent_ID_Korisnika
    into vlasnik
    from recept
    where ID_Recepta = new.Recept_ID_Recepta;
    if vlasnik = new.Klijent_Korisnik_ID_Korisnika then
        signal sqlstate '45000'
        set message_text =
        'Ne mozete ocijeniti sopstveni recept';
    end if;
end//
delimiter ;

delimiter //
create trigger trg_zabrani_cuvanje_sopstvenog_recepta
before insert on korisnik_cuva_recept
for each row
begin
    declare vlasnik int;
    select Klijent_ID_Korisnika
    into vlasnik
    from recept
    where ID_Recepta = new.Recept_ID_Recepta;
    if vlasnik = new.Klijent_Korisnik_ID_Korisnika then
        signal sqlstate '45000'
        set message_text =
        'Ne mozete sacuvati sopstveni recept';
    end if;
end//
delimiter ;

delimiter //
create procedure provjeri_aktivnost_admina(in p_adminID int)
begin
    declare v_status int;
    select status
    into v_status
    from korisnik
    where id_korisnika = p_adminID;
    if v_status = 0 then
        signal sqlstate '45000'
        set message_text =
            'Administrator je deaktiviran!';
    end if;
end//
delimiter ;

delimiter //
create trigger trg_admin_dodaj_sastojak
before insert on sastojak
for each row
begin
    call provjeri_aktivnost_admina(new.administrator_id_korisnika);
end//
delimiter ;

delimiter //
create trigger trg_admin_uklanja_sastojak
before insert on administrator_uklanja_sastojak
for each row
begin
    call provjeri_aktivnost_admina(new.administrator_korisnik_id_korisnika);
end//
delimiter ;

delimiter //
create trigger trg_admin_uklanja_komentar
before insert on administrator_uklanja_komentar
for each row
begin
    call provjeri_aktivnost_admina(new.administrator_korisnik_id_korisnika);
end//
delimiter ;

delimiter //
create trigger trg_admin_uklanja_recept
before insert on administrator_uklanja_recept
for each row
begin
    call provjeri_aktivnost_admina(new.administrator_korisnik_id_korisnika);
end//
delimiter ;

delimiter //
create trigger trg_admin_uklanja_klijenta
before insert on administrator_uklanja_klijenta
for each row
begin
    call provjeri_aktivnost_admina(new.administrator_korisnik_id_korisnika);
end//
delimiter ;

delimiter //
create procedure provjeri_admin_status(in p_admin_id int)
begin
    declare v_status int;
    select status
    into v_status
    from korisnik
    where id_korisnika = p_admin_id;
    if v_status = 0 then
        signal sqlstate '45000'
        set message_text =
        'Deaktivirani administrator ne moze izvrsavati akcije';
    end if;
end//
delimiter ;

create view pregled_recepata as
select 
    r.id_recepta,
    r.naziv,
    r.opis,
    r.detaljno_uputstvo,
    r.status,
    r.klijent_id_korisnika,
    avg(kor.ocjena) as prosjecna_ocjena,
    group_concat(distinct concat(s.naziv, ' (', rs.kolicina, 'g)')separator ', ') as sastojci
from recept r
left join korisnik_ocjenjuje_recept kor
    on r.id_recepta = kor.recept_id_recepta
left join recept_sadrzi_sastojak rs
    on r.id_recepta = rs.recept_id_recepta
left join sastojak s
    on rs.sastojak_id_sastojka = s.id_sastojka
group by r.id_recepta;
