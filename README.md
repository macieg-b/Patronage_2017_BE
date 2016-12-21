# Partonage 2017 - Maciej Bartłomiejczyk
It's a simple RestAPI project based on Spring Boot. It enables us to manage movies and actors repositories. It's an implementation of task one for a "backend team". 


Movies
-------
### GET
>**`/movies`** - get list of movies
>
> Response:
>- "id" : "",
>- "actors" : [], 
>- "uri" : ""

### GET
>**`/movies/{id}`** - get movie selected by id
>
> Response:
>- "id" : "",
>- "actors" : [], 
>- "uri" : ""

### POST
>**`/movies`** - create movie
>
> Input:
>- "tittle"
>
> Response:
>- "id" : "",
>- "actors" : [], 
>- "tittle" : ""

### PUT
>**`/movies/{id}`** - update movie
>
> Input:
>- "tittle"
>
> Response:
>- "id" : "",
>- "actors" : [], 
>- "tittle" : ""

### DELETE
>**`/movies/{id}`** - remove movie selected by id


Actors
-------
### GET
>**`/actors`** - get list of actors
>
> Response:
>- "id" : "",
>- "name"

### GET
>**`/actors/{id}`** - get actor selected by id
>
> Response:
>- "id" : "",
>- "name"

### POST
>**`/actors`** - create actor
>
> Input:
>- "name"
>
> Response:
>- "id" : "",
>- "name" : ""

### PUT
>**`/actors/{id}`** - update movie, allow to bind actor with movies
>
> Input:
>- "name",
>- "movieIds" : [] - optional
>
> Response:
>- "id" : "",
>- "name" : ""

### DELETE
>**`/actors/{id}`** - remove actor selected by id
