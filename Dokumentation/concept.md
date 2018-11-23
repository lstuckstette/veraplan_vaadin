# Arbeit

Entwicklung eines verteilten Softwaresystems zur gemeinschaftlichen Erstellung von Dienstplänen nach dem Bottom-Up Ansatz

## Use Cases

### Authentifizierung

1. Benutzer Registrieren
2. Benutzer Login
3. Eigene Nutzerdaten einsehen
4. Eigene Nutzerdaten ändern
5. (Eigenen Benutzer löschen)
6. (Passwort vergessen)

### Rollen

1. Rolle durch 'admin' setzen (defualt == USER)
2. Rolle durch 'admin' ändern


### Erfassen von Stammdaten

1. Manuelle Eingabe von Personal, Abteilungen, Räumen+Equipment, Gebäuden, Rollen
2. (import aus datenbank...)

### Erfassen von Plan-Ressourcen und Constraints

1. Erfassen von TimeConstraints des Personals
2. Gleichzeitiges erfassen von Ressourcen in 'usergroup'
2. Erstellen von 'Assignments'

### Visualisierung von Plan-Ressourcen und Constraints

1. Anzeige der Organisation (Building -> Room ; Department -> User/Usergroups) als 'Tree'
2. Anzeige der Assignments 'dynamic sorted'

### Nutzung des Cloud-Service

1. Export der Plan-Ressourcen und Constraints in JSON-Format
2. Senden der Plan-Ressourcen und Constraints im JSON-Format
2. Empfangen der generierten Pläne

### Visualisierung der errechneten Pläne

1. Anzeige der vorgeschlagenen Pläne
