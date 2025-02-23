# Spring Boot 3 RESTful API
## A Spring Boot 3 RESTfull API sample
 
[![Buid Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)

## Description:
Project exemplifies the use of the following resources: 

	- Different environment profiles with different sets of configurations for
	  the Spring Security, Authenticated routes and databases.
	- Custom database table to store and manage the system's users, which I 
      called Members in the system.
    - HATEOAS
    - Content Negotiation (JSON, XML and YML)
    - CORS for origin filtering.
    - Automatic Code Coverage generation (maven-surefire-plugin and JaCoCo).
    - Spring Boot Security 6.4
    - Authentication based on JWT tokens
	- JWT tokens generation and validation through filters.
	- Authentication events capture.
	- Authentication filters.
    - Lombok
    - Mapstruct
    - OpenAPI
    - H2 database in "file" mode, having the generated persistence 
	  files store at the 'data' folder at the root of the project.
    - Flyway to apply migrations to H2
	- Using yaml files for the application properties.
    - Tests and mocks:
			Junit, Assertj, Rest Assured, Mockito, BDDMockito, Hamcrest...

## Features:

- Project was written from a very basic, and simplistic implementation to a more 
  complex, but yet minimalistic implementation, using common Java resources/dependencies.
- System implements different profiles and creates databases structures for those 
  different profiles (test, dev, prod), using Flyway migrations and H2 (file-mode) 
  database.
- Code Coverage: on pom.xml maven-surefire-plugin and with JaCoCo have been 
  configured to run together and generate automatic re 	 ports everytime 
  'mvn test' is run.
- Mapstruct and Lombok working together Lombok Mapstruct Binding.

---

## OpenAPI:
```bash
http://localhost:8080/swagger-ui/index.html
http://localhost:8080/v3/api-docs
```

---

## H2:

```bash
http://localhost:8080/h2
```

**H2 persistence base on environment profiles:**


- **DEV** : jdbc:h2:file:./data/test_dev_db
- **TEST** : jdbc:h2:file:./data/testdb
- **PROD** : jdbc:h2:file:./data/test_prod_db

---

## JWT Token generation and use:

```bash
 [GET] /api/member/v1/token
```

- This route requires basic authentication, and the accepted media type response 
  can be JSON (default), XML or YAML.
- The response carries the JWT token on its header, in the 'Authorization' attribute, and
  also in the body, through the property (or tag) 'token'.

### To get the JWT token from the response body:

```bash
# JSON response
$ curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -L -X GET 'http://localhost:8080/api/member/v1/token' | jq

# XML response
$ curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -H 'Accept: application/xml' -L -X GET 'http://localhost:8080/api/member/v1/token' | xmllint --format -

# YAML response
$ curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -H 'Accept: application/x-yaml' -L -X GET 'http://localhost:8080/api/member/v1/token' | yq
```

- The default JSON response body would be:

```bash
{
    "token": "eyJhbGciOiJIUzM4NCJ9.eyJpc3MiOiJzcHJpbmdfYm9vdF8zX3Jlc3RmdWxsX2FwaSIsInN1YiI6IkpXVCBUb2tlbiIsInVzZXJuYW1lIjoiYXlydG9uLnNlbm5hQGJyYXZvLmNvbSIsImF1dGhvcml0aWVzIjoiUk9MRV9BRE1JTixST0xFX0FTU09DSUFURSxST0xFX01BTkFHRVIsVklFV0lORk9DT1JQIiwiaWF0IjoxNzQwMzI1MjQ3LCJleHAiOjE3NDAzMjUzMDd9.o7hvWtWmwiM-OrabUCZB4vbF6nBDSwgyP_jci2cm97XRKIeVGxMHTIuRJOQJ7uCe"
}
```

### To get the JWT token from the response headers:

```bash
$ curl -s -I -u 'ayrton.senna@bravo.com:ayrton_pass' -L -X GET 'http://localhost:8080/api/member/v1/token'
```

- The response would be:

```bash
HTTP/1.1 200 
Authorization: eyJhbGciOiJIUzM4NCJ9.eyJpc3MiOiJzcHJpbmdfYm9vdF8zX3Jlc3RmdWxsX2FwaSIsInN1YiI6IkpXVCBUb2tlbiIsInVzZXJuYW1lIjoiYXlydG9uLnNlbm5hQGJyYXZvLmNvbSIsImF1dGhvcml0aWVzIjoiUk9MRV9BRE1JTixST0xFX0FTU09DSUFURSxST0xFX01BTkFHRVIsVklFV0lORk9DT1JQIiwiaWF0IjoxNzQwMzI1NDI0LCJleHAiOjE3NDAzMjU0ODR9.9cJECD286vmK2nThHTMw_OZssGPavf9k6M0fu7RQf2GoOmQLRRxQ6AHVsKBwsjPA
Set-Cookie: JSESSIONID=598F62A04F9B40A6D85ECCF8F853F024; Path=/; HttpOnly
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
X-Content-Type-Options: nosniff
X-XSS-Protection: 0
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sun, 23 Feb 2025 15:43:44 GMT
```

### To get the JWT token from the response body and use it on other authenticated routes (in bash): 

```bash
# get the JWT token and stores it in a bash variable:
$ myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`
       	
# run cURL using the variable as the authorization token:
$ curl -L -X GET 'http://localhost:8080/api/member/v1/member-details/ayrton.senna@bravo.com' -H "Authorization: $myJWTToken"
```

---

## InfoController (Description and URLs)
This controller implements 3 actions:

```bash
 [GET] /api/corporation/v1             - unautenticated route
 [GET] /api/corporation/v1/info        - unautenticated route
 [GET] /api/corporation/v1/info-corp   - authenticated route
```
    
- This controller implements 3 routes.
- Only Web layer tests (mocks).
- **HATEOAS** - regardless of the selected Response data format, Responses 
  include HATEOAS links to make it a RESTful API.
- **Content Negotiation** allow for Responses and Requests in JSON, XML and YML 
  data formats (**JSON by default**).


***ROUTES:***

**/api/corporation/v1**

```bash
# ----- Base64 -----
curl -s -H 'Authorization: Basic ZG9uOnZpdG9fcGFzcw==' -H 'Accept: application/json' \
        -L -X GET 'http://localhost:1110/api/corporation/v1' | jq
```

```bash 
curl -s -u 'don:vito_pass' -H 'Accept: application/json' \
        -L -X GET 'http://localhost:1110/api/corporation/v1' | jq
```

**/api/corporation/v1/info**

```bash
# ----- Base64 -----
curl -s -H 'Authorization: Basic Y29uc2lnbGllcmk6dG9tX3Bhc3M=' -H 'Accept: application/json' \
        -L -X GET 'http://localhost:1110/api/corporation/v1/info' | jq
```

```bash
curl -s -u 'consiglieri:tom_pass' -H 'Accept: application/json' \
        -L -X GET 'http://localhost:1110/api/corporation/v1/info' | jq
```

**/api/corporation/v1/info-corp**

```bash
# ----- Base64 -----
curl -s -H 'Authorization: Basic Y2Fwbzpyb2Njb19wYXNz' -H 'Accept: application/json' \
        -L -X GET 'http://localhost:1110/api/corporation/v1/info-corp' | jq
```

```bash
curl -s -u 'capo:rocco_pass' -H 'Accept: application/json' \
        -L -X GET 'http://localhost:1110/api/corporation/v1/info-corp' | jq
```

---

## MemberController (Description and URLs)

This controller implements routes to manage Members (Users), and also the action
 to generate the JWT tokens:

- This controller have the following actions, acessible by default, through:

- **HATEOAS** - regardless of the selected Response data format, Responses 
  include 'links' to make it a RESTful API.

- **Content Negotiation** allow for Responses and Requests in JSON, XML and YML 
  data formats (**JSON by default**).

```
   [GET] /api/member/v1/token
  [POST] /api/member/v1/member-create
   [GET] /api/member/v1/list 
   [GET] /api/member/v1/member-details/{username} 
   [GET] /api/member/v1/me 
   [PUT] /api/member/v1/member-update 
 [PATCH] /api/member/v1/member-password
 [PATCH] /api/member/v1/manage-member-password
 [PATCH] /api/member/v1/member-disable/{id}
 [PATCH] /api/member/v1/member-enable/{id}
```

## TO BE CONTINUED...

---
## Author: James Mallon
- [github](https://github.com/jamesmallon)
- [linkedin](https://www.linkedin.com/in/roccojamesmallon/)

## License
MIT

---
> If you want to be a lion, you must train with lions.
> -- Carlos Gracie, Sr.
