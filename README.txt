******************************************  Description of the game:  ******************************************
	
makefile:
	- “make run” will run with default arguments
		- speed: 1
		- FPS: 30
	- “java SnakeGame (FPS value) (speed value)” will run with specific arguments
		- range of FPS: 1-100
		- range of speed: 1-10

————————————————————

Splash Screen:
	- Press “ENTER” OR click “Play Game”: enter the snake interface

————————————————————

Snake Interface (use keyboard to control the game):
	- S:   start a new game
	- P:   pause the current game and continue the paused game
	- R:   restart the game with previous speed and FPS
		If you restart the game, you still need to press ’S’ to start the new game
	- U:   speed up at any time
	- D:   slow down at any time
	- Arrow keys:   change direction

————————————————————

Basic game features:

1. Pink Snake
	- length is initialized to be 2 grids

2. Yellow Fruit
	- normal, 60% appearance

3. Red Fruit
	- shorten the snake by 2 grids, 
	- the snake will not be shorten if it occupies equal or less than 3 grids
	- if the first fruit is red, the snake will grow for 1 grid since the snake is initialized to 2 grids
	- 20% appearance

4. Green Fruit
	- arrow keys change to opposite directions
	- arrow keys will become normal until the snake ate a not-green fruit
	- e.g., press ‘UP’, but the snake changes direction to ‘DOWN’
	- 20% appearance

5. When you eat a fruit successfully, you can get a score equals current speed value
	

*********************************************  Enhancements:  *********************************************

Two power-ups:

1. Red Fruit
	- shorten the snake by 2 grids, 
	- the snake will not be shorten if it occupies equal or less than 3 grids
	- if the first fruit is red, the snake will grow for 1 grid since the snake is initialized to 2 grids
	- 20% appearance

2. Green Fruit
	- arrow keys change to opposite directions
	- arrow keys will become normal until the snake ate a not-green fruit
	- e.g., press ‘UP’, but the snake changes direction to ‘DOWN’
	- 20% appearance


***************************************  Development Environment:  ***************************************

Mac OS
Version 10.11.3
java 1.7.0_81 



