-- Script de création de la table livres
CREATE TABLE IF NOT EXISTS livres (
    isbn VARCHAR(20) PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    auteur VARCHAR(255) NOT NULL,
    annee_publication INT,
    disponible BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Données de test
INSERT INTO livres (isbn, titre, auteur, annee_publication, disponible) VALUES
('978-2-07-074192-6', 'Les Misérables', 'Victor Hugo', 1862, TRUE),
('978-2-07-036822-8', 'Le Seigneur des Anneaux', 'J.R.R. Tolkien', 1954, TRUE),
('978-2-07-044819-0', 'Harry Potter à l''école des sorciers', 'J.K. Rowling', 1997, TRUE);
