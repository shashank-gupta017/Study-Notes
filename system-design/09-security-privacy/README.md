# Phase 3: Security & Privacy

## Overview

Build secure, compliant systems that protect user data and prevent attacks.

**Duration**: Week 6 (Days 36-42)  
**Prerequisites**: All previous modules  
**Goal**: Master security patterns for production systems

---

## Module 9.1: Authentication & Authorization

### Authentication (Who are you?)

**Methods**:

**1. Password-Based**
```python
# NEVER store plain passwords!

# Bad
user.password = "mypassword123"

# Good: Hash + Salt
import bcrypt

def hash_password(password):
    salt = bcrypt.gensalt()
    hashed = bcrypt.hashpw(password.encode(), salt)
    return hashed

def verify_password(password, hashed):
    return bcrypt.checkpw(password.encode(), hashed)

# Usage
hashed = hash_password("mypassword123")
db.save_user(email, hashed)

# Login
if verify_password(input_password, stored_hash):
    return success()
```

**2. Multi-Factor Authentication (MFA)**
```python
def login_with_mfa(email, password, totp_code):
    # Step 1: Verify password
    user = db.get_user(email)
    if not verify_password(password, user.password_hash):
        return error("Invalid credentials")
    
    # Step 2: Verify TOTP (time-based one-time password)
    import pyotp
    totp = pyotp.TOTP(user.mfa_secret)
    if not totp.verify(totp_code):
        return error("Invalid MFA code")
    
    # Both valid
    return create_session(user)
```

**3. OAuth 2.0 / OpenID Connect**
```
User → "Login with Google"
    ↓
Redirect to Google
    ↓
User approves
    ↓
Google → Authorization Code
    ↓
Your App exchanges code for token
    ↓
Your App gets user info
```

```python
# OAuth flow
@app.route('/auth/google')
def google_login():
    # Redirect to Google
    return redirect(
        f"https://accounts.google.com/o/oauth2/v2/auth?"
        f"client_id={GOOGLE_CLIENT_ID}&"
        f"redirect_uri={REDIRECT_URI}&"
        f"response_type=code&"
        f"scope=openid email profile"
    )

@app.route('/auth/google/callback')
def google_callback():
    code = request.args.get('code')
    
    # Exchange code for token
    response = requests.post('https://oauth2.googleapis.com/token', data={
        'code': code,
        'client_id': GOOGLE_CLIENT_ID,
        'client_secret': GOOGLE_CLIENT_SECRET,
        'redirect_uri': REDIRECT_URI,
        'grant_type': 'authorization_code'
    })
    
    token_data = response.json()
    access_token = token_data['access_token']
    
    # Get user info
    user_info = requests.get(
        'https://www.googleapis.com/oauth2/v1/userinfo',
        headers={'Authorization': f'Bearer {access_token}'}
    ).json()
    
    # Create session
    user = get_or_create_user(user_info['email'])
    return create_session(user)
```

**4. JWT (JSON Web Tokens)**
```python
import jwt

def create_token(user_id):
    payload = {
        'user_id': user_id,
        'exp': datetime.utcnow() + timedelta(hours=24),
        'iat': datetime.utcnow()
    }
    
    token = jwt.encode(payload, SECRET_KEY, algorithm='HS256')
    return token

def verify_token(token):
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=['HS256'])
        return payload
    except jwt.ExpiredSignatureError:
        raise Unauthorized("Token expired")
    except jwt.InvalidTokenError:
        raise Unauthorized("Invalid token")

# Usage
@app.route('/api/protected')
def protected():
    token = request.headers.get('Authorization').split()[1]  # "Bearer <token>"
    payload = verify_token(token)
    user_id = payload['user_id']
    
    # Continue with authorized request
    return get_user_data(user_id)
```

---

### Authorization (What can you do?)

**1. Role-Based Access Control (RBAC)**
```python
# Define roles
ROLES = {
    'admin': ['read', 'write', 'delete'],
    'editor': ['read', 'write'],
    'viewer': ['read']
}

def require_permission(permission):
    def decorator(func):
        def wrapper(*args, **kwargs):
            user = get_current_user()
            if permission not in ROLES[user.role]:
                return error(403, "Forbidden")
            return func(*args, **kwargs)
        return wrapper
    return decorator

@app.route('/api/posts/<post_id>', methods=['DELETE'])
@require_permission('delete')
def delete_post(post_id):
    # Only admins can delete
    db.delete_post(post_id)
    return success()
```

