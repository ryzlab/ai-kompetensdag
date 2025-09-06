# Förberedelser (valfritt)

Det krävs inga förberedelser men det kan ändå vara bra att följa instruktionerna i detta avsnitt eftersom det är en del som måste laddas ned och installeras innan labbarna kan göras.


## Installera Ollama

Om du vill testa att köra en modell lokalt utan att använda internet kan Ollama vara ett bra alternativ.

Ollama finns att installera från https://ollama.com/

Det tar ett tag och kräver en del bandbredd att köra modeller så innan kompetensdagen kan det vara bra att köra Ollama en gång så att modellen laddas ned.

`$ ollama run llama3.2`

Testa att chatta och se om det går någorlunda snabbt att få svar.
Labbarna går att köra lokalt mot Ollama eller mot OpenAI:S modeller på nätet.

## Ladda hem Milvus
Milvus är en vektordatabas som kan användas när man jobbar med AI.
Som förberedelse, kör 

`$ docker-compose up`

i projektets rotkatalog där `docker-compose.yml` ligger. Det kommer att ta ett tag att ladda ned och starta men efter den första gången så går det fort.

Nu är allt förberett för att kompetensdagen ska gå smidigare.

# Labbar

Här kommer instruktioner för att köra labbarna under kompetensdagen.

## Bygg applikationerna med
`$ ./gradlew build`

## Applikationen Chat

Kör chat med

`SPRING_PROFILES_ACTIVE=apikey,chat ./gradlew bootRun -PmainClass=se.ryz.ai.ailab.Chat`

## Applikationen Indexer

Kör indexer med

`$ SPRING_PROFILES_ACTIVE=apikey,index ./gradlew bootRun -PmainClass=se.ryz.ai.ailab.IndexDocument --args='<DOCUMENT>'`

Dokumentet kan vara en pdf eller textfil

## Chatta med dokumentet

Kör med
`$ SPRING_PROFILES_ACTIVE=apikey,chatdocument ./gradlew bootRun -PmainClass=se.ryz.ai.ailab.ChatDocument`