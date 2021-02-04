# DragDropGame

## Game Logic:
1. Once the game starts:
    1. Timer starts at the top with 20 seconds along with shapes left to match
    2. A shape appears at the bottom placeholder along with a unique set of shapes on the screen
    3. The player must match shape with the shapes on screen
    4. The shape that is matched - dissapears from the screen and a new shape to match appears at the bottom of the screen
    5. With each level a new shape gets added to the the screen
    6. There are a total of 20 levels

## The app will showcase:

- Clean architecture
- MVVM
- Kotlin coroutines + Room
- Kotlin collections


## tasks

### 1. Get the app working with the first screen and the first few levels
  - [x] Write up a rough draft for the app
  - [x] Position the "matching" shape at the bottom of the screen
  - [x] Centralize everything on one branch
  - [x] Implement the logic for the matching shape to be left where it is after a drop if it didn't hit anything
  - [x] Implement the shape to reappear at the bottom based on the shapes that are left on the screen once a match happens.
  - [x] Restart the game once all shapes were matched
  - [x] Implement the concept of a Level and advancing to the first 4 levels
  - [x] Implement buttons that will show after the completion of a Level (Main menu and next level)
  - [x] Implement the fail of the user in case the time runs out
  - [x] Add the retry / main menu option after user failure
  - [x] Add operator overloading for accessing points
  - [ ] Remove dependency on shape center in Shape class
  - [x] Implement moving the matching shape on touch instead of long press
  - [ ] Write up tests for the logic and calculations involved above
  - [ ] Apply clean architecture principles + MVVM

### 2. Finish the rest of the 20 levels
  - [ ] find more shapes and add them so it will be sufficient for 20 levels
  - [ ] add the logic that will take the user to the next level on completion
  - [ ] Write up tests for the logic above

### 3. Build a main menu
  - [ ] Display the user's current highscore and the level they are at right now
  - [ ] Add the button and logic for starting a new game
  - [ ] Add the button and logic for continuing where the user left off
  - [ ] Write up unit and UI test for cases above

### 4. Add Local Storage
  - [ ] Add Room to the project
  - [ ] Implement storing the level at which the user is at right now
  - [ ] Implement storing the highscore
  - [ ] Write test for storage functionality

### 5. Release version 1
  - [ ] release the app on google play

### Backlog
  - [ ] Make sure that the sizes of the elements are relative to the screen size

###Bugs
  - [ ] if the drop shape is dropped a little bit to the right of the matching shape - the match doesn't happen
  - [ ] if the time runs out and the player is still holding the shape - the app will crash