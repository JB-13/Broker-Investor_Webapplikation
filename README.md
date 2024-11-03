## Die Webapplikation
Die Webapplikation stellt zwei Einstiegspunkte bereit. Einen Zugang für die Investoren und einen Zugang für die Broker.
Dies erfolgt über zwei verschiedene Web-Seiten nämlich /broker.html und /investor.html (Es gibt aber nur eine REST-Anwendung, die mehrere Controller besitzt)

## Funktionalität für Broker
- Broker können sich über Benutzernamen und ein korrektes Password an der Anwendung anmelden
- Weiter ist es möglich, dass sich Broker auch neu an der Anwendung registrieren können. (Daten werden dann in der Datenbank abgelegt.)
- Broker können sich auch wieder deregistrieren
- Nach erfolgreicher Anmeldung bzw. Registrierung werden dem Broker seine Asseets
angezeigt und die Broker können ihre Assets verwalten:
  - Assets neu anlegen
  - angelegt Assets löschen, falls sie nicht einem Portfolio zugeordnet sind. Ansonsten ist das Löschen nicht möglich und es wird eine entsprechende Fehlermeldung angezeigt
- Broker können sich von der Anwendung auch wieder abmelden
- Nur angemeldete Broker können die REST-Schnittstelle aufrufen

## Funktionalität für Investoren
- Investoren können sich über Benutzernamen und ein korrektes Password an der Anwendung anmelden
- Weiter ist es möglich, dass sich Investoren auch neu an der Anwendung registrieren können (Daten werden dann in der Datenbank abgelegt)
- Investoren können sich auch wieder deregistrieren
- Nach erfolgreicher Anmeldung bzw. Registrierung werden dem Investor seine Portfolios
angezeigt
- Der Investor kann existierende Portfolios verkaufen, was der Einfachheit wegen durch ein Löschen realisiert wird
- Der Investor kann neue Portfolios anlegen, wobei jedem Portfolio ein (registrierter) Broker zugeordnet werden muss. Ist der Broker festgelegt, können Assets des Brokers in das Portfolio übernommen werden (Ein Portfolio kann nur Assets des ihm zugeordneten Brokers enthalten)
- Investoren können sich von der Anwendung auch wieder abmelden
- Nur angemeldete Investoren können die REST-Schnittstelle aufrufen
