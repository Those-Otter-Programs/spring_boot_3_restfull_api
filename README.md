# Spring Boot 3 RESTful API
## A Spring Boot 3 RESTfull API sample
 
[![Buid Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)

[Project repository](https://github.com/Those-Otter-Programs/spring_boot_3_restfull_api)

## Description:
Project exemplifies the use of the following resources: 

    - It was used SOLID, DRY, KISS, FIRST and other useful principles.
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

- The project evolved from a basic and simplistic implementation to a more complex yet minimalistic monolithic implementation, utilizing the latest versions of common Java resources and dependencies.
- The objective was to test the new versions of the dependencies mentioned earlier by applying them to a micro-monolithic application. This test did not take into account concurrent or parallel access to the application's resources, particularly the database.
- The decision to use the H2 database was based on its practicality. H2 is a relatively simple database, primarily used for testing. However, for a micro application like this, it allows us to complete tasks without the need to configure or run additional services or containers. It is portable and easy to manage.
- The system creates database structures for different profiles (test, dev, prod) using Flyway migrations and an H2 (file-mode) database.
- In the pom.xml file, the maven-surefire-plugin has been configured to work in conjunction with JaCoCo, generating automatic reports whenever 'mvn test' is executed.
- MapStruct and Lombok working together, with the integration of Lombok’s MapStruct binding features.

---

## Controllers

- [InfoController](#infocontroller)
- [MemberController](#membercontroller)
- [AuthenticationFailureLogController](#authenticationfailurelogcontroller)

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

### To get the JWT token from the response body and use it in other authenticated routes: 

```bash
# BASH:

# get the JWT token and stores it in a bash variable:
myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' \
    -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`
        
# run cURL using the variable as the authorization token:
curl -s -H "Authorization: $myJWTToken" \
    -L -X GET 'http://localhost:8080/api/member/v1/member-details/ayrton.senna@bravo.com' | jq
```

---

## InfoController

This controller implements 3 actions:

```bash
 [GET] /api/corporation/v1             - unautenticated route
 [GET] /api/corporation/v1/info        - unautenticated route
 [GET] /api/corporation/v1/info-corp   - authenticated route
```
    
- This controller implements 3 routes.
- Only Web layer tests.
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
myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' \
    -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`

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

## MemberController

This controller implements routes to manage Members (Users), and also the action to generate the JWT tokens:

- **HATEOAS** - regardless of the selected Response data format, Responses 
  include 'links' to make it a RESTful API.

- **Content Negotiation** allow for Responses and Requests in JSON, XML and YML 
  data formats (**JSON by default**).

- **Pagination** - paginated list of members on /api/member/v1/list, with page number, size (quantity), and sorting direction 

```
   [GET] /api/member/v1/token
  [POST] /api/member/v1/member-create
   [GET] /api/member/v1/list 
   [GET] /api/member/v1/member-full-details/{username} 
   [GET] /api/member/v1/member-details/{username} 
   [GET] /api/member/v1/me 
   [PUT] /api/member/v1/member-update 
 [PATCH] /api/member/v1/member-password
 [PATCH] /api/member/v1/manage-member-password
 [PATCH] /api/member/v1/member-disable/{id}
 [PATCH] /api/member/v1/member-enable/{id}
 [PATCH] /api/member/v1/member-lock/{id}
 [PATCH] /api/member/v1/member-unlock/{id}
```

### REQUESTS /api/member/v1/member-create:
##### Requesting the JWT token and saving it into a bash variable:

```bash

# Requesting the JWT token and storing it in a bash variable
myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' \
    -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`

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

### REQUESTS /api/member/v1/list:

Paginated params for this request:

- **page**: *(default: 0)* the page number to be shown (determined by the quantity of members in the database divided by the value of the '**size**' param, which is explained bellow)
- **size**: *(default: 8)* the quantity of members to be showed per page
- **sortDir**: *(default: asc)* the sorting direction, if asc or desc.
- **sortBy**: *(default: memberEmail)* the data wich the sorting must be based (memberEmail, memberId, and so on..)

##### OBSERVATIONS:

**sortDir:** 
It selects 'asc' (ascending) sort direction, in case an invalid sorting direction is received.

**sortBy:** 
It throws a custom exception (InvalidSortByException) in case the value is not a member data, which would make it an unsortable data.


```bash

# Requesting the JWT token and storing it in a bash variable
myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' \
    -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`

# run cURL using the variable as the authorization token:

# ------------- JSON - PAGINATED --------------
curl -s -H "Authorization: $myJWTToken" 
    -L -X GET 'http://localhost:8080/api/member/v1/list?page=0&size=8&sortDir=desc&sortBy=memberId' | jq

curl -s -H "Authorization: $myJWTToken" 
    -L -X GET 'http://localhost:8080/api/member/v1/list?page=0&size=8&sortDir=desc' | jq

curl -s -H "Authorization: $myJWTToken" 
    -L -X GET 'http://localhost:8080/api/member/v1/list?page=0&size=8' | jq

curl -s -H "Authorization: $myJWTToken" 
    -L -X GET 'http://localhost:8080/api/member/v1/list?page=0' | jq

curl -s -H "Authorization: $myJWTToken" 
-L -X GET 'http://localhost:8080/api/member/v1/list' | jq

# -------------- XML - PAGINATED --------------
curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/xml' \
    -L -X GET 'http://localhost:8080/api/member/v1/list?page=0&size=8&sortDir=desc&sortBy=memberId' | xmllint --format -

# ------------- YAML - PAGINATED --------------
curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/x-yaml' \
    -L -X GET 'http://localhost:8080/api/member/v1/list?page=0&size=8&sortDir=desc&sortBy=memberId' | yq

# ------------- CORS - PAGINATED --------------
curl -s -H "Authorization: $myJWTToken" -H 'Origin: http://localhost:3000' \
    -L -X GET 'http://localhost:8080/api/member/v1/list?page=0&size=8&sortDir=desc&sortBy=memberId' | jq

```

### REQUESTS /api/member/v1/member-full-details/{username} 


```bash
# Requesting the JWT token and storing it in a bash variable
myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' \
    -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`

# run cURL using the variable as the authorization token:

# ------------- JSON --------------
curl -s -H "Authorization: $myJWTToken" \
    -L -X GET 'http://localhost:8080/api/member/v1/member-full-details/ayrton.senna@bravo.com' | jq

# ------------- XML --------------
curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/xml' \
    -L -X GET 'http://localhost:8080/api/member/v1/member-full-details/ayrton.senna@bravo.com' \
    | xmllint --format -

# ------------- YAML --------------
curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/x-yaml' \
    -L -X GET 'http://localhost:8080/api/member/v1/member-full-details/ayrton.senna@bravo.com' | yq

# ------------- CORS --------------
curl -s -H "Authorization: $myJWTToken" -H 'Origin: http://localhost:3000' \
    -L -X GET 'http://localhost:8080/api/member/v1/member-full-details/ayrton.senna@bravo.com' | jq
```

### REQUESTS /api/member/v1/member-details/{username} 


```bash
# Requesting the JWT token and storing it in a bash variable
myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' \
    -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`

# run cURL using the variable as the authorization token:

# ------------- JSON --------------
curl -s -H "Authorization: $myJWTToken" \
    -L -X GET 'http://localhost:8080/api/member/v1/member-details/ayrton.senna@bravo.com' | jq

# ------------- XML --------------
curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/xml' \
    -L -X GET 'http://localhost:8080/api/member/v1/member-details/ayrton.senna@bravo.com' \
    | xmllint --format -

# ------------- YAML --------------
curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/x-yaml' \
    -L -X GET 'http://localhost:8080/api/member/v1/member-details/ayrton.senna@bravo.com' | yq

# ------------- CORS --------------
curl -s -H "Authorization: $myJWTToken" -H 'Origin: http://localhost:3000' \
    -L -X GET 'http://localhost:8080/api/member/v1/member-details/ayrton.senna@bravo.com' | jq
```

### REQUESTS /api/member/v1/me 

```bash
# Requesting the JWT token and storing it in a bash variable
myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' \
    -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`

# run cURL using the variable as the authorization token:

# ------------- JSON --------------
curl -s -H "Authorization: $myJWTToken" \
    -L -X GET 'http://localhost:8080/api/member/v1/me' | jq

# ------------- XML --------------
curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/xml' \
    -L -X GET 'http://localhost:8080/api/member/v1/me' | xmllint --format -

# ------------- YAML --------------
curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/x-yaml' \
    -L -X GET 'http://localhost:8080/api/member/v1/me' | yq

# ------------- CORS --------------
curl -s -H "Authorization: $myJWTToken" -H 'Origin: http://localhost:3000' \
    -L -X GET 'http://localhost:8080/api/member/v1/me' | jq
```

### REQUESTS /api/member/v1/member-update 

##### Requesting the JWT token and saving it into a bash variable:

```bash
# Requesting the JWT token and storing it in a bash variable
myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' \
    -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`
```

##### REQUEST ---> sending JSON data:

```bash 
# ------------- JSON (request and response) --------------
curl -s -H "Authorization: $myJWTToken" -H 'Content-Type: application/json' \
    -L -X PUT 'http://localhost:8080/api/member/v1/member-update' \
    -d '{
            "membrerId": 51,
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
# -------------- XML (request and response) --------------
curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/xml' \
    -H 'Content-Type: application/xml' \
    -L -X PUT 'http://localhost:8080/api/member/v1/member-update' \
    -d '<MemberCreateRequest>
            <memberId>52</memberId>
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
# ------------- YAML (request and response) --------------
curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/x-yaml' \
    -H 'Content-Type: application/x-yaml' \
    -L -X PUT 'http://localhost:8080/api/member/v1/member-update' \
    -d '---
        memberId: 53
        memberName: "Nelson Piquet"
        memberEmail: "nelson.piquet@bravo.com"
        memberMobileNumber: "(11) 98765-4321"
        memberPassword: "piquet_pass"
        memberAuthorities:
            - "ROLE_ADMIN"' | yq
```

##### REQUEST ---> with CORS origin filtering:
```bash
# ------------- CORS - origin filter and JSON request / response -------------
curl -s -H "Authorization: $myJWTToken" -H 'Origin: http://localhost:3000' \
    -H 'Content-Type: application/json' \
    -L -X PUT 'http://localhost:8080/api/member/v1/member-update' \
    -d '{
            "memberId": 54,
            "memberName":"Felipe Massa", 
            "memberEmail":"felipe.massa@bravo.com",
            "memberMobileNumber":"(11) 98765-4321", 
            "memberPassword": "massa_pass",
            "memberAuthorities": [
                "ROLE_ADMIN"
            ]
        }' | jq
```

### REQUESTS /api/member/v1/member-password

##### Requesting the JWT token and saving it into a bash variable:

```bash
# Requesting the JWT token and storing it in a bash variable
myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' \
    -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`
```

##### REQUEST ---> [PATCH] Requesting JSON data:

```bash
curl -s -H "Authorization: $myJWTToken" \
    -L -X PATCH 'http://localhost:8080/api/member/v1/member-password' \
    -d '{"newPassword": "mynewpassword"}' | jq
```

##### REQUEST ---> [PATCH] Requesting XML data:

```bash
curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/xml' -H 'Content-Type: application/xml' \
    -L -X PATCH 'http://localhost:8080/api/member/v1/member-password' \
    -d '<MemberUpdatePasswordRequest>
       	    <newPassword>mynewpassword</newPassword>
       	</MemberUpdatePasswordRequest>' | xmllint --format -
```

##### REQUEST ---> [PATCH] Requesting YAMLL data:

```bash
curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/x-yaml' -H 'Content-Type: application/x-yaml' \
    -L -X PATCH 'http://localhost:8080/api/member/v1/member-password' \
    -d '--- newPassword: "mynewpassword"' | yq
```

##### REQUEST ---> [PATCH] Requesting JSON data with CORS origin filtering:

```bash
curl -s -H "Authorization: $myJWTToken" -H 'Origin: http://localhost:3000' \
    -L -X PATCH 'http://localhost:8080/api/member/v1/member-password' \
    -d '{"newPassword": "mynewpassword"}' | jq
```

### REQUESTS /api/member/v1/manage-member-password

##### Requesting the JWT token and saving it into a bash variable:

```bash
# Requesting the JWT token and storing it in a bash variable
myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' \
    -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`
```

##### REQUEST ---> [PATCH] Sending and requesting JSON data:

```bash 
curl -s -H "Authorization: $myJWTToken" \
    -L -X PATCH 'http://localhost:8080/api/member/v1/manage-member-password' \
    -d '{"memberUsername": "mfredson2@amazon.com", "memberPassword": "newpassword"}' | jq

```

##### REQUEST ---> [PATCH] Sending JSON data and requesting XML data:

```bash 
curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/xml' \
    -H 'Content-Type: application/xml' \
    -L -X PATCH 'http://localhost:8080/api/member/v1/manage-member-password' \
    -d '<MemberManagePasswordRequest>
       		<memberUsername>mfredson2@amazon.com</memberUsername>
       		<memberPassword>newpassword</memberPassword>
       	</MemberManagePasswordRequest>' | xmllint --format -
```
       
##### REQUEST ---> [PATCH] Sending JSON data and requesting YAML data:

```bash 
curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/x-yaml' \
    -H 'Content-Type: application/x-yaml' \
    -L -X PATCH 'http://localhost:8080/api/member/v1/manage-member-password' \
    -d '---
        memberUsername: "Nelson Piquet"
        memberPassword: "newpassword"' | yq
```
       
##### REQUEST ---> [PATCH] Sending and requesting JSON data with CORS origin filtering:

```bash 
curl -s -H "Authorization: $myJWTToken" -H 'Origin: http://localhost:3000' \
    -L -X GET 'http://localhost:8080/api/member/v1/manage-member-password' \
    -d '{"memberUsername": "mfredson2@amazon.com", "memberPassword": "newpassword"}' \
    | jq
```

### REQUESTS /api/member/v1/member-disable/{id}

```bash
# Requesting the JWT token and storing it in a bash variable
myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' \
    -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`

# run cURL using the variable as the authorization token:

# ------------- JSON --------------
curl -s -H "Authorization: $myJWTToken" \
    -L -X PATCH 'http://localhost:8080/api/member/v1/member-disable/3' | jq

# ------------- XML --------------
curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/xml' \
    -L -X PATCH 'http://localhost:8080/api/member/v1/member-disable/3' | xmllint --format -

# ------------- YAML --------------
curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/x-yaml' \
    -L -X PATCH 'http://localhost:8080/api/member/v1/member-disable/3' | yq

# ------------- CORS --------------
curl -s -H "Authorization: $myJWTToken" -H 'Origin: http://localhost:3000' \
    -L -X PATCH 'http://localhost:8080/api/member/v1/member-disable/3' | jq
```

### REQUESTS /api/member/v1/member-enable/{id}

```bash
# Requesting the JWT token and storing it in a bash variable
myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' \
    -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`

# ------------- JSON --------------
curl -s -H "Authorization: $myJWTToken" \
    -L -X PATCH 'http://localhost:8080/api/member/v1/member-enable/3' | jq

# -------------- XML --------------
curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/xml' \
    -L -X PATCH 'http://localhost:8080/api/member/v1/member-enable/3' | xmllint --format -

# ------------- YAML --------------
curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/x-yaml' \
    -L -X PATCH 'http://localhost:8080/api/member/v1/member-enable/3' | yq

# ------------- CORS --------------
curl -s -H "Authorization: $myJWTToken" -H 'Origin: http://localhost:3000' \
    -L -X PATCH 'http://localhost:8080/api/member/v1/member-enable/3' | jq
```

### REQUESTS /api/member/v1/member-lock/{id}

```bash
# Requesting the JWT token and storing it in a bash variable
myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' \
    -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`

# ------------- JSON --------------
curl -s -H "Authorization: $myJWTToken" \
    -L -X PATCH 'http://localhost:8080/api/member/v1/member-lock/3' | jq

# -------------- XML --------------
curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/xml' \
    -L -X PATCH 'http://localhost:8080/api/member/v1/member-lock/3' | xmllint --format -

# ------------- YAML --------------
curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/x-yaml' \
    -L -X PATCH 'http://localhost:8080/api/member/v1/member-lock/3' | yq

# ------------- CORS --------------
curl -s -H "Authorization: $myJWTToken" \
    -H 'Origin: http://localhost:3000' \
    -L -X PATCH 'http://localhost:8080/api/member/v1/member-lock/3' | jq
```

### REQUESTS /api/member/v1/member-unlock/{id}

```bash
# Requesting the JWT token and storing it in a bash variable
myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' \
    -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`

# ------------- JSON --------------
curl -s -H "Authorization: $myJWTToken" \
    -L -X PATCH 'http://localhost:8080/api/member/v1/member-unlock/3' | jq

# -------------- XML --------------
curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/xml' \
    -L -X PATCH 'http://localhost:8080/api/member/v1/member-unlock/3' | xmllint --format -

# ------------- YAML --------------
curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/x-yaml' \
    -L -X PATCH 'http://localhost:8080/api/member/v1/member-unlock/3' | yq

# ------------- CORS --------------
curl -s -H "Authorization: $myJWTToken" \
    -H 'Origin: http://localhost:3000' \
    -L -X PATCH 'http://localhost:8080/api/member/v1/member-unlock/3' | jq
```

## AuthenticationFailureLogController

This controller implements routes to manage Members (Users), and also the action to generate the JWT tokens:

- **HATEOAS** - regardless of the selected Response data format, Responses 
  include 'links' to make it a RESTful API.

- **Content Negotiation** allow for Responses and Requests in JSON, XML and YML 
  data formats (**JSON by default**).

- **Pagination** - paginated list of members on /api/authentication-failure/v1/member/{username}, with page number, size (quantity), and sorting direction 

```
   [GET] /api/authentication-failure/v1/member/{username}
   [GET] /api/authentication-failure/v1/log/{id} 
```

### REQUESTS /api/authentication-failure/v1/member/{username}:

Paginated params for this request:

- **page**: *(default: 0)* the page number to be shown (determined by the quantity of a member's logs in the database divided by the value of the '**size**' param, which is explained bellow)
- **size**: *(default: 8)* the quantity of a member's authentication failure logs to be showed per page
- **sortDir**: *(default: asc)* the sorting direction, if asc or desc.
- **sortBy**: *(default: memberEmail)* the data wich the sorting must be based (memberEmail, memberId, and so on..)

##### OBSERVATIONS:

**sortDir:** 
It selects 'asc' (ascending) sort direction, in case an invalid sorting direction is received.

**sortBy:** 
It throws a custom exception (InvalidSortByException) in case the value is not a member's log data, which would make it an unsortable data.


```bash

# Requesting the JWT token and storing it in a bash variable
myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' \
    -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`

# run cURL using the variable as the authorization token:

# ------------- JSON - PAGINATED --------------
curl -s -H "Authorization: $myJWTToken" \
    -L -X GET 'http://localhost:8080/api/authentication-failure/v1/member/ayrton.senna@bravo.com?page=0&size=8&sortDir=desc&sortBy=logAuthTime' | jq
    
curl -s -H "Authorization: $myJWTToken" \
    -L -X GET 'http://localhost:8080/api/authentication-failure/v1/member/ayrton.senna@bravo.com?page=0&size=8&sortDir=desc' | jq

curl -s -H "Authorization: $myJWTToken" \
    -L -X GET 'http://localhost:8080/api/authentication-failure/v1/member/ayrton.senna@bravo.com?page=0&size=8' | jq
    
curl -s -H "Authorization: $myJWTToken" \
    -L -X GET 'http://localhost:8080/api/authentication-failure/v1/member/ayrton.senna@bravo.com?page=0' | jq
    
curl -s -H "Authorization: $myJWTToken" \
    -L -X GET 'http://localhost:8080/api/authentication-failure/v1/member/ayrton.senna@bravo.com' | jq

# -------------- XML - PAGINATED --------------
curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/xml' \
    -L -X GET 'http://localhost:8080/api/authentication-failure/v1/member/ayrton.senna@bravo.com?page=0&size=8&sortDir=desc&sortBy=logAuthTime' \
    | xmllint --format -

# ------------- YAML - PAGINATED --------------
curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/x-yaml' \
    -L -X GET 'http://localhost:8080/api/authentication-failure/v1/member/ayrton.senna@bravo.com?page=0&size=8&sortDir=desc&sortBy=logAuthTime' \
    | yq

# ------------- CORS - PAGINATED --------------
curl -s -H "Authorization: $myJWTToken" -H 'Origin: http://localhost:3000' \
    -L -X GET 'http://localhost:8080/api/authentication-failure/v1/member/ayrton.senna@bravo.com?page=0&size=8&sortDir=desc&sortBy=logAuthTime' \
    | jq

```

### REQUESTS /api/authentication-failure/v1/log/{id}

```bash
# Requesting the JWT token and storing it in a bash variable
myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' \
    -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`

# run cURL using the variable as the authorization token:

# ------------- JSON --------------
curl -s -H "Authorization: $myJWTToken" \
    -L -X GET 'http://localhost:8080/api/authentication-failure/v1/log/1' | jq

# -------------- XML --------------
curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/xml' \
    -L -X GET 'http://localhost:8080/api/authentication-failure/v1/log/1' | xmllint --format -

# ------------- YAML --------------
curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/x-yaml' \
    -L -X GET 'http://localhost:8080/api/authentication-failure/v1/log/1' | yq

# ------------- CORS --------------
curl -s -H "Authorization: $myJWTToken" -H 'Origin: http://localhost:3000' \
    -L -X GET 'http://localhost:8080/api/authentication-failure/v1/log/1' | jq
```

---

## TO BE CONTINUED...
---

## Author: James Mallon
- [github](https://github.com/jamesmallon)
- [linkedin](https://www.linkedin.com/in/roccojamesmallon/)

## License
[Apache 2.0](LICENSE)

---
### Have a drink...

![Have a good one](src/main/resources/imgs/cool_otter.jpg)

---
> If you want to be a lion, you must train with lions.
> -- Carlos Gracie, Sr