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
  echo "2: Deploy app to AWS"
  echo "3: Deploy app to AWS (no build)"
  echo "4: Deploy authorizer to AWS"
  echo "5: Spin up local API"
  echo "6: Spin up local API (no build)"
  echo "7: Run full test suite"
  echo "8: Run specific JUnit test"
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
    mvn clean package
    cd authorizer
    zip -r authorizer.zip authorizer.js package.json
    cd ..
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
    cd authorizer
    zip -r authorizer.zip authorizer.js package.json
    cd ..
    sam deploy \
      -t cloudformation.yaml \
      --stack-name MatrixScript \
      --s3-bucket cf-templates-uv8wcc3dn52r-us-east-1 \
      --capabilities CAPABILITY_NAMED_IAM
    ;;
  5)
    mvn clean package -DskipTests
    sudo docker stop $(sudo docker ps -a -q --filter ancestor=amazon/dynamodb-local) &> /dev/null
    sudo docker run --name dynamo -d -p 8000:8000 amazon/dynamodb-local
    sudo aws dynamodb create-table \
      --table-name MatrixScript-Sessions \
      --attribute-definitions AttributeName=SessionToken,AttributeType=S \
      --key-schema AttributeName=SessionToken,KeyType=HASH \
      --billing-mode PAY_PER_REQUEST \
      --endpoint-url http://localhost:8000 \
      --output text
    sudo sam local start-api \
      -t cloudformation.yaml \
      --warm-containers EAGER \
      --env-vars env.json \
      --region us-east-1 \
      --log-file logs.txt
    ;;
  6)
    sudo docker stop $(sudo docker ps -a -q --filter ancestor=amazon/dynamodb-local) &> /dev/null
    sudo docker run -d -p 8000:8000 amazon/dynamodb-local
    sudo aws dynamodb create-table \
      --table-name MatrixScript-Sessions \
      --attribute-definitions AttributeName=SessionToken,AttributeType=S \
      --key-schema AttributeName=SessionToken,KeyType=HASH \
      --billing-mode PAY_PER_REQUEST \
      --endpoint-url http://localhost:8000 \
      --output text
    sudo sam local start-api \
      -t cloudformation.yaml \
      --warm-containers EAGER \
      --env-vars env.json \
      --region us-east-1 \
      --log-file logs.txt
    ;;
  7)
    mvn test
    ;;
  8)
    read -p "Test class name: " TEST_CLASS
    mvn test -Dtest="$TEST_CLASS"
    ;;
  *)
    echo "Invalid selection!"
    ;;
esac
