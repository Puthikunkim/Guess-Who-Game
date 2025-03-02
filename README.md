# SOFTENG206 - Guess Who Game
Project for SOFTENG 206 (Software Engineering Design 1) in Part II of Software Engineering at The University of Auckland.

# Demo
<img src="https://github.com/user-attachments/assets/e1bc6dd5-cdff-4a54-ad3b-752a7d46a7d6" width="400"/>
<img src="https://github.com/user-attachments/assets/fcb5d5b3-4bb9-40cc-9dfa-31fe17adf67e" width="400"/>
<img src="https://github.com/user-attachments/assets/c12e1558-28bf-4d88-baa1-e7c210a2381c" width="400"/>
<img src="https://github.com/user-attachments/assets/656d2c61-89bc-45fb-84b3-2082cef093f9" width="400"/>
<img src="https://github.com/user-attachments/assets/42d68e6f-8261-4986-b036-9d5e019b4bc4" width="400"/>
<img src="https://github.com/user-attachments/assets/550cc456-6ecb-4b88-82ff-5074ac7fa999" width="400"/>
<img src="https://github.com/user-attachments/assets/c60226c1-bf7d-4e12-b2c2-6223e03660b4" width="400"/>
<img src="https://github.com/user-attachments/assets/5c69e7a7-3433-4da7-a74d-607e3006f4ef" width="400"/>
<img src="https://github.com/user-attachments/assets/e409d355-83f6-4784-a5a6-cfe560880cd0" width="400"/>
<img src="https://github.com/user-attachments/assets/d2e76fe7-1fea-4297-b02f-5c45b6e298c1" width="400"/>
<img src="https://github.com/user-attachments/assets/c3cec142-6eb7-4df6-958c-f51f8967312b" width="400"/>

# Developers:
- Jerry Kim
- Shazeel Ali
- Jerry Zhang

# Tech Stack:
- Java
- JavFX
- CSS

# Project Requirements
- Design your own guess who game with unique personalities for each suspect who could be a thief
- Responses from suspects must be generated intelligently utilising OpenAI's API
- Cleverly prompt engineer responses for each suspect
- Include interactable clues with different solutions
- User guesses must be graded by OpenAI's API
- Incorporate text-to-speech for accessibility
- Inclusion of randomness in game design for replayability

## To setup the API to access Chat Completions and TTS

- add in the root of the project (i.e., the same level where `pom.xml` is located) a file named `apiproxy.config`
- put inside the credentials that you received from no-reply@digitaledu.ac.nz (put the quotes "")

  ```
  email: "UPI@aucklanduni.ac.nz"
  apiKey: "YOUR_KEY"
  ```

## To setup codestyle's API

- add in the root of the project (i.e., the same level where `pom.xml` is located) a file named `codestyle.config`
- put inside the credentials that you received from gradestyle@digitaledu.ac.nz (put the quotes "")

  ```
  email: "UPI@aucklanduni.ac.nz"
  accessToken: "YOUR_KEY"
  ```

these are your credentials to invoke gradestyle

## To run the game

`./mvnw clean javafx:run`

## To debug the game

`./mvnw clean javafx:run@debug` then in VS Code "Run & Debug", then run "Debug JavaFX"

## To run codestyle

`./mvnw clean compile exec:java@style`
