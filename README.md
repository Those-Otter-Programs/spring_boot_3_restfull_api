# Spring Boot 3 RESTful API
## A Spring Boot 3 RESTfull API sample
 
[![Buid Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)

## Description:
Project exemplifies the use of the following resources: 

    - It was used S.O.L.I.D., DRY, KISS, F.I.R.S.T. and other useful principles.
    - Design Patterns when reasonable to do so.
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
    - Authentication/Authorization filters.
    - Lombok
    - Mapstruct
    - OpenAPI
    - H2 database in "file-mode", having the generated persistence 
      files store on the 'data' folder at the root of the project.
    - Flyway to apply migrations to H2
    - Using yaml files for the application properties.
    - Tests and mocks:
    		Junit, Assertj, Rest Assured, Mockito, BDDMockito, Hamcrest...

## Features:

- Project was written going from a very basic, and simplistic implementation to a more 
  complex, but yet minimalistic implementation, using common Java resources/dependencies.
- System implements different profiles and creates databases structures for those 
  different profiles (test, dev, prod), using Flyway migrations and H2 (file-mode) 
  database.
- Code Coverage: on pom.xml maven-surefire-plugin and with JaCoCo have been 
  configured to run together and generate automatic reports everytime 
  'mvn test' is run.
- Mapstruct and Lombok working together along with Lombok Mapstruct Binding.

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
# ------------- JSON --------------
curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' \
	-L -X GET 'http://localhost:8080/api/member/v1/token' | jq

# Base64 encoded credentials:
curl -s -u 'YXlydG9uLnNlbm5hQGJyYXZvLmNvbTpheXJ0b25fcGFzcw=='
	-L -X GET 'http://localhost:8080/api/member/v1/token' | jq

# ------------- XML --------------
curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -H 'Accept: application/xml' \
	-L -X GET 'http://localhost:8080/api/member/v1/token' | xmllint --format -

# ------------- YAML --------------
curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -H 'Accept: application/x-yaml' \
	-L -X GET 'http://localhost:8080/api/member/v1/token' | yq

# ------------- CORS --------------
curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -H 'Origin: http://localhost:3000' \
	-L -X GET 'http://localhost:8080/api/member/v1/token' | yq
```

-- JSON response --

```bash
{
    "token": [your-jwt-token]
}
```

### To get the JWT token from the response headers:

```bash
curl -s -I -u 'ayrton.senna@bravo.com:ayrton_pass' -L -X GET 'http://localhost:8080/api/member/v1/token'

```

-- response --

```bash
HTTP/1.1 200 
Authorization: [your-jwt-toke] 
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
# BASH:

# get the JWT token and stores it in a bash variable:
myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`
       	
# run cURL using the variable as the authorization token:
curl -s -H "Authorization: $myJWTToken" -L -X GET 'http://localhost:8080/api/member/v1/member-details/ayrton.senna@bravo.com' | jq
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
# JSON response:
curl -s -L -X GET 'http://localhost:8080/api/corporation/v1' | jq

# XML response:
curl -s -H 'Accept: application/xml' \
	-L -X GET 'http://localhost:8080/api/corporation/v1' | xmllint --format -

# YAML response:
curl -s -H 'Accept: application/x-yaml' \
	-L -X GET 'http://localhost:8080/api/corporation/v1' | yq
```

**/api/corporation/v1/info**

```bash
# JSON response:
curl -s -L -X GET 'http://localhost:8080/api/corporation/v1/info' | jq

# XML response:
curl -s -H 'Accept: application/xml' \
	-L -X GET 'http://localhost:8080/api/corporation/v1/info' | xmllint --format -

# YAML response:
curl -s -H 'Accept: application/x-yaml' \
	-L -X GET 'http://localhost:8080/api/corporation/v1/info' | yq
```

**/api/corporation/v1/info-corp**

```bash
# BASH:

# get the JWT token and stores it in a bash variable:
myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`

# run cURL using the variable as the authorization token:

# ------------- JSON --------------
curl -s -H "Authorization: $myJWTToken" \
	-L -X GET 'http://localhost:8080/api/corporation/v1/info-corp' | jq

# ------------- XML ---------------
curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/xml' \
	-L -X GET 'http://localhost:8080/api/corporation/v1/info-corp' | xmllint --format -

# ------------- YAML --------------
curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/x-yaml' \
	-L -X GET 'http://localhost:8080/api/corporation/v1/info-corp' | yq

# ------------- CORS --------------
curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/json' -H 'Origin: http://localhost:3000' \
	-L -X GET 'http://localhost:8080/api/corporation/v1/info-corp' | jq
```

---

## MemberController (Description and URLs)

This controller implements routes to manage Members (Users), and also the action to generate the JWT tokens:

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

### REQUESTS (/api/member/v1/member-create):
##### First Request JWT token ---> saving token into a bash variable:

```bash

# Requesting the JWT token and storing it in a bash variable
myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`

```

##### REQUEST ---> sending JSON data:

```bash 
# JSON request and response:
curl -s -H "Authorization: $myJWTToken" -H 'Content-Type: application/json' \
    -L -X POST 'http://localhost:8080/api/member/v1/member-create' \
    -d '{
            "memberName":"Rubens Barrichello", 
            "memberEmail":"rubens.barrichello@bravo.com",
            "memberMobileNumber":"(11) 98765-4321", 
            "memberPassword": "barrichello_pass",
            "memberAuthorities": [
                "ROLE_ADMIN"
            ]
        }' | jq
```

##### REQUEST ---> sending XML data:

```bash 
# XML request and response:
curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/xml' -H 'Content-Type: application/xml' \
    -L -X POST 'http://localhost:8080/api/member/v1/member-create' \
    -d '<MemberCreateRequest>
            <memberName>Emerson Fittipaldi</memberName>
            <memberEmail>emerson.fittipaldi@bravo.com</memberEmail>
            <memberMobileNumber>(11) 98765-4321</memberMobileNumber>
            <memberPassword>fittipaldi_pass</memberPassword>
            <memberAuthorities>
                <memberAuthorities>ROLE_ADMIN</memberAuthorities>
            </memberAuthorities>
        </MemberCreateRequest>' | xmllint --format -
```

##### REQUEST ---> sending YAML data:

```bash
# YAML request and response:
curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/x-yaml' -H 'Content-Type: application/x-yaml' \
    -L -X POST 'http://localhost:8080/api/member/v1/member-create' \
    -d '---
        memberName: "Nelson Piquet"
        memberEmail: "nelson.piquet@bravo.com"
        memberMobileNumber: "(11) 98765-4321"
        memberPassword: "piquet_pass"
        memberAuthorities:
            - "ROLE_ADMIN"' | yq
```

##### REQUEST ---> with CORS origin filtering:
```bash
# CORS - origin filter and JSON request / response
curl -s -H "Authorization: $myJWTToken" -H 'Origin: http://localhost:3000' \
    -H 'Content-Type: application/json' \
    -L -X POST 'http://localhost:8080/api/member/v1/member-create' \
    -d '{
            "memberName":"Felipe Massa", 
            "memberEmail":"felipe.massa@bravo.com",
            "memberMobileNumber":"(11) 98765-4321", 
            "memberPassword": "massa_pass",
            "memberAuthorities": [
                "ROLE_ADMIN"
            ]
        }' | jq
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
