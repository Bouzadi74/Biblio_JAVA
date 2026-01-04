-- Create livres table
CREATE TABLE livres (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(20) UNIQUE NOT NULL,
    titre VARCHAR(255) NOT NULL,
    auteur VARCHAR(255) NOT NULL,
    annee_publication INT NOT NULL,
    disponible BOOLEAN DEFAULT TRUE
);

-- Insert test data
INSERT INTO livres (isbn, titre, auteur, annee_publication, disponible) VALUES
('978-2-07-061897-0', 'Le Seigneur des Anneaux : La Communauté de l''Anneau', 'J.R.R. Tolkien', 1954, TRUE),
('978-2-07-068256-7', 'Le Seigneur des Anneaux : Les Deux Tours', 'J.R.R. Tolkien', 1954, FALSE),
('978-2-07-068257-4', 'Le Seigneur des Anneaux : Le Retour du Roi', 'J.R.R. Tolkien', 1955, TRUE),
('978-2-253-16996-6', 'Harry Potter et la Pierre Philosophale', 'J.K. Rowling', 1997, TRUE);
