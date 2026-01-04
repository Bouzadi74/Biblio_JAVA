-- Create emprunts table
CREATE TABLE emprunts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_membre BIGINT NOT NULL,
    isbn_livre VARCHAR(20) NOT NULL,
    date_emprunt DATE NOT NULL,
    date_retour_prevue DATE NOT NULL,
    date_retour_effective DATE,
    statut VARCHAR(20) NOT NULL
);

-- Insert test data
INSERT INTO emprunts (id_membre, isbn_livre, date_emprunt, date_retour_prevue, date_retour_effective, statut) VALUES
(1, '978-2-07-061897-0', '2025-01-01', '2025-01-10', NULL, 'EN_COURS'),
(2, '978-2-07-068256-7', '2025-01-03', '2025-01-12', NULL, 'EN_COURS');
