# Abstract
In this repository you can find the theory over the design, analysis, and fabrication of a bulk acoustic wave (BAW) resonator for filtering applications in the 220-225 MHz band, with the aim of comparing it to current ceramic resonators. 

Finite element analysis (FEA) methods, using COMSOL Multiphysics, and analytical analysis were used to determine the characteristics of the resonator, such as resonance frequency and quality factor (Q). 
The FEA results showed high selectivity and signal filtering efficiency, comparable to the values obtained by one example of COMSOL Multiphysics. 

The advantages of the BAW resonator include high selectivity, reduced size, and better integration into integrated circuits, although it presents challenges in complexity and manufacturing costs compared to ceramic resonators.

Java were used to code a script for the FEA in COMSOL Multiphysics 6.1. You can use the .class or .mph archives to run the same analysis on your computer or can you use the raw script. 
To use the raw script you must compiled before, in order to do that you should follow the next steps: 

# Structure of The Resonator
![alt text](https://github.com/ezequiel1611/thin_film_baw_resonator/blob/main/images/structure.png)

The bottom layer of the resonator is silicon, on top of which there is an aluminum layer that functions as the ground terminal. 
The next layer is the piezoelectric layer, in this case made of zinc oxide (ZnO), and above it, there is another aluminum terminal that will be the positive terminal. 
A part of the silicon has been removed from the center of the resonator so that the thickness of the resonator's active structure is very small. 
The central part of the silicon has a thickness of 7 µm, while the piezoelectric layer has a thickness of 9.5 µm, both are a square with a side length of 1.7 mm. Both aluminum layers have a thickness of 200 nm, and the upper electrode has a square surface with a side length of 500 µm. 

# Results
## Admittance vs Frequency
![alt text](https://github.com/ezequiel1611/thin_film_baw_resonator/blob/main/images/admittance.png)
## Quality Factor
![alt text](https://github.com/ezequiel1611/thin_film_baw_resonator/blob/main/images/quality_factor.png)

# How to use the Script
## Compile Java script on Linux
1. **Open a Command Terminal:** Go to the folder where you want to store the script and there will be a command terminal there.
2. **Clone the Repository:** In the Command Terminal, type:
   ```
   git clone https://github.com/ezequiel1611/thin_film_baw_resonator
   ```
   and press Enter to create a local copy of this repository on your computer.
3. **Go into the New Folder:** Change directory to the newly cloned repository by typing:
   ```
   cd thin_film_baw_resonator
   ``` 
4. **Compile the Script:** To compile the script, type the following command in the Command Terminal, replacing `<COMSOL path>`with the actual path where COMSOL Multiphysics is installed:
   ```
   \<COMSOL path\>/bin/comsol compile resonador_baw_chatgpt.java
   ```
   If COMSOL is installed in the default location, the command might look like this:
   ```
   /usr/local/comsol61/multiphysics/bin/comsol compile resonador_baw_chatgpt.java 
   ```
5. **Run the Model:** After the compilation ends, you should see a new file with a `.class` extension. Open COMSOL Multiphysics, go to `File->Open` and select the `.class` file to run the model.

## Compile Java script on Windows
1. **Open Command Prompt:** Pres `Win + R`, type `cmd`, and press Enter to open the Command Prompt. Navigate to the folder where you want to store the script using the `cd` command. For example, if you want to store it in a folder named `Scripts` on your Desktop, you can use:
  ```
  cd %USERPROFILE%\Desktop\Scripts
  ```
2. **Clone the Repository:** In the Command Prompt, type:
  ```
  git clone https://github.com/ezequiel1611/thin_film_baw_resonator
  ```
and press Enter to create a local copy of this repository on your computer.

3. **Go into the New Folder:** Change directory to the newly cloned repository by typing:
  ```
  cd thin_film_baw_resonator
  ```
4. **Compile the Script:** To compile the script, type the following command in the Command Prompt, replacing `<COMSOL path>`with the actual path where COMSOL Multiphysics is installed:
  ```
  <COMSOL path>\bin\win64\comsol compile thin_film_baw_resonator.java
  ```
If COMSOL is installed in the default location, the command might look like this:
  ```
  C:\Program Files\COMSOL\COMSOL56\Multiphysics\bin\win64\comsol compile resonador_baw_chatgpt.java
  ```
5. **Run the Model:** After the compilation ends, you should see a new file with a `.class` extension. Open COMSOL Multiphysics, go to `File->Open` and select the `.class` file to run the model.
