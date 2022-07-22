## cip-phone-number-validation

### Summary

Backend server for validating a given phone number

The default port for cip-phone-number-frontend is 6080
The default port for cip-phone-number is port 6081
The default port for cip-phone-number-validation is port 6082
The default port for cip-phone-number-verification is port 6083
The default port for cip-phone-number-stubs is port 6099
The default port for internal-auth is port 8470

### Testing

#### Unit tests

    sbt clean test

#### Integration tests

    sbt clean it:test

### Running app

    sm --start CIP_PHONE_NUMBER_VALIDATION_ALL

Run the services against the current versions in dev, stop the CIP_PHONE_NUMBER_VALIDATION service and start manually

    sm --start CIP_PHONE_NUMBER_VALIDATION_ALL -r
    sm --stop CIP_PHONE_NUMBER_VALIDATION
    cd cip-phone-number-validation
    sbt run 6082

For reference here are the details for running each of the services individually

    cd cip-phone-number-frontend
    sbt run
 
    cd cip-phone-number
    sbt run

    cd cip-phone-number-validation
    sbt run

    cd cip-phone-number-verification
    sbt run

### Curl microservice (for curl microservice build jobs)

#### Validate

    -XPOST -H "Content-type: application/json" -H "Authorization: <auth-token>" -d '{
	    "phoneNumber": "<phone-number>"
    }' 'https://cip-phone-number-validation.protected.mdtp/customer-insight-platform/phone-number/validate'

### License

This code is open source software licensed under
the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
