## user-service

### Purpose
Handles user profiles, roles and personal data.
Accepts only internal service JWTs issued by auth-service.

### Run locally
1. Start PostgreSQL
2. Ensure auth-service exposes JWKS at http://localhost:9000/.well-known/jwks.json
3. Run:
   mvn spring-boot:run

### Security
- JWT validation via JWKS
- Required scope: internal