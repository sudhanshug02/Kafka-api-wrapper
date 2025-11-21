# kafka-api-wrapper

Minimal Spring Boot project demonstrating:
- REST API to accept transactions
- AES-256 encrypt/decrypt utilities
- JWT validation (demo)
- MD5 checksum validation
- Produce encrypted messages to Kafka
- Simple consumer that decrypts messages

How to run (local):
1. Start Kafka and Zookeeper using docker-compose:
   docker-compose up -d
2. Build and run the Spring Boot app:
   mvn clean package
   java -jar target/kafka-api-wrapper-1.0.0.jar

POST /api/tx/send expects:
Headers:
  Authorization: Bearer <jwt>
  Checksum: <MD5 of JSON body>

Body example:
{
  "Mobile":"8999999999",
  "UserId":"123",
  "Amount":"5000"
}
