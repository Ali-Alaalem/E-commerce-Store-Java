# E-Commerce Project 3 API

A comprehensive RESTful e-commerce API built with Spring Boot, featuring secure JWT authentication, role-based access control, thread-safe inventory management with `ReentrantLock`, and complete CRUD operations for products, categories, carts, and orders.

---

## API Endpoints

### 🔐 Authentication & User Management
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| `POST` | `/auth/users/register` | Public | Register new customer |
| `POST` | `/auth/users/login` | Public | Login & receive JWT token |
| `POST` | `/auth/users/password/reset` | Public | Request password reset email |
| `POST` | `/auth/users/password/reset/submit` | Public | Submit new password with token |
| `GET` | `/api/auth/verify?token=...` | Public | Verify email address |
| `PUT` | `/auth/users/change/password` | Authenticated | Change logged-in user's password |
| `POST` | `/auth/users/imageUpdater` | CUSTOMER | Upload profile picture (form-data) |
| `PATCH` | `/auth/users/UpdateUser` | Authenticated | Update user profile |
| `DELETE` | `/auth/users/DeleteUser` | Authenticated | Soft-delete own account |
| `PUT` | `/auth/users/{{userId}}/user` | ADMIN | Soft-delete/toggle any user |

### 📦 ADMIN Operations
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| `GET` | `/api/categories` | Public | List all categories |
| `GET` | `/api/categories/category/{{categoryId}}` | ADMIN | Get category by ID |
| `POST` | `/api/categories/createCategory` | ADMIN | Create new category |
| `PUT` | `/api/categories/updateCategory/{{categoryId}}` | ADMIN | Update category |
| `DELETE` | `/api/categories/deleteCategory/{{categoryId}}` | ADMIN | Delete category |
| `GET` | `/api/products` | Public | List all products |
| `GET` | `/api/products/product/{{productId}}` | Public | Get product details |
| `POST` | `/api/products/createProduct` | ADMIN | Create product |
| `PUT` | `/api/products/updateProduct/{{productId}}` | ADMIN | Update product |
| `DELETE` | `/api/products/deleteProduct/{{productId}}` | ADMIN | Delete product |

### 🛒 CUSTOMER Operations 
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| `GET` | `/api/cart` | CUSTOMER | View user's cart |
| `POST` | `/api/cart/addToCart?productId=&quantity=` | CUSTOMER | Add item to cart |
| `DELETE` | `/api/cart/removeFromCart/{{productId}}` | CUSTOMER | Remove item from cart |
| `DELETE` | `/api/cart/clearCart` | CUSTOMER | Empty entire cart |
| `POST` | `/api/orders/checkout` | CUSTOMER | 🔒 Thread-safe checkout |
| `GET` | `/api/orders` | ADMIN | List all orders |
| `GET` | `/api/orders/order/{{orderId}}` | Authenticated | Get order details |
| `PUT` | `/api/orders/updateOrderStatus/{{orderId}}?status=` | ADMIN | Update order status |
| `DELETE` | `/api/orders/deleteOrder/{{orderId}}` | ADMIN | Delete order |

---

## Tools & Technologies

| Category | Technology |
|----------|-----------|
| **Security** | Spring Security, JWT, BCrypt |
| **Database** | PostgreSQL, Hibernate ORM |
| **Build Tool** | Maven |
| **Utilities** | Lombok, Cloudinary (file uploads), JavaMail Sender |
| **Testing** | Postman (collection included) |
| **Concurrency** | `ReentrantLock`, `ConcurrentHashMap` |
| **API Design** | RESTful conventions |

---

## Approach

The project was developed using a clean, layered architecture following the **Controller → Service → Repository → Entity** pattern. This structure ensures clear separation of concerns, improves maintainability, and makes the codebase easier to extend and test.

Security was a done with A custom **JWT-based authentication** mechanism was implemented using Spring Security, including token generation, validation, and a request filter to secure protected endpoints. Role-based access control was designed dynamically, with roles stored in the database and enforced using method-level security (`@PreAuthorize`). The overall approach emphasized clean code, strong security practices, and adherence to Spring Boot best practices while building hospital management backend.

---

## Challenges & Solutions

### 1. Concurrent Stock Updates Without Database Locks
**Problem:** Initial concurrency approach used a single global lock, blocking all product purchases when any single item was being processed.

**Solution:** Implemented fine-grained `ReentrantLock` per product ID with `ConcurrentHashMap` for lock management. Allowing concurrent purchases of different products while safely serializing access to the same product's stock.

---

## Project Links

- **User Stories**: [https://trello.com/b/dHZTYPVU/project-3](#)
- **ERD Diagram**: [https://trello.com/b/dHZTYPVU/project-3](#)
- **Planning Documentation**: [https://github.com/Ali-Alaalem/E-commerce-Store-Java](#)  

--- 
## Postman Collection

A Postman collection is included for testing all API endpoints. You can import it into Postman to quickly explore the API.

- **File location:** `In the root Dir with the name: E-Commerce_PostMan_Collection.json`
- **Environment variable:** The collection uses a Postman environment variable `{{baseUrl}}` to define the API base URL (`http://localhost:8080`). Make sure to set `{{baseUrl}}` in Postman before sending requests.
- **To use:**
    1. Open Postman → Click `Import`
    2. Select the JSON file above
    3. Set `BaseUrl` in the environment to your API URL
    4. The collection with all endpoints will be loaded and ready to test

> Ensure your environment variables for the application (email) are configured before testing the endpoints.

