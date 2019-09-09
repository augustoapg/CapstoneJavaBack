# CapstoneJavaBack
Backend in Java of the HMC Bike Rental Capstone project



## API List
| API Name  | Method | Description | 
| ---- | ---- | ---- |
| `/getBikes`  | GET | Get All Bikes |
| `/getLocks`  | GET | Get All Locks |
| `/getKeys`  | GET | Get All Keys |
| `/getLocks`  | GET | Get All Locks |
| `/getRentals`  | GET | Get All Rentals |
| `/getCustomers`  | GET | Get All Customers |
| `/getCustomer/{sheridanIdInput}`  | GET | Get Customer with ID |
| `/getRental/{id}`  | GET | Get Rental with ID |
| `/getActiveRentals`  | GET | Get Active Rentals |
| `/getActiveRental/{id}`  | GET | Get Active Rental with ID |
| `/getArchivedRentals`  | GET | Get Archived Rentals |
| `/editBike`  | PATCH | Edit Bike info with Bike JSON obj |
| `/returnRental`  | PATCH | Return Rental with Rental JSON obj |
| `/newCustomer`  | POST | Create new Customer with Customer JSON obj |
| `/newRental`  | POST | Create new Rental with Rental JSON obj |
| `/newBike`  | POST | Create new Bike with Bike JSON obj |
| `/editRental`  | PATCH | Edit Rental info with Rental JSON obj |
| `/editBike`  | PATCH | Edit Bike info with Bike JSON obj |

## Common Issues and Troubleshoot

### heroku
A problem that seems to be happening when calling the addDummyData endpoint is: 
> org.postgresql.util.PSQLException: FATAL: too many connections for role

To fix that, you need to run the following command in your terminal: `heroku pg:killall DATABASE_URL`. This will kill all connections and you should be able to call the addDummyData after that. To run this command you need to have heroku and postgress installed (windows), and add the postgress bin to the PATH variable. If it does not work in Bash, try in PowerShell.

Follow the same fix for the following error:
> org.postgresql.util.PSQLException: FATAL: terminating connection due to administrator command