**2. Attribute-Based Access Control (ABAC)**
```python
def can_access(user, resource, action):
    # Check multiple attributes
    rules = [
        # Rule 1: Admins can do anything
        user.role == 'admin',
        
        # Rule 2: Owner can edit own posts
        action == 'edit' and resource.owner_id == user.id,
        
        # Rule 3: Public posts can be viewed by anyone
        action == 'view' and resource.visibility == 'public',
        
        # Rule 4: Department members can view department posts
        action == 'view' and user.department == resource.department
    ]
    
    return any(rules)

@app.route('/api/posts/<post_id>')
def view_post(post_id):
    user = get_current_user()
    post = db.get_post(post_id)
    
    if not can_access(user, post, 'view'):
        return error(403, "Forbidden")
    
    return post
```

---

## Module 9.2: Data Encryption

### Encryption at Rest

**Database Encryption**:
```python
from cryptography.fernet import Fernet

# Generate key (store securely!)
key = Fernet.generate_key()
cipher = Fernet(key)

# Encrypt sensitive data
def save_user(email, ssn):
    encrypted_ssn = cipher.encrypt(ssn.encode())
    
    db.insert({
        'email': email,
        'ssn_encrypted': encrypted_ssn  # Stored encrypted
    })

# Decrypt when needed
def get_user_ssn(email):
    user = db.get_user(email)
    ssn = cipher.decrypt(user.ssn_encrypted).decode()
    return ssn
```

**File Encryption**:
```python
# Encrypt files before storing (S3, disk)
def upload_file(file_data, filename):
    # Encrypt
    encrypted_data = cipher.encrypt(file_data)
    
    # Store
    s3.put_object(
        Bucket='my-bucket',
        Key=filename,
        Body=encrypted_data
    )

def download_file(filename):
    # Retrieve
    encrypted_data = s3.get_object(
        Bucket='my-bucket',
        Key=filename
    )['Body'].read()
    
    # Decrypt
    data = cipher.decrypt(encrypted_data)
    return data
```

---

### Encryption in Transit (TLS/SSL)

**HTTPS Configuration**:
```python
# Flask with HTTPS
from flask import Flask
app = Flask(__name__)

if __name__ == '__main__':
    app.run(
        ssl_context=(
            'cert.pem',    # Certificate
            'key.pem'      # Private key
        ),
        host='0.0.0.0',
        port=443
    )
```

**Force HTTPS**:
```python
@app.before_request
def force_https():
    if not request.is_secure:
        url = request.url.replace('http://', 'https://', 1)
        return redirect(url, code=301)
```

**Security Headers**:
```python
@app.after_request
def set_security_headers(response):
    response.headers['Strict-Transport-Security'] = 'max-age=31536000; includeSubDomains'
    response.headers['X-Content-Type-Options'] = 'nosniff'
    response.headers['X-Frame-Options'] = 'DENY'
    response.headers['X-XSS-Protection'] = '1; mode=block'
    response.headers['Content-Security-Policy'] = "default-src 'self'"
    return response
```

---

## Module 9.3: Common Attacks & Prevention

### 1. SQL Injection

**Vulnerable**:
```python
# DON'T DO THIS!
query = f"SELECT * FROM users WHERE email = '{email}'"
db.execute(query)

# Attack: email = "' OR '1'='1"
# Result: SELECT * FROM users WHERE email = '' OR '1'='1'
# Returns all users!
```

**Safe**:
```python
# Use parameterized queries
query = "SELECT * FROM users WHERE email = ?"
db.execute(query, [email])

# Or ORM
user = User.query.filter_by(email=email).first()
```

---

### 2. Cross-Site Scripting (XSS)

**Vulnerable**:
```html
<!-- User input directly in HTML -->
<div>Welcome, {{ user.name }}</div>

<!-- Attack: user.name = "<script>alert('XSS')</script>" -->
<!-- Result: Script executes! -->
```

**Safe**:
```html
<!-- Auto-escape (most frameworks do this by default) -->
<div>Welcome, {{ user.name | escape }}</div>

<!-- Result: &lt;script&gt;alert('XSS')&lt;/script&gt; -->
<!-- Displayed as text, not executed -->
```

