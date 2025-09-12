# Förberedelser

Det krävs inga förberedelser men det kan ändå vara bra att följa instruktionerna i detta avsnitt eftersom det är en del som måste laddas ned och installeras innan labbarna kan göras.


## Installera Ollama

Om du vill testa att köra en modell lokalt utan att använda internet kan Ollama vara ett bra alternativ.

Ollama finns att installera från https://ollama.com/

Det tar ett tag att ladda hem en modell så för att förbereda kan man köra modellen Ollama 3.2:

`$ ollama run llama3.2`

Testa att chatta och se om det går någorlunda snabbt att få svar.
Labbarna går att köra lokalt mot Ollama eller mot OpenAI:S modeller på nätet.

## Ladda hem Milvus
Milvus är en vektordatabas som kan användas när man jobbar med AI och RAG.
Som förberedelse, kör 

`$ docker-compose up`

i projektets rotkatalog där `docker-compose.yml` ligger. Det kommer att ta ett tag att ladda ned och starta men efter den första gången så går det fort.

Nu är allt förberett för att kompetensdagen ska gå smidigare.

# Exepmelapplikationer

Här kommer instruktioner för att köra exemplena under kompetensdagen.

## Bygg applikationerna med
`$ ./gradlew build`

## Konfigurera API-nyckel
Kopiera filen `src/main/resources/application-apikey.properties-template` till `src/main/resources/application-apikey.properties` och konfigurera
api nyckeln för OpenAI (kommer på kompetensdagen) i filen.

## Applikationen Chat

Kör chat med

`$ SPRING_PROFILES_ACTIVE=apikey,chat ./gradlew bootRun -PmainClass=se.ryz.ai.ailab.Chat`

## Applikationen Indexer

Kör indexer med

`$ SPRING_PROFILES_ACTIVE=apikey,index ./gradlew bootRun -PmainClass=se.ryz.ai.ailab.IndexDocument --args='<DOCUMENT>'`

Dokumentet kan vara en pdf eller textfil

## Chatta med dokumentet

Kör med
`$ SPRING_PROFILES_ACTIVE=apikey,chatdocument ./gradlew bootRun -PmainClass=se.ryz.ai.ailab.ChatDocument`

# Shelly
Kommandon för att slå av och på en Shelly enhet via dess REST API

För att slå på shelly
`curl -X POST -d '{"id":1,"method":"Switch.Set","params":{"id":0,"on":true}}' http://172.16.50.40/rpc`

För att slå av shelly
`curl -X POST -d '{"id":1,"method":"Switch.Set","params":{"id":0,"on":false}}' http://172.16.50.40/rpc`
