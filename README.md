# Sample JavaFX application using Proxy API

# SOFTENG206 Guess Who Alpha

## To setup the API to access Chat Completions and TTS

- add in the root of the project (i.e., the same level where `pom.xml` is located) a file named `apiproxy.config`
- put inside the credentials that you received from no-reply@digitaledu.ac.nz (put the quotes "")

  ```
  email: "UPI@aucklanduni.ac.nz"
  apiKey: "YOUR_KEY"
  ```

  These are your credentials to invoke the APIs.

  The token credits are charged as follows:

  - 1 token credit per 1 character for Googlel "Standard" Text-to-Speech.
  - 4 token credit per 1 character for Google "WaveNet" and "Neural2" Text-to-Speech.
  - 1 token credit per 1 character for OpenAI Text-to-Text.
  - 1 token credit per 1 token for OpenAI Chat Completions (as determined by OpenAI, charging both input and output tokens).

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

## To run game
Please note that we have disabled the tokens we provided. So, if you or anyone you share the project with needs to run your project, they will need an OpenAI access token. We have made this easy for you, as you can simply place the token in the config file (with your own email) and run it. It's using our endpoint, but we won't disable it (at least not anytime soon). You can be reassured we are not saving or snooping the token. But if you prefer not to go through our endpoint, you will need to (possibly substantially) refactor your code to call the OpenAI API directly. Some Java wrappers are available to use but still require refactoring to use those wrappers instead of ours. 

Unfortunately, this same process won't work for the Google TTS voices. This means you must either change the code to use the OpenAI TTS voices or revert to the free built-in TTS.

The final thing, is the code style tool that is runnable. You shouldn't have committed your config in the first place (remember, it was in the .gitignore, but we noticed some students still forcefully added it or saved to a different filename).  That, again, shouldn't be an issue, even if you did, as it just means someone can ping the code style tool to run and send the email report (but it will go to your email). Even if they see the report, they won't be able to click on any of the links as they aren't added to our repo (private in the SOFTENG 206 GitHub Organization). 
