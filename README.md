# Nine Men's Morris
A modern, feature-rich implementation of the classic Nine Men's Morris board game built with Java and JavaFX. This project demonstrates advanced Object-Oriented Programming principles, network programming with sockets, and concurrent thread management.
## Features

### Game Modes

- **Local Multiplayer**: Two players on the same device with hot-seat gameplay
- **Online Multiplayer**: Play over network using TCP socket connections
  - Host/Client architecture
  - Real-time move synchronization
  - Automatic opponent discovery on local network
  - Connection status indicators

### Core Gameplay

**Complete Rule Implementation**:
  - Three distinct game phases: Placing, Moving, Flying
  - Automatic mill detection (16 possible mill combinations)
  - Smart piece removal validation (cannot remove from mills unless all pieces are in mills)
  - Win condition detection (opponent has <3 pieces or no valid moves)

 **Intelligent Game Logic**:
  - **Strategy Pattern** for phase-specific rules
  - Adjacency validation for normal moves
  - Free movement when player has 3 pieces (Flying phase)
  - Turn-based system with automatic player switching
 
 - **User Experience Features**:
  - Piece highlighting on selection
  - Error messages with explanations
  - Game state indicators
  - Piece counters (available/on board)
  - Reset game functionality
  - Return to main menu option

Network Features
**Robust Communication**:
  - Client-Server architecture using TCP sockets
  - Message serialization for move transmission
  - Automatic game state synchronization
  - Disconnect detection and handling
  - Thread-safe UI updates with `Platform.runLater()`

**Network Protocols**:
  - Custom message format: `COLOR:FROM:TO`
  - Player name exchange on connection
  - Game reset synchronization
  - Graceful disconnect notifications

Data Persistence

- **SQLite Database Integration**:
  - Automatic game history recording
  - Player statistics tracking
  - Game duration logging
  - Game type classification (Local/Online)
  - Timestamp-based game records
 
### Software Engineering Best Practices

 **Design Patterns**:
  - **Model-View-Controller (MVC)** for separation of concerns
  - **Strategy Pattern** for game rules (PlacingRules, MovingRules, FlyingRules)
  - **Observer Pattern** for UI updates
  - **Singleton Pattern** for database management

 **OOP Principles**:
  - Inheritance (Node → SquareNode, Player → LocalPlayer/NetworkPlayer)
  - Polymorphism (dynamic method dispatch for Player and IGameRules)
  - Encapsulation (private fields, public accessors)
  - Abstraction (IGameRules interface, Player abstract class)

## Technologies

### Core Technologies

| Technology | Version | Purpose |
|------------|---------|---------|
| **Java** | 17+ | Core programming language |
| **JavaFX** | 21.0.6 | GUI framework and FXML support |


### Development Tools

- **IDE**: IntelliJ IDEA 2024+
- **Scene Builder**: Visual FXML editor
- **Git**: Version control

## Installation

- git clone https://github.com/popam482/Nine-Mens-Morris.git
- cd Nine-Mens-Morris
- Build with Maven
- mvn clean package
- Run the application
- mvn javafx:run

## Screenshots

<img width="936" height="967" alt="image" src="https://github.com/user-attachments/assets/95210a52-0049-4754-bd3b-e0eca074a701" />

<img width="920" height="962" alt="image" src="https://github.com/user-attachments/assets/25e0b936-db7b-40a5-b25f-daf91e5b492b" />

<img width="926" height="953" alt="image" src="https://github.com/user-attachments/assets/3fc955fe-9ca8-4095-996b-6dc91b8123fb" />
