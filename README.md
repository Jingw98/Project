  === Prototype of VIDIVOX  ===
Contributors: Mincheng Mu(mmu119)  Jing Wang(jwan501)

The prototype is a video player with additional functions of convert text to speech and editing video.

      == Description ==
Basic function:
Play: video file can be add from the top menu bar ”File > Open File”. The default path is the working directory. Video can be play and pause by press play button.

Skip: Skip the video forwards or backwards at a speed of 5X. Keep clicking the skip button will increase the speed of skipping. The maximin speed is 30X. During the skip, the video would play at the normal speed after pressing the Play button.

Stop: Stop the playing video, clear the User Interface.

Mute: Mute the player. Clicked again to set the volume back to original.

Volume: Change the volume of the player.



Speech function:
Convert text to speech: Type the words in the bottom text field and press ENTER. And the input text will be heard be played by player. After that a MP3 file of speech can be saved to selected folder. The save path can be changed by press the save button beside the text field.

Add MP3 file to Audio: Editing function is in the top menu bar “Other > Add Audio”. A new window will pops up for future options. If a video has been played by player, the path of the video will be added to video path automatically. Or it will be empty, and the user have to add it. The MP3 file that user want to added to video can also be selected in this window. After fill both paths, user could choose overlap or overlay the original video sound by press the button at the button of the window.

Overlap: Overlap the original video sound.
Overlay: Overlay the original video sound
                               
      ==How to use==
An executable jar file called  VIDIVOX.jar is provided.
Execute in terminal with command : java -jar VIDIVOX.jar

      == Attention ==
The res folder which includes all the button picture must be in the same folder of jar file.
