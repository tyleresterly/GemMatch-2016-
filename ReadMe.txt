TYLER ESTERLY
CS251
GemMatch Lab ReadMe file:


_________________
Game Play:       \
__________________\

How to play:
____________

The game is played by swapping gems and creating 3-of-a-kind of the same color.
When a match is mad, the matched gems disappear, and all gems above them will 
drop down and take their place. This cascade can create new matches, or "combos",which
count for additional points. Gems are swapped by clicking on one (a yellow box
will appear around the gem), and then clicking on the gem you would like to swap
with. Only gems in the same 3x3 grid as the selected gem are valid swaps and all others
will be ignored. Swaps that don't result in a match are allowed.

The game starts with 4 gem colors, and then adds up to 3 more as the game goes on. 

How Scoring Works:
_________________

You have two minutes to get the highest score possible. The scording system is pretty simple.
Essentially the game counts how many gems were just matched, and then multiplies it by the current
combo number, and then adds the number of matches.

For example, if a swap creates 4-of-a-kind, then the proceeding cascade creates a
3-of-a-kind match, and the cascade after that causes a 5-of-a-kind, the score will be:

( (4 + (0*4)) + (3 + (1*3)) + (5 + (2*5)) ) = 22 total points.



___________________
Program Internals: \
____________________\
The program only contains two class-files:

GemManager.java:
________________

This class does all of the bookkeeping for the Gem-Match game. Essentially, the methods and classes
important for game logic are as follows:

Class Gem:
 - Creates a "Gem" that is essentially a color bundled with a character-representation of the gem.
The plan was to make gems with "special powers" that would share this as a parent class, but I didn't
have time to implement that.


fillBoard:
 - Takes a Linked List of the available gems and randomly fills a 2D array (representing the actual gameboard) with 
the gems.

swapGems: 
 - Function that swaps two gems on the gameboard assuming it is a legal swap (within the same 3x3 section of 
the first gem-clicked). It then puts the coordinates of the gems in a Linked List filled with coords that could contain
possible matches (matchCoords)

After a gem is swapped,

isMatch:
 - Detects if a vertical or horizontal match occurs and counts the amount of gems that 
have been matched. If a match has been detected it records the coordinates of the gems
that have been matched and stores them in a Linked List (clearedCoords).

If a match is detected,

clear:
 - loops through clearedCoords and sets every coordinate in the list to null. 

after the matched gems are removed,

cascade:
 - loops through the gameboard column by column, starting from the bottom. If it find a null
coordinate, it searches for the next gem sitting above it and pulls it down to the empty spot.
If there is no gem sitting above it, it means that a new gem needs to be created and pulled down.


GemGui.java
____________

This class creates the GUI for the Gem-Match game. The important methods and classes are as follows:

GamePanel: 
 - Essentially this class just draws the game board, and it does so in a way that looks nice
no matter what the size of the window is. It also draws a yellow box around a gem that the user selects.

InfoPanel:
 - creates a JPanel that contains all the information that the user might need for the game.
This includes a field that tells the player how much time is remaining, a field that contains their 
current score, and a field that displays their latest combo number.
This panel also contains a JButton that allows the player to restart the game (essentially just closes
the current window and then opens a new one).

AnimateCascade:
 - contains an actionPerformed method that animates two gems swapping, the matched gems disappearing, the new gems
falling into place, and then it repeats itself until the combo is over. It also updates the score.

GemGui:
 - The constructor for the GemGui. It creates a GemManager, adds the GamePanel and InfoPanel to a JPanel,
and finally has a listener that checks for mouse-clicks. If a click is made, the paintComponent method in
GamePanel draws a box around the gem that was clicked on, and assuming the user clicks a gem within the 3x3 
area surrounding the first gem, it will then swap the two gems.

When gems are swapped,

swap:
 - Method that swaps two gems. After the gems are swapped, a timer is started that
uses the actionPerformed method in AnimateCascade. The timer keeps ticking until
there are no more matches, and then the game waits for the player to make another
swap.



_____________________________
Extras                       \
______________________________\
There are a few things that I added that weren't on Brooke's list of bare-minimum requirements.

Gems Added Over Time
_____________________
The game starts with only 4 possible gem colors. As the game goes on
up to 3 more gem colors are different to slightly increase difficulty.

Post-game stats
_______________

After every game finishes, a window appears and tells the 
user the following:
 - That games score. 
 - The highest score in the current session.
 - The largest combo the player made. 


Saves Current-Session High-Score
________________________________
After every game in the current session, it compares
that game's score to the sessions highest score and 
updates the HighScore accordingly. If a new HighScore is made,
the game alerts you of it at the end of the game.

Window-Resizing
________________
The game is able to resize and redraw the GameBoard depending on the 
size of the window with no error.


______________________________
Bugs and Future Additions     \
_______________________________\

Bugs
____
From what I can test there are no noticible bugs. Everything
works as intended, or at least from what I could tell/test.

Future Additions
_________________
There are a few things I planned on adding but couldn't due to time restraints/this
being about a week before finals.

Options Menu:
 - A simple menu that allowed you to set the number of columns, rows, and total time
in a single game.

Special Gems:
 - I had a few ideas for these. I wanted to make a gem that cleared everything vertically if a 
vertial match was made, and horizontally if a horizontal match was made. Another type
of gem would clear all gems surrounding the match. and the final one was a wild card gem that
could be used in a match with any other color.