## Summary

Backend technical Test v2

Postmen collection file added with necessary authentication endpoints.

Note: Stateless session management handled with combination of Cookies based approach and JWT usage (any db or in-memory lookup for sessionid-user match is not used, so this approach is still stateless). Therefore, using OpenAPI documentation's (Try it out button) is not possible (check link below). Prefer Postman for api calls.
https://swagger.io/docs/specification/authentication/cookie-authentication/

Lombok is not used at entity classes to prevent possibilities of; Accidentally Loading Lazy Attributes and Broken HashSets (and HashMaps).


