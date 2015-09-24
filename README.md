     == Prototype of VIDIVOX  ==
Contributors: Mincheng Mu(mmu119)  Jing Wang(jwan501)

This prototype is a video player with additional functions of converting text to speech and adding audio to video.

      ==How to use==
An executable jar file called "VIDIVOX.jar" and a folder called "res" is provided.
Place jar file VIDIVOX.jar and folder under the same directory.
Execute in terminal by typing the command : 
java -jar VIDIVOX.jar

      == Description ==
Basic functions:

Play: video file can be add from the top menu bar ”File > Open File”. The default path is the working directory. Video can be play and pause by press play button.

Skip: Skip the video forwards or backwards at a speed of 5X. Keep clicking the skip button will increase the speed of skipping. The maximin speed is 30X. The skipping can be canceled by pressing the play button.

Stop: Stop the playing video.

Mute: Mute the player. Clicked again to set the volume back to original.

Volume: Change the volume of the player.

Speech functions:

Convert text to speech: Type the words in the bottom text field and press ENTER. The input text will be converted into synthetic audio and played. After that a MP3 file of speech can be saved by pressing the "save" button. The save path can be changed by press the save button beside the text field.

Add MP3 file to Audio: Editing function is in the top menu bar “Other > Add Audio”. A new window will pops up for future options. If a video has been played by player, the path of the video will be added to video path automatically. Or it will be empty, and the user have to add it. The MP3 file that user want to added to video can also be selected in this window. After fill both paths, user could choose overlap or overlay the original video sound by press the button at the button of the window.

Overlap: Add an audio file to the start of video without replacing the audio of video.
Overlay: Add an audio file to the start of video. The audio of file will be replaced.
                               

     ==GitHub==
     https://github.com/Jingw98/Project
