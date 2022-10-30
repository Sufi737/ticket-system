## Ticket service
This service allows creation of support tickets for client. This service consists of two entities Ticket and Status. A ticket is created by an employee, can be moved from one status to another and can be assigned to another employee.

Below are the REST endpoints available:

| Method | Path | Description | Roles Allowed |
| :---:   | :---: | :---: | :---: |
| GET | ticket/{code} | Get a ticket by code   | USER, ADMIN |
| POST | ticket/ | Create a ticket | USER, ADMIN |
| PUT | ticket/ | Update a ticket | USER, ADMIN |
| DELETE | ticket/{code} | Delete a ticket by code | ADMIN |

| Method | Path | Description | Roles Allowed |
| :---:   | :---: | :---: | :---: |
| GET | status/{code} | Get a status by code   | ADMIN |
| POST | status/ | Create a status | ADMIN |
| PUT | status/ | Update a status | ADMIN |
| DELETE | status/{code} | Delete a status by code | ADMIN |

Note that all above endpoints should never be called directly but via the Gateway server. Refer Gateway server in this repository for more details.