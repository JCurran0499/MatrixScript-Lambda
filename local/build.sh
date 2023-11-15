aws dynamodb create-table \
  --table-name MatrixScript-Sessions \
  --attribute-definitions AttributeName=SessionToken,AttributeType=S \
  --key-schema AttributeName=SessionToken,KeyType=HASH \
  --billing-mode PAY_PER_REQUEST \
  --endpoint-url http://localhost:4566 \
  --output text

aws iam create-role \
  --role-name Lambda-matrixscript \
  --assume-role-policy-document file://local/trust.json \
  --endpoint-url http://localhost:4566 \
  --output text

aws iam attach-role-policy \
  --role-name Lambda-matrixscript \
  --policy-arn "arn:aws:iam::aws:policy/AmazonDynamoDBFullAccess" \
  --endpoint-url http://localhost:4566 \
  --output text

aws lambda create-function \
  --function-name MatrixScript \
  --runtime java17 \
  --role "arn:aws:iam::000000000000:role/Lambda-matrixscript" \
  --handler app.MatrixScript::handleRequest \
  --timeout 900 \
  --memory-size 2048 \
  --package-type Zip \
  --zip-file fileb://target/MatrixScript-2.0-jar-with-dependencies.jar \
  --environment file://local/env.json \
  --endpoint-url http://localhost:4566 \
  --output text