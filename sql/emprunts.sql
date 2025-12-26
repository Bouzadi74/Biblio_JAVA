CREATE TABLE emprunts (
    id_emprunt INT PRIMARY KEY AUTO_INCREMENT,
    id_membre INT NOT NULL,
    id_livre INT NOT NULL,
    date_emprunt DATE NOT NULL,
    date_retour_prevue DATE NOT NULL,
    date_retour_effective DATE,
    statut VARCHAR(20) NOT NULL
);
INSERT INTO emprunts 
(id_membre, id_livre, date_emprunt, date_retour_prevue, date_retour_effective, statut)
VALUES
-- Emprunts en cours
(1, 101, '2025-01-01', '2025-01-10', NULL, 'EN_COURS'),
(1, 102, '2025-01-03', '2025-01-12', NULL, 'EN_COURS'),

-- Emprunt retourné à temps
(2, 103, '2024-12-01', '2024-12-10', '2024-12-09', 'RETOURNE'),

-- Emprunt retourné en retard
(3, 104, '2024-11-01', '2024-11-10', '2024-11-15', 'EN_RETARD'),

-- Emprunt en retard (non retourné)
(2, 105, '2024-12-20', '2024-12-30', NULL, 'EN_RETARD');
