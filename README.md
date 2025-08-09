# Getting Started

## Build application with
`$ ./gradlew build`

## Chat

Run chat with

`SPRING_PROFILES_ACTIVE=apikey,chat ./gradlew bootRun -PmainClass=se.ryz.ai.ailab.Chat`

## Indexer

Run indexer with

`$ SPRING_PROFILES_ACTIVE=apikey,index ./gradlew bootRun -PmainClass=se.ryz.ai.ailab.IndexDocument --args='<DOCUMENT>'`

Document can be a pdf or text file

## Chat with document

Run with
`$ SPRING_PROFILES_ACTIVE=apikey,chatdocument ./gradlew bootRun -PmainClass=se.ryz.ai.ailab.ChatDocument`