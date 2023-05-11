# TitleBot
Take home assignment for https://github.com/chatmeter/titlebot

## How to setup and run project
1. Have Docker installed
2. Clone repository onto local machine
3. At the directory's location, run `docker compose up -d`
4. Navigate to `localhost:3000` to view the project UI

---
## Assumptions and Addional Requirements
Due to the recommended time box of 4 hours and minimal project description, I have made the following assumptions and constrained requirements:

1. The backend server can handle multiple clients at the same time, but is not expected to scale as a single service.
2. A single user will only have a small history of persisted website information.
3. Data history can only be added to (no deletion, re-ordering, etc).
4. External websites will always return a response.
5. Users can enter invalid urls into the form (no form validation).
6. Only simple error messaging is expected.

## Design
Based of the above assumptions I have created the following design:
1. The backend is built on `Scala` based on the `Play Framework`.
    - I chose this framework because it avoids a lot of the boilerplate and enabled me to quickly build the backend.
    - It is based off of `Akka` which ensures that the application remains highly concurrent and resilent as it scales.
2. The frontend is build on `React` as specified by the product requirements.
    - I use `Material UI` as the main library for the UI experience.
3. Data Persistence: Achieved through `LocalStorage` in the client
    - I primarily chose to persist data in the client to simplify the design of the full application.
    - Pros:
        - No need to make an API call on page refresh.
        - No need to set up additional infranstructure/handle the cost of supporting a database.
        - Simplifies data management: don't need to deal with timestamps (for ordering) or userIDs.
    - Cons:
        - Not scalable as the data grows a lot for a single user.
4. API Design
    - User calls `localhost:9000/getWebsiteInfo?url=example.com`
    - Backend accepts the request and checks if it has seen this URL before (`TitleResultCacheManager.get(url)`)
        - If it has, return the result of `TitleResultCacheManager.get(url)` to the client
    - If it hasn't, make two Get requests to the url. One to retrieve the favicon and one to retrieve the title.
    - Wait for both of these results to return and as one result to the client.
    - Adds this result to the `TitleResultCacheManager`, so results for this url will return fast for all users.
---
## Bonus Feature
The bonus feature I developed was the intelligent API requests to websites. Whilst the backend is running, it will remember the website information of previous website it looks at through the `TitleResultCacheManager` across all clients/users. This reduces the latency of frequently requested websites by a lot. An extension would be to add a database that the backend interacts with, so it remembers even if the server has to turn off/restart.
