-- Dummy SQL script: create LIVRE table and insert sample data
CREATE TABLE LIVRE (
    id INTEGER PRIMARY KEY,
    isbn VARCHAR(32),
    titre VARCHAR(255),
    auteur VARCHAR(255),
    annee INTEGER,
    total_copies INTEGER,
    available_copies INTEGER
);

-- Sample data (5 books)
INSERT INTO LIVRE (id, isbn, titre, auteur, annee, total_copies, available_copies) VALUES (1, '9780140449136', 'The Odyssey', 'Homer', 1999, 5, 3);
INSERT INTO LIVRE (id, isbn, titre, auteur, annee, total_copies, available_copies) VALUES (2, '9780140449181', 'Meditations', 'Marcus Aurelius', 2006, 2, 0);
INSERT INTO LIVRE (id, isbn, titre, auteur, annee, total_copies, available_copies) VALUES (3, '9780261103573', 'The Hobbit', 'J.R.R. Tolkien', 1999, 4, 2);
INSERT INTO LIVRE (id, isbn, titre, auteur, annee, total_copies, available_copies) VALUES (4, '9780307277671', 'The Road', 'Cormac McCarthy', 2006, 3, 1);
INSERT INTO LIVRE (id, isbn, titre, auteur, annee, total_copies, available_copies) VALUES (5, '9780307474278', 'The Great Gatsby', 'F. Scott Fitzgerald', 2004, 6, 6);

-- Note: This script is provided for DAO implementation/testing only. Do not run from UI code.
