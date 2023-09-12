#!/bin/bash

if [ -z $1 ]
then
  echo "================================================================================"
  echo "|                                                                              |"
  echo "|  |\   /|  / \ |_   _||  _|  | | \ \/ / / __|  / _| |  _|  | | | -  ||_   _|  |"
  echo "|  | \ / | / _ \  | |  |   \  | |  |  |  \__ \ | |_  |   \  | | |  _/   | |    |"
  echo "|  |_| |_|/_/ \_\ |_|  |_||_| |_| /_/\_\ |___/  \__| |_||_| |_| |_|     | |    |"
  echo "|                                                                              |"
  echo "================================================================================"
  echo " "
  echo " "
  echo "1: Build app (maven)"
  echo "2: Run full test suite"
  echo "3: Run specific JUnit test"
  echo " "

  read -p "Select: " CHOICE

else
  CHOICE="$1"
fi

case $CHOICE in
  1)
    mvn clean package -DskipTests
    ;;
  2)
    mvn test
    ;;
  3)
    read -p "Test class name: " TEST_CLASS
    mvn test -Dtest="$TEST_CLASS"
    ;;
  *)
    echo "Invalid selection!"
    ;;
esac
