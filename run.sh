#!/bin/bash

if [ -z "$1" ]
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
  echo "2: Deploy app to AWS"
  echo "3: Deploy app to AWS (no build)"
  echo "4: Deploy authorizer to AWS"
  echo "5: Spin up local API"
  echo "6: Run full test suite"
  echo "7: Run specific JUnit test"
  echo " "

  read -p -r "Select: " CHOICE

else
  CHOICE="$1"
fi

case $CHOICE in
  1)
    mvn clean package -DskipTests
    ;;
  2)
    mvn clean package
    sam deploy \
      -t cloudformation.yaml \
      --stack-name MatrixScript \
      --s3-bucket cf-templates-uv8wcc3dn52r-us-east-1 \
      --capabilities CAPABILITY_NAMED_IAM
    ;;
  3)
    sam deploy \
      -t cloudformation.yaml \
      --stack-name MatrixScript \
      --s3-bucket cf-templates-uv8wcc3dn52r-us-east-1 \
      --capabilities CAPABILITY_NAMED_IAM
    ;;
  4)
    cd authorizer || exit
    zip -r authorizer.zip authorizer.js package.json
    cd ..
    sam deploy \
      -t cloudformation.yaml \
      --stack-name MatrixScript \
      --s3-bucket cf-templates-uv8wcc3dn52r-us-east-1 \
      --capabilities CAPABILITY_NAMED_IAM
    ;;
  5)
    docker compose up -d &&
    ./local/build.sh
    ;;
  6)
    mvn test
    ;;
  7)
    read -p -r "Test class name: " TEST_CLASS
    mvn test -Dtest="$TEST_CLASS"
    ;;
  *)
    echo "Invalid selection!"
    ;;
esac
