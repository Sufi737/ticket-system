## Employee service
Employee service provides REST endpoints to manage employees data. This service consists of the following entities:
1. Employee
2. Role
3. Department

The following endpoints are available for use:

#### Employee

| Method| Path | Description| Roles allowed |
| :---:   | :---: | :---: | :---: |
| GET |  employee/{id}  | Get employee details by ID   | ADMIN, USER |
| GET |  employee/{code}  | Get employee details by employee code   | ADMIN, USER |
| POST |  employee/  | Create an employee   | ADMIN  |
| PUT |  employee  | Update employee details   | ADMIN  |
| DELETE |  employee/{id}  | Delete an employee by ID | ADMIN |


#### Department

| Method| Path | Description| Roles allowed |
| :---:   | :---: | :---: | :---: |
| GET |  department/{id}  | Get department details by ID   | ADMIN |
| POST |  department/  | Create a department   | ADMIN  |
| PUT |  department  | Update department details   | ADMIN  |
| DELETE |  department/{id}  | Delete a department by ID | ADMIN |

#### Role

| Method| Path | Description| Roles allowed |
| :---:   | :---: | :---: | :---: |
| GET |  role/{id}  | Get role by ID   | ADMIN |
| POST |  role/  | Create a role   | ADMIN  |
| PUT |  role  | Update role details   | ADMIN  |
| DELETE |  role/{id}  | Delete a role by ID | ADMIN |

Note that department and role endpoints are only available for ADMIN role of Keycloak user. To understand Keycloak user roles refer to the Keycloak service of this repository

On how to use the above endpoints, refer the main README file of the repository