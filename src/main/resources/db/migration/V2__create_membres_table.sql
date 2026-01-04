-- Create membres table
CREATE TABLE membres (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    actif BOOLEAN NOT NULL DEFAULT TRUE,
    telephone VARCHAR(30),
    adresse VARCHAR(255)
);

-- Insert test data
INSERT INTO membres (nom, prenom, email, actif, telephone, adresse) VALUES
('EL AMRANI', 'Yassine', 'yassine.elamrani@mail.com', TRUE, '0612345678', 'Rabat'),
('BENALI', 'Sara', 'sara.benali@mail.com', TRUE, '0698765432', 'Casablanca'),
('AIT SAID', 'Omar', 'omar.aitsaid@mail.com', FALSE, '0677889900', 'Fès');

