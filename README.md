# DragDropGame

## Game Logic:
1. Once the game starts:
    1. Timer starts at the top with 10 seconds along with shapes left to match
    2. A shape appears at the bottom placeholder along with a unique set of shapes on the screen
    3. The player must match shape with the shapes on screen
    4. The shape that is matched - dissapears from the screen
    5. With each level a new shape gets added to the the screen
    6. There are a total of 20 levels

## The app will showcase:

- Clean architecture
- MVVM
- Kotlin coroutines + Room
- Kotlin collections


## tasks

### 1. Get the app working with the first screen and the first level
  - [x] Write up a rough draft for the app
  - [ ] Position the "matching" shape at the bottom of the screen
  - [ ] Change the logic so that the position of the shapes to match are according to the screen size and don't overlap other elements or go out of bounds
  - [ ] Implement a dialog that will pop up that will ask the user if they want to restart or go to the main menu and remove the restart button
  - [ ] Write up tests for the logic above

### 2. Build a main menu
  - [ ] Display the user's current highscore and the level they are at right now
  - [ ] Add the button and logic for starting a new game
  - [ ] Add the button and logic for continuing where the user left off
  - [ ] Write up unit and UI test for cases above

### 3. Finish the rest of the 20 levels
  - [ ] find more shapes and add them so it will be sufficient for 20 levels
  - [ ] add the logic that will take the user to the next level on completion
  - [ ] Write up tests for the logic above

### 4. Add Local Storage
  - [ ] Add Room to the project
  - [ ] Implement storing the level at which the user is at right now
  - [ ] Implement storing the highscore
  - [ ] Write test for storage functionality

### 5. Release version 1
  - [ ] release the app on google play

### Backlog
  - [ ] Make sure that the sizes of the elements are relative to the screen size