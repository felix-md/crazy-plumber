#!/bin/bash


# Compile all files to another folder
find src -name "*.java" -print | xargs javac -d compiled

echo "Compilation done"

# Change to the bin directory


# Launch the App file
java -cp compiled:levels:puzzle_pipes gui.App

echo "Execution done"


#delete all the class files
rm -rf compiled