```python
import html

# Sanitize user input
safe_name = html.escape(user_input)
```

---

### 3. Cross-Site Request Forgery (CSRF)

**Attack**:
```html
<!-- Malicious site -->
<img src="https://bank.com/transfer?to=attacker&amount=1000">
<!-- If user logged into bank.com, request succeeds! -->
```

**Prevention**:
```python
# Generate CSRF token
from flask_wtf.csrf import CSRFProtect

csrf = CSRFProtect(app)

@app.route('/transfer', methods=['POST'])
@csrf.protect()  # Validates CSRF token
def transfer():
    # Process transfer
    pass
```

```html
<!-- Include CSRF token in forms -->
<form method="POST" action="/transfer">
    <input type="hidden" name="csrf_token" value="{{ csrf_token() }}">
    <input type="text" name="amount">
    <button type="submit">Transfer</button>
</form>
```

---

### 4. Injection Attacks

**Command Injection**:
```python
# Vulnerable
os.system(f"ping {user_input}")

# Attack: user_input = "google.com; rm -rf /"

# Safe: Validate and sanitize
import shlex
import subprocess

def ping(host):
    # Whitelist validation
    if not re.match(r'^[a-zA-Z0-9.-]+$', host):
        raise ValueError("Invalid host")
    
    # Use subprocess with list (no shell)
    subprocess.run(['ping', '-c', '1', host], check=True)
```

---

### 5. Denial of Service (DoS)

**Prevention Strategies**:

**Rate Limiting**:
```python
# See Module 8.2 for detailed implementation
@app.route('/api/expensive-operation')
@rate_limit(limit=10, period=60)  # 10 requests per minute
def expensive_operation():
    # Process
    pass
```

**Request Size Limits**:
```python
# Flask
app.config['MAX_CONTENT_LENGTH'] = 16 * 1024 * 1024  # 16 MB max

@app.errorhandler(413)
def request_entity_too_large(error):
    return error(413, "File too large")
```

**Timeout Limits**:
```python
# Gunicorn configuration
timeout = 30  # Kill requests taking > 30s
```

**CDN & DDoS Protection**:
- Use Cloudflare, AWS Shield
- Geo-blocking
- IP filtering

---

## Module 9.4: API Security

### API Key Management

**Generation**:
```python
import secrets

def generate_api_key():
    # Cryptographically secure random
    api_key = secrets.token_urlsafe(32)
    
    # Hash before storing
    key_hash = bcrypt.hashpw(api_key.encode(), bcrypt.gensalt())
    
    db.save_api_key(user_id, key_hash)
    
    # Return once (user must save it)
    return api_key
```

**Validation**:
```python
@app.before_request
def validate_api_key():
    api_key = request.headers.get('X-API-Key')
    
    if not api_key:
        return error(401, "API key required")
    
    # Find key in database
    stored_hash = db.get_api_key_hash(api_key[:8])  # Lookup by prefix
    
    if not stored_hash or not bcrypt.checkpw(api_key.encode(), stored_hash):
        return error(401, "Invalid API key")
```

---

### Input Validation

**Never Trust User Input**:
```python
from pydantic import BaseModel, validator

class UserCreate(BaseModel):
    email: str
    age: int
    username: str
    
    @validator('email')
    def validate_email(cls, v):
        if not re.match(r'^[\w\.-]+@[\w\.-]+\.\w+$', v):
            raise ValueError('Invalid email')
        return v
    
    @validator('age')
    def validate_age(cls, v):
        if not 0 <= v <= 150:
            raise ValueError('Invalid age')
        return v
    
    @validator('username')
    def validate_username(cls, v):
        if not re.match(r'^[a-zA-Z0-9_]{3,20}$', v):
            raise ValueError('Invalid username')
        return v

@app.route('/api/users', methods=['POST'])
def create_user():
    try:
        user_data = UserCreate(**request.json)
        # Validated!
        create_user_in_db(user_data)
    except ValidationError as e:
        return error(400, str(e))
```

---

## Module 9.5: Privacy & Compliance

### GDPR (General Data Protection Regulation)

**Key Requirements**:

