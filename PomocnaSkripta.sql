select * from korisnik_cuva_recept;
select count(*) from korisnik_cuva_recept where Recept_ID_Recepta = 1 and Klijent_Korisnik_ID_Korisnika = 7;
SELECT * from korisnik_ocjenjuje_recept;
select * from komentar;
select * from recept;
select * from korisnik;
select * from klijent;
select * from sastojak;
select * from korisnik where ID_korisnika ='7';
insert into sastojak
(naziv,kalorije,proteini,ugljeni_hidrati,masti)
values
('sastojak2',123,23,45,12);
select * from recept_sadrzi_sastojak;
select * from nutritivni_podaci;
SELECT * FROM klijent_prijavljuje_komentar;
insert into korisnik
(ime, prezime, email, brojtelefona, korisnickoime, lozinka)
values
('Admin', 'Admin', 'admin@gmail.com', '065000000', 'admin', 'admin123');
insert into administrator (id_korisnika)
values (last_insert_id());
select * from administrator;
select * from administrator_uklanja_recept;
select * from administrator_uklanja_komentar;
select * from administrator_uklanja_sastojak;
update korisnik 
set status = 0
where ID_korisnika = 15;
update sastojak 
set status = 0
where ID_sastojka = 6;
update recept 
set status = 0
where ID_recepta = 8;