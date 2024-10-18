Schwarz Java Code Review
========================

## Global overview

Current application architecture it is similar to DDD (Domain Driven Design) as
the structure organize the code base on core/domain and core/services. For
other side there is an abstraction of the repositories.
The architecture also is showing a separation of concerns( configuration, 
web and domain)

The current application architecture resembles Domain-Driven Design (DDD),
with a codebase structure organized around core/domain and core/services 
components. Additionally, there is an abstraction layer for repositories. 
The architecture effectively separates concerns across configuration, web, 
and domain layers.

One of the key improvements we can focus on is upgrading the Java version 
to Java 17. This upgrade will allow us to leverage modern features,
enhance security, and improve code maintainability.
Once this is completed, we can proceed to adopt Spring Boot 3.
Keep updated in modern versions will not only keep us aligned with the 
latest technologies but also help us mitigate vulnerabilities and 
streamline future maintenance efforts.

## Application Structure

- configuration
- core
    - domain
    - repository
    - services
- web
    - dto

I have created a new directory under the web layer, 
named controllers, to organize and store the application's 
current controllers or resources.

Additionally, the services/model/Basket has been relocated 
to core/model/Basket to better align with the domain structure.
The final project structure is now as follows:

- configuration
- core
    - domain
    - model
    - repository
    - services
- web
    - dto
    - controllers

1. **configuration** - Configuration files like SwaggerConfiguration, Spring beans
   configuration definition, etc. I think a WebSecurityConfiguration file is needed
   in order to define the security of the resources.

2. **core** - Define here all the logic of the application
    - **domain** - Database entities
    - **model** - Model to work
    - **repository** - JPA interface to interact with database
    - **services** - Current implementation of the services. I think it
   is better to have interface per service in order to have a contract to
    follow and work with the implementation of the service we need.

3. **web** - Define the resources and the Data Transfer Objects to interact with
    - dto - Data Transfer Objects
    - controllers - Spring Controllers


## Configuration

Not applied any extra configuration, it is needed a Spring Security chain.
I think current SwaggerConfiguration can be removed as it is not defining any
specific rule for Swagger.

Some changes have been applied over application.yml in order to fix the access
to the database. The issue was related with the field minBasketValue from Coupon
due to JPA per default creates the fields without camelCase.

## Core

### **- core/domain/Coupon.java**

Needed to specify name of the table as we are working with JPA. GeneratedValue
is helpful in order to auto generate ids when the creation is happening.

### **- core/repository/CouponRepository**

A new method has been created in order to return a `List<Coupon>` instead to use
current `findByCode(final String code)`. I created this method because in the
CouponService we were calling inside a for loop a request to database. In order
to reduce the number of request we trigger to database we can improve our code
and do only one request to obtain a `List<Coupon>`.

### **- core/services/CouponService**

We can use a specific constructor to build our CouponService instead to 
use `@RequiredArgsConstructor` . This is useful for several reasons:
- Start removing dependency with Lombok(it will probably be replaced by Java records
  in Java 17)
- Allow us to control our constructor and in case the service grow too much
  we can realize and prevent it splitting the service

`public Optional<Basket> apply(final Basket basket, final String code)` has been
removed. The reason of that is because we should not mix concerns. We can
create a BasketService in order to interact with Basket logic. It will be better
in terms of separation of concerns, and it will make our code easier and more
readable. For other side, we will be able to test the services in a better
way as the service will be smaller.


`public Coupon createCoupon(final CouponDTO couponDTO)` has been modified in
order to show an error and return a value once a NullPointerException is trigger.
It is not a big improvement, but we can deal now with the issue of saving
in database an unexpected object.

`public List<Coupon> getCoupons(final CouponRequestDTO couponRequestDTO)` has
been modified in order to use the new method from CouponRepository. As you
can see the logic has been reduced and the performance improved as we are
using the database power to perform just one search and reduce the number
of request to database.

### **- core/services/BasketService**

The logic of this method has been simplified to make it more readable and
easier to understand avoiding if and else-if conditions.

## Web

### **- web/dto/CouponRequestDTO**

`@NoArgsConstructor` and `@AllArgsConstructor` were needed to be applied
because the service it was not able to build the object.


### **- web/controllers/CouponResource**

`@RequiredArgsConstructor` can be removed as we don't need to create this Resource.
I changed the @RequestMapping for /api/coupons as I extracted the
endpoint related with Basket entity to a new controller.

`@PostMapping("/create")` has been modified in order to return specific
ResponseEntity result in the case of a Bad Request. The status for a successful
response has been also changed from 200 to 201.

`@PostMapping("/")` (previously "/coupons") has been modified in order to
return a ResponseEntity.

### **- web/controllers/BasketResource**

As the service has been moved we should make use of this new BasketService. For
other side there was some commented code related with Swagger documentation.

## Testing

### Unit Testing

Current unit testing has been split due to creation of new BasketService. 
CouponApplicationTests can be removed as we are not doing anything with this file.

### Integration Testing

We were missing testing over the resources. We can use MockMvc from Spring
to test those components. This will allow us to test directly a request and
mock the logic behind like the service.