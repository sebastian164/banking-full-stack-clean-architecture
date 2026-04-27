Feature: Bank API integration tests

  Background:
    * url 'http://localhost:' + karate.properties['karate.port'] + '/api'

  Scenario: create and read a client
    Given path 'clients'
    And request
      """
      {
        "name": "Karate Client",
        "gender": "Otro",
        "age": 31,
        "identification": "karate-1001",
        "address": "Karate Street",
        "phone": "0991234567",
        "password": "1234",
        "status": true
      }
      """
    When method post
    Then status 201
    And match response.name == 'Karate Client'
    * def clientId = response.id

    Given path 'clients', clientId
    When method get
    Then status 200
    And match response.identification == 'karate-1001'

  Scenario: reject debit without available balance
    Given path 'movements'
    And request
      """
      {
        "date": "2026-04-26",
        "type": "DEBITO",
        "value": -10,
        "accountNumber": "496825"
      }
      """
    When method post
    Then status 400
    And match response.message == 'Saldo no disponible'

  Scenario: create a movement and generate report
    Given path 'movements'
    And request
      """
      {
        "date": "2026-04-26",
        "type": "CREDITO",
        "value": 35,
        "accountNumber": "225487"
      }
      """
    When method post
    Then status 201
    And match response.balance == 735

    Given path 'reports'
    And param clientId = 2
    And param startDate = '2026-04-01'
    And param endDate = '2026-04-30'
    When method get
    Then status 200
    And match response.clientName == 'Marianela Montalvo'
    And assert response.totalCredits >= 35
    And match response.pdfBase64 != ''
