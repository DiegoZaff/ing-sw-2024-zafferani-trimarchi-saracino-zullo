##### ing-sw-2024-zafferani-trimarchi-saracino-zullo

# Software Engineering Project 2024 - Codex Naturalis
![Codex Naturalis](https://github.com/DiegoZaff/ing-sw-2024-zafferani-trimarchi-saracino-zullo/blob/a8db7a035c7d94030ae9989600cca92f9ab789fa/deliverables/Rules%20and%20requirements/814qEh0JKdS.jpg)



## Project description

This project consists in the realization of a working application about the table game Codex Naturalis on local multiplayer. The network communication  has been implemented in both Socket and RMI



## Project specifications and rulebook

Project specifications in [English](https://github.com/DiegoZaff/ing-sw-2024-zafferani-trimarchi-saracino-zullo/blob/a8db7a035c7d94030ae9989600cca92f9ab789fa/deliverables/Rules%20and%20requirements/requirements%20english.pdf) and [Italian](https://github.com/DiegoZaff/ing-sw-2024-zafferani-trimarchi-saracino-zullo/blob/a8db7a035c7d94030ae9989600cca92f9ab789fa/deliverables/Rules%20and%20requirements/requirements.pdf).

Codex Naturalis rulebook in [English](https://github.com/DiegoZaff/ing-sw-2024-zafferani-trimarchi-saracino-zullo/blob/a8db7a035c7d94030ae9989600cca92f9ab789fa/deliverables/Rules%20and%20requirements/CODEX_Rulebook_EN.pdf) and [Italian](https://github.com/DiegoZaff/ing-sw-2024-zafferani-trimarchi-saracino-zullo/blob/a8db7a035c7d94030ae9989600cca92f9ab789fa/deliverables/Rules%20and%20requirements/CODEX_Rulebook_IT.pdf).



## Running the application

- To start download the [jar files](), you will find a ServerApplication and a ClientApplication.
- Run from command line the ServerApplication, default ports are specified below.

      /path/to/openjdk21/bin/java -jar ServerApplication.jar 8886 8887
      
- Run from command line the ClientApplication, the game can be run in local using the localhost ip address or the ip addres of network the device hosting the server application is connected to.
The game can be played both with RMI and TCP connection, the choice in embeded in the GUI, while for the TUI you have to add --rmi at the end of the command.

- To run the client GUI:

      /path/to/openjdk21/bin/java -jar ClientApplication.jar client-ip --gui
      ex: /path/to/openjdk21/bin/java -jar ClientApplication.jar 127.0.0.1 --gui

- To run the client TUI:

      /path/to/openjdk21/bin/java -jar ClientApplication.jar server-ip client-ip port (--rmi)
      ex rmi: /path/to/openjdk21/bin/java -jar ClientApplication.jar 127.0.0.1 127.0.0.1 8887 --rmi
      ex tcp: /path/to/openjdk21/bin/java -jar ClientApplication.jar 127.0.0.1 127.0.0.1 8886

For the best UI experience for the TUI it is recommanded to use Unix terminals.
For the best UI experience for the GUI is recommanded a resolution on 1920x1080 and for Windows users a 100% zoom.

## Project functionalities

The following table shows the implemented functionalities

| Project Funcionalities   |    |
|--------------------------|----|
| Complete Rules           | ✔  |
| TUI                      | ✔  |
| GUI                      | ✔  |
| RMI Connection           | ✔  |
| Socket COnnection        | ✔  |
| Chat                     | ✔  |
| Multiple Games           | ✔  |
| Server Persistence       |  ✔ |
| Disconnection Resilience | ❌  |


## Documentation

* ### [JavaDoc](https://gc28project.netlify.app/it.polimi.ingsw.gc28/module-summary.html)
* ### [UML Class diagram](https://github.com/DiegoZaff/ing-sw-2024-zafferani-trimarchi-saracino-zullo/blob/a604bbb1bd29c48ee914b22a45e0188c3ddd3fdc/deliverables/UML2/uml%20model%202.png)

* ### [UML Network diagram](https://github.com/DiegoZaff/ing-sw-2024-zafferani-trimarchi-saracino-zullo/blob/a604bbb1bd29c48ee914b22a45e0188c3ddd3fdc/deliverables/UML2/uml%20rete.drawio.png)

* ### [Test coverage report](https://gc28testcoverage.netlify.app/)

* ### [Jar File]()



## Group members

* [Diego Zafferani](https://github.com/DiegoZaff)

* [Alessandro Trimarchi](https://github.com/AlessandroTrimarchi)

* [Giacomo Saracino](https://github.com/giasa-poli)

* [Francesco Zullo](https://github.com/ZulloFrancesco)





## Software used for Project Development

| Software | Description                |
|----------|----------------------------|
| Maven    | Dependency Management      |
| IntelliJ | IDE                        |
| JSON     | Loading Cards              |
| JavaFX   | GUI                        |
| DrawIO   | Class and Network diagrams |
| JUnit    | Unit Testing               |

## License

* ### Game [License](https://www.craniocreations.it/prodotto/codex-naturalis)

* ### Project [License](https://github.com/DiegoZaff/ing-sw-2024-zafferani-trimarchi-saracino-zullo/blob/62c8999c15e8e05dc601cd14efefa12c3c669287/LICENSE)