**1. Right to be Forgotten**:
```python
@app.route('/api/users/<user_id>', methods=['DELETE'])
def delete_user(user_id):
    # Delete user data
    db.delete_user(user_id)
    
    # Delete from all systems
    analytics.delete_user_data(user_id)
    logs.anonymize_user_logs(user_id)
    backups.mark_for_deletion(user_id)
    
    # Notify downstream systems
    event_bus.publish('user_deleted', {'user_id': user_id})
    
    return success()
```

**2. Data Export (Portability)**:
```python
@app.route('/api/users/<user_id>/export')
def export_user_data(user_id):
    # Collect all user data
    data = {
        'profile': db.get_user(user_id),
        'posts': db.get_user_posts(user_id),
        'comments': db.get_user_comments(user_id),
        'likes': db.get_user_likes(user_id),
    }
    
    # Return as JSON
    return jsonify(data)
```

**3. Consent Management**:
```python
class ConsentManager:
    def record_consent(self, user_id, purpose, granted):
        db.insert_consent({
            'user_id': user_id,
            'purpose': purpose,  # e.g., 'marketing', 'analytics'
            'granted': granted,
            'timestamp': now()
        })
    
    def can_process(self, user_id, purpose):
        consent = db.get_consent(user_id, purpose)
        return consent and consent.granted

# Usage
@app.route('/api/send-marketing-email')
def send_marketing():
    if not consent_manager.can_process(user_id, 'marketing'):
        return error(403, "User has not consented to marketing")
    
    send_email(user_id, marketing_content)
```

---

### PII (Personally Identifiable Information) Handling

**Minimize Collection**:
```python
# Bad: Collect everything
user = {
    'email': email,
    'ssn': ssn,
    'credit_card': cc,
    'address': address,
    # ...
}

# Good: Collect only what's needed
user = {
    'email': email,  # Needed for login
    # Don't store SSN unless absolutely required
}
```

**Data Masking**:
```python
def mask_email(email):
    username, domain = email.split('@')
    masked_username = username[0] + '*' * (len(username) - 2) + username[-1]
    return f"{masked_username}@{domain}"

def mask_phone(phone):
    return '*' * (len(phone) - 4) + phone[-4:]

# In logs
logger.info(f"User {mask_email(user.email)} logged in")
```

**Anonymization**:
```python
def anonymize_user_data(user_id):
    db.update_user(user_id, {
        'email': f"deleted_{user_id}@anonymized.com",
        'name': f"Deleted User {user_id}",
        'phone': None,
        'address': None,
        'deleted_at': now()
    })
```

---

## Practical Exercises

### Exercise 1: Authentication System

Design authentication for a web app:
- Support email/password and OAuth (Google, GitHub)
- Implement MFA
- Session management
- Password reset flow

---

### Exercise 2: API Security

Secure a public API:
- API key authentication
- Rate limiting (different tiers)
- Input validation
- CORS configuration

---

### Exercise 3: GDPR Compliance

Implement GDPR requirements:
- User data export
- Account deletion
- Consent management
- Data retention policies

---

## Key Takeaways

1. **Authentication**: Hash passwords, use MFA, consider OAuth
2. **Authorization**: RBAC or ABAC based on needs
3. **Encryption**: At rest and in transit (TLS/SSL)
4. **Input Validation**: Never trust user input
5. **Common Attacks**: Prevent SQL injection, XSS, CSRF
6. **Privacy**: GDPR compliance, PII handling, anonymization

---

## Security Checklist

- [ ] Passwords hashed with bcrypt/argon2
- [ ] HTTPS everywhere (TLS 1.3+)
- [ ] Input validation on all endpoints
- [ ] SQL injection prevention (parameterized queries)
- [ ] XSS prevention (escape output)
- [ ] CSRF tokens on state-changing operations
- [ ] Rate limiting implemented
- [ ] Security headers set
- [ ] API keys secured
- [ ] Sensitive data encrypted at rest
- [ ] Logging without PII
- [ ] Regular security audits

---

## Self-Assessment

- [ ] Can implement secure authentication
- [ ] Understand OAuth 2.0 flow
- [ ] Can prevent common attacks (SQL injection, XSS, CSRF)
- [ ] Can implement encryption at rest and in transit
- [ ] Understand GDPR requirements
- [ ] Can handle PII securely

---

## Next Module

[10-classic-designs](../10-classic-designs/README.md) - Apply everything to real system designs

---

**Remember**: Security is not a feature, it's a requirement. Design with security from day one.
