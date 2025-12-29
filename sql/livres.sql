<<<<<<< HEAD
-- ============================================================================
-- Script SQL pour la gestion des Livres en Bibliothèque
-- Base de données: bibliotheque
-- Table: livres
-- ============================================================================

-- Créer la base de données (si elle n'existe pas)
CREATE DATABASE IF NOT EXISTS bibliotheque 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE bibliotheque;

-- Supprimer la table si elle existe (pour un nettoyage)
DROP TABLE IF EXISTS livres;

-- Créer la table des livres
CREATE TABLE livres (
    isbn VARCHAR(20) PRIMARY KEY COMMENT 'Identifiant unique du livre (ISBN)',
    titre VARCHAR(255) NOT NULL COMMENT 'Titre du livre',
    auteur VARCHAR(255) NOT NULL COMMENT 'Auteur du livre',
    annee_publication INT NOT NULL COMMENT 'Année de publication',
    disponible BOOLEAN DEFAULT true COMMENT 'Disponibilité du livre (true = disponible)',
    date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Date d''ajout du livre',
    INDEX idx_titre (titre) COMMENT 'Index sur le titre pour les recherches',
    INDEX idx_auteur (auteur) COMMENT 'Index sur l''auteur pour les recherches',
    INDEX idx_disponible (disponible) COMMENT 'Index sur la disponibilité'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Table contenant tous les livres de la bibliothèque';

-- ============================================================================
-- Données de test
-- ============================================================================

INSERT INTO livres (isbn, titre, auteur, annee_publication, disponible) VALUES
('978-2-07-061897-0', 'Le Seigneur des Anneaux : La Communauté de l''Anneau', 'J.R.R. Tolkien', 1954, true),
('978-2-07-068256-7', 'Le Seigneur des Anneaux : Les Deux Tours', 'J.R.R. Tolkien', 1954, false),
('978-2-07-068257-4', 'Le Seigneur des Anneaux : Le Retour du Roi', 'J.R.R. Tolkien', 1955, true),
('978-2-253-16996-6', 'Harry Potter et la Pierre Philosophale', 'J.K. Rowling', 1997, true),
('978-2-253-17004-7', 'Harry Potter et la Chambre des Secrets', 'J.K. Rowling', 1998, false),
('978-2-253-17109-9', 'Harry Potter et le Prisonnier d''Azkaban', 'J.K. Rowling', 1999, true),
('978-2-226-07128-8', 'Le Hobbit', 'J.R.R. Tolkien', 1937, true),
('978-2-263-03237-9', '1984', 'George Orwell', 1949, true),
('978-2-07-043927-2', 'Fondation', 'Isaac Asimov', 1951, false),
('978-2-253-04977-7', 'Le Comte de Monte-Cristo', 'Alexandre Dumas', 1844, true),
('978-2-07-036822-0', 'Les Misérables', 'Victor Hugo', 1862, true),
('978-2-07-089618-9', 'Orgueils et Préjugés', 'Jane Austen', 1813, true),
('978-2-226-16068-4', 'Dune', 'Frank Herbert', 1965, false),
('978-2-07-071357-7', 'Neuromancien', 'William Gibson', 1984, true),
('978-2-07-064513-7', 'L''Iliade', 'Homère', -800, true);

-- ============================================================================
-- Vérification des données
-- ============================================================================

-- Vérifier le nombre de livres
SELECT COUNT(*) AS nombre_total_livres FROM livres;

-- Vérifier les livres disponibles
SELECT COUNT(*) AS nombre_disponibles FROM livres WHERE disponible = true;

-- Vérifier les livres empruntés
SELECT COUNT(*) AS nombre_empruntes FROM livres WHERE disponible = false;

-- Afficher tous les livres (classement par titre)
SELECT isbn, titre, auteur, annee_publication, 
       IF(disponible = true, 'Disponible', 'Emprunté') AS statut
FROM livres 
ORDER BY titre ASC;

-- Afficher les livres par auteur (exemple: Tolkien)
-- SELECT * FROM livres WHERE auteur LIKE '%Tolkien%' ORDER BY titre ASC;

-- ============================================================================
-- Notes d'utilisation
-- ============================================================================
-- 1. Créer une base de données: mysql -u root -p < script.sql
-- 2. L'application utilise cette table via LivreDAOImpl
-- 3. Les colonnes annee_publication et disponible sont essentielles
-- 4. Les index améliorent les performances de recherche
-- ============================================================================
=======
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
>>>>>>> 23e1e4fb064834a1628e4ab848cde59643820c49
