# URL

## USERS

### Create User

POST http://localhost:8080/users 

```JSON
{
    "username": "user1",
    "password": "user@1",
    "email": "user1@example.com", 
    "role": "BUYER"
}
```

### Read User

GET http://localhost:8080/users/{userid}

GET http://localhost:8080/users

### Update User

PUT http://localhost:8080/users/{userid}

```JSON
{
    "email": "user2@example.com"
}
```

### Delete User

DELETE http://localhost:8080/users/{userid}

## PROPERTIES

### Create Property

POST http://localhost:8080/properties

```JSON
{
    "title": "Peris Apartment",
    "location": "Peris",
    "type": "APARTMENT",
    "price": 700000.0,
    "status": "AVAILABLE"
}
```

### Read Property

GET http://localhost:8080/properties

### Update Property

PUT http://localhost:8080/properties/{propertyid}

```JSON
{
    "title": "NY Pent House",
    "location": "New York",
    "type": "HOUSE",
    "price": 2000000.0,
    "status": "SOLD",
}
```

### Delete Property

DELETE http://localhost:8080/properties/{propertyid}

## BOOKINGS

### Create Booking

POST http://localhost:8080/bookings

```JSON
{
    "property": {
        "id": 2
    },
    "bookingDate": "2025-04-03"
}
```

### Read Booking

GET http://localhost:8080/bookings/owner

GET http://localhost:8080/bookings/buyer

### Update Booking

PUT http://localhost:8080/bookings/{bookingId}

```JSON
{
    "bookingDate": "2025-04-04"
}
```

PUT http://localhost:8080/bookings/{bookingId}/status

```JSON
{
    "status": "CONFIRMED"
}
```

### Delete Booking

DELETE http://localhost:8080/bookings/{bookingId}
