# E-commerce Backend Project

## Description

This project is a backend assignment for the Integrify Academy Full Stack course. The task was to create an e-commerce
website, and a game key store was chosen for this purpose. The frontend implementation for this project can be
found [here](https://github.com/Redmoor19/Ecommerce-FGS-App).

Fake Game Store is a non-profit educational project with no actual products. All data is mocked, and keys are generated
using UUIDs.

The backend section was implemented by a team of three, mentioned below. During this project, I learned how to build a
complex backend for an e-commerce platform using Java (Spring Boot) and PostgreSQL. The backend development was
completed within a tight two-week deadline, simulating a real-world work scenario. This project also taught me how to
collaborate effectively within a team, where each member had specific roles and tasks.

## Table of Contents

- [Installation](#installation)
- [DevStack](#devstack)
- [Structure](#structure)
- [Usage](#usage)
- [Credits](#credits)
- [License](#license)

## Installation

- Fork this repository and clone your fork to your local machine.
- Pull all the data from your fork.
- Install OpenJDK 22.
- Build the project using Maven.

## DevStack

This project was implemented using Java (Spring Boot) and PostgreSQL. The key Java packages and frameworks used are
listed below:

- [Spring Boot Framework](https://spring.io/): The main framework for building REST APIs. It includes several packages
  for DTO validation, JPA repository management, and mailing.
- [Lombok](https://projectlombok.org/): Reduces the amount of boilerplate code.
- [Thymeleaf](https://www.thymeleaf.org/): Used for email templating.
- [Spring Security](https://spring.io/projects/spring-security): A part of the Spring Boot Framework. JWT strategy was
  used for authentication.
- [ModelMapper](https://modelmapper.org/): Used for mapping DTOs.

## Structure

This project utilizes a layered architecture
with [entities](#entities), [controllers](#controllers), [services](#services), and [repositories](#repositories).

### Entities

- **Game**: Information about the products.
- **User**: User information, including important fields for authentication.
- **Order**: Acts both as a shopping cart and a history log of orders.
- **Review**: User feedback about the games.
- **Favourites**: A bridge between the user and games, containing a whitelist of favorite games.
- **GameOrder**: A bridge between orders and games to store information about games and quantities within orders.
- **Key**: Game keys that are automatically generated using UUIDs.

### Controllers

- **Auth Controller**: Handles all requests related to authentication, such as login, signup, verification, and password
  reset.
- **Game Controller**: Handles all requests related to games, including CRUD operations, reviews, and keys.
- **Order Controller**: Handles all requests related to orders, carts, and checkouts.
- **User Controller**: Handles all requests related to users, including CRUD operations and favorites.

### Services

- **Auth Service**: Functions related to authentication.
- **Email Service**: Sends emails for password resets, verification tokens, and game keys.
- **Game Service**: All logic related to games CRUD operations.
- **JWT Service**: Functions related to JWT token authentication, authorization, encryption, and decryption.
- **Order Service**: Handles order CRUD operations, checkout, and payment.
- **User Service**: Manages user CRUD operations.
- **UserDetails Service**: A service used by Spring Security.

### Repositories

- **Game Repository**: Storage for games.
- **User Repository**: Storage for users.
- **Order Repository**: Storage for orders.
- **Review Repository**: Storage for reviews.
- **Key Repository**: Storage for keys.
- **GameOrder Repository**: Tracks games and quantities within users' orders.
- **FavouriteUserGames Repository**: Stores users' favorite games.

## Usage

As with any e-commerce project, the functionality of this REST API revolves around CRUD operations, with access roles
determining available actions.

### Users

- Authentication-related operations.
- Browse games, add to favorites, and leave reviews.
- View orders and add games to the cart.
- Update user information.

### Admins

- All operations available to users.
- Full CRUD operations on games and users.
- View orders.
- Add game keys.

## Credits

This project was created independently as an assignment for [Integrify Academy](https://www.integrify.io/).

### Collaborators

- [Viktoriia Olshanskaia](https://github.com/Olshanskaya)
- [Uladzislau Krukouski](https://github.com/GreeeenGoo)

## License

This project was created for educational purposes only. You are free to copy, edit, and contribute to it. Commercial use
is not permitted.